package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class FresnelDiffuseMaterial extends Material {
	
	/*
	 * An implementation of a diffuse material that has a fresnel effect at hard angles
	 */
	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static final int SPECULAR = 0;
	protected static final int DIFFUSE = 1;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	protected double reflectiveRadius = 0.5;
	protected double schlickExponent = 5.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FresnelDiffuseMaterial(Texture tintTexture, double reflectiveRadius, double schlickExponent)
	{
		this.tintTexture = tintTexture;
		this.reflectiveRadius = reflectiveRadius;
		this.schlickExponent = schlickExponent;
	}
	
	/*
	@Override
	public Color shade(ShadingData data)
	{
		//Storage for result color
		Color shade = new Color(0x000000ff);
		
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector3 point = data.getIntersectionData().getPoint();
		Vector3 normal = data.getIntersectionData().getNormal().normalizeM();
		Vector3 rayDir = data.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(rayDir);
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(rayDir);
		}

		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(normal.dot(Vector3.positiveYAxis) == 1.0)
			uTangent = normal.cross(Vector3.cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(Vector3.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		
		//Direct Illumination
		IlluminationData ildata;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()).multiply3(tint));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector3 sampleDir = Vector3.cosineWeightedSample(uTangent, normal, vTangent);
			double reflectiveCoeff = Math.pow((reflectiveRadius - DdotN) / (reflectiveRadius), schlickExponent);;
			
			//if reflecting
			//Add the direct shading and samples shading together
			if(DdotN < reflectiveRadius && Math.random() < reflectiveCoeff)
			{
				shade.add3M(rflectColor.add3M(reflect(data, point, normal, AIR_REFRACTIVE_INDEX)));
			}else{
				shade.add3M(rflectColor.add3M(recurse(data, point, sampleDir, 1.0).multiply3(tint)));
			}	
		}
		
		return shade;
	}
	*/

	

	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 point = idata.getPoint();
		Vector3 normal = idata.getNormal().normalizeM();
		Vector3 ray = rdata.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(ray);
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(ray);
		}
		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(normal.dot(Vector3.positiveYAxis) == 1.0)
			uTangent = normal.cross(Vector3.cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(Vector3.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		//Calculate the reflective coefficient
		double reflectiveCoeff = Math.pow((reflectiveRadius - DdotN) / (reflectiveRadius), schlickExponent);
		
		//Create a new sample RayData
		RayData newRData = new RayData(point, null, RayData.Type.REFLECT);
		
		//if reflecting
		//Add the direct shading and samples shading together
		if(DdotN < reflectiveRadius && Math.random() < reflectiveCoeff)
		{
			newRData.getRay().setDirection(reflect(ray, point, normal));
			newRData.setSubType(SPECULAR);
		}else{
			
			newRData.getRay().setDirection(Vector3.cosineWeightedSample(uTangent, normal, vTangent));
			newRData.setSubType(DIFFUSE);
		}	
		
		return newRData;
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		Color tint = tintTexture.evaluate(idata);
		
		Vector3 normal = idata.getNormal().normalizeM();
		
		double DdotN = normal.dot(rdata.getRay().getDirection());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}
		
		Color result = diffuse(light, normal, lightDirection.normalize());
		
		return result.multiply3M(tint);
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		if(sample.getSubType() == SPECULAR)
		{
			return light;
		}else{
			return light.multiply3(tintTexture.evaluate(idata));
		}
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}


}