package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class SubsurfaceScatterPTMaterial extends Material{
	
	/*
	 * A path tracing implementation of a sub-surface scattering material
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture texture;
	protected double refractiveIndex = AIR_REFRACTIVE_INDEX;
	protected double absorptionCoeff = 0.5;
	protected double scatteringCoeff = 0.5;
	protected double radius = 1.0;
	
	protected Material material;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SubsurfaceScatterPTMaterial(Material material, Texture texture, double refractiveIndex, double absorptionCoeff, double scatteringCoeff,
			double radius)
	{
		this.texture = texture;
		this.refractiveIndex = refractiveIndex;
		this.absorptionCoeff = absorptionCoeff;
		this.scatteringCoeff = scatteringCoeff;
		this.radius = radius;
		
		this.material = material;
	}


	/* *********************************************************************************************
	 * Material Override
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		//Get color from texture
		Color tint = texture.evaluate(data.getIntersectionData());
		
		//Setup the point of intersection
		Vector4 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		Vector4 normal = data.getIntersectionData().getNormal();
		
		//Ray Direction
		Vector4 rayDir = (new Vector4(data.getRay().getDirection())).normalize3M();
		
		
		double DdotN = normal.dot3(rayDir);
		double thisRefractiveIndex = refractiveIndex;
		boolean exiting = false;
		
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			//TODO: Would it be better to use a refractiveIndex stack?
			//thisRefractiveIndex = AIR_REFRACTIVE_INDEX;//TODO: Is this the right test, and right place to assume we are exiting the material?
			//normal = normal.multiply3(-1.0);
			//DdotN *= -1.0;
			
			exiting = true;
			
			//return Color.black();
		}
		
		
		//Basis, used for sampling
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(Math.abs(normal.dot3(positiveYAxis)) == 1.0)
			uTangent = normal.cross3(positiveXAxis).normalize3M();
		else
			uTangent = normal.cross3(positiveYAxis).normalize3M();
		vTangent = uTangent.cross3(normal).normalize3M();
		
		
		
		
		//Calculate the Fresnel transmittance
		//TODO: What do we do with this?
		double fresnelTrans = 0.0;
		
		
		//Total absorption
		double absorption = absorptionCoeff + scatteringCoeff;
		double alphaPrime = scatteringCoeff / absorption;
		
		//Other coeffs
		double D = (1.0/3.0) * absorption;
		double sigma = Math.sqrt(3.0 * absorptionCoeff * absorption);
		
		
		//Fresnel Reflectance
		double Fdr = (-1.440 / (refractiveIndex * refractiveIndex)) + (0.710 / refractiveIndex) + (0.668) + (0.0636 * refractiveIndex);
		
		
		//Reflectance ratio
		double A = (1.0 * Fdr) / (1.0 - Fdr);
		
		
		//The Z distances
		double zr = 1.0/absorption;
		double zv = zr + 4.0 * A * D;
		
		
		//The disk sample
		Vector4 diskSample = diskSample(radius, 1.0);
		Vector4 orientedDiskSample = point.add3(uTangent.multiply3(diskSample.get(0))).add3(vTangent.multiply3(diskSample.get(1)));
		
		//orientedDiskSample = point.add3(uniformSphereSample().multiply3M(radius));
		
		//The X sources
		Vector4 xr = orientedDiskSample.subtract3(normal.multiply3(zr));
		Vector4 xv = orientedDiskSample.add3(normal.multiply3(zv));
		
		
		//The S distances
		double sr = xr.distance(point);
		double sv = xv.distance(point);
		
		
		
		double leadingCoeff = alphaPrime / (4.0 * Math.PI);

		//Color cr = lightForPoint(data, xr, zr, sigma, sr);
		//Color cv = lightForPoint(data, xv, zv, sigma, sv);

		//Color cr = lightForPoint(data, point, zr, sigma, sr);
		//Color cv = lightForPoint(data, point, zv, sigma, sv);
		
		
		//Shade
		//Color shade = cr.add3M(cv).multiply3M(leadingCoeff);
		
		Color shade = material.shade(data);
		//shade = Color.black();
		
		//Get direct lighting?
		
		//Get lighting for samples
		Color recursionColor = null;
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//TODO; Make this PT
			Color rcr = attenuate(recurse(data, xr, uniformSphereSample(), thisRefractiveIndex), zr, sigma, sr);
			Color rcv = attenuate(recurse(data, xv, uniformSphereSample(), thisRefractiveIndex), zv, sigma, sv);
			
			recursionColor = (rcr).add3(rcv).multiply3M(leadingCoeff);
			//recursionColor = Color.black();
			
		}else{
			recursionColor = new Color();
		}
		
		
		return shade.add3M(recursionColor);
	}
	/*
	//Get a random sample on a disk
	private Vector4 diskSample(double radius, double weight)
	{
		Vector4 sample = new Vector4();
		double theta = Math.random() * Math.PI * 2.0;
		double distance = Math.pow(Math.random(), weight) * radius;
		sample.set(distance * Math.cos(theta), distance * Math.sin(theta), 0, 0);
		return sample;
	}
	
	private Vector4 uniformSphereSample()
	{
		Vector4 sample = new Vector4(10.0, 10.0, 10.0, 0.0);
		while(sample.magnitude3Sqrd() > 1.0) {
			sample.set(2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 0);
		}
		return sample.normalize3();
	}
	*/
	
	private Color lightForPoint(ShadingData data, Vector4 point, double z, double sigma, double s)
	{
		//The Shade
		Color shade = new Color();
		
		//Direct Illumination
		IlluminationData ildata;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(attenuate(ildata.getColor(), z, sigma, s));
		}
		
		return shade;
	}
	
	private Color attenuate(Color light, double z, double sigma, double s)
	{
		double d = z * (1.0 * sigma * s) * (Math.exp(-1.0 * sigma * s)) * (1.0 / Math.pow(s, 3.0));
		return light.multiply3(d);
	}

}
