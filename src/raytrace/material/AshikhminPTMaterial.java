package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class AshikhminPTMaterial extends Material{
	
	/*
	 * An implementation of the Ashikhmin BRDF
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color diffuseTint = new Color();
	protected Color specularTint = new Color();
	protected int sampleCount = 1;

	protected double specularReflectance = 0.5;
	protected double diffuseReflectance = 0.5;
	protected double uExponent = 0.0;
	protected double vExponent = 0.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public AshikhminPTMaterial(Color diffuseTint, Color specularTint, double specularReflectance,
			double diffuseReflectance, double uExponent, double vExponent, int sampleCount)
	{
		this.diffuseTint = diffuseTint;
		this.specularTint = specularTint;
		this.specularReflectance = specularReflectance;
		this.diffuseReflectance = diffuseReflectance;
		this.uExponent = uExponent;
		this.vExponent = vExponent;
		this.sampleCount = sampleCount;
	}
	

	@Override
	public Color shade(ShadingData data)
	{	
		Color shade = new Color(0x000000ff);
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal().normalize3();
		Vector4 rayDir = data.getRay().getDirection().multiply3(-1.0).normalize3();
		
		double DdotN = normal.dot3(rayDir);
		//If the normal is facing the wrong direction, flip it
		if(DdotN < 0.0) {
			normal = normal.multiply3(-1.0);
			DdotN = normal.dot3(rayDir);
		}
		
		
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(normal.dot3(positiveYAxis) == 1.0)
			uTangent = normal.cross3(cosineWeightedSample()).normalize3();
		else
			uTangent = normal.cross3(positiveYAxis).normalize3();
		vTangent = uTangent.cross3(normal).normalize3();
		
		
		//Direct Illumination
		IlluminationData ildata;
		Vector4 lightDir;
		Vector4 half;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			lightDir = ildata.getDirection().multiply3(-1.0).normalize3();
			half = halfVector(rayDir, lightDir);

			shade.add3M(ashikminSpecular(ildata.getColor(), rayDir, lightDir, normal, uTangent, vTangent, half));
			shade.add3M(ashikminDiffuse(ildata.getColor(), rayDir, lightDir, normal, uTangent, vTangent, half));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample random points
			Color rflectColor = new Color();
			Vector4 halfSample;
			Vector4 sample;
			for(int i = 0; i < sampleCount; ++i)
			{
				//Roll a random to trace either specular reflections or diffuse reflections
				if(Math.random() < specularReflectance)
				{

					//Specular Sample
					halfSample = createHalfSample(normal, uTangent, vTangent);
					sample = createSample(halfSample, rayDir);
					
					if(sample.dot3(normal) > 0.0)
					{
						rflectColor.add3M(recurse(data, point, sample, 1.0).multiply3(specularTint));	
					}
				
				}else{
					
					//Diffuse Sample
					sample = cosineWeightedSample(uTangent, normal, vTangent);
	
					rflectColor.add3M(recurse(data, point, sample, 1.0).multiply3(diffuseTint));	
				}
			}
			
			//Add the direct shading and samples shading together
			shade.add3M(rflectColor.multiply3M(1.0/sampleCount));
		}
		
		return shade;
	}
	
	private Color ashikminDiffuse(Color light, Vector4 k1/*rayDir*/, Vector4 k2/*lightDir*/, Vector4 n, Vector4 u, Vector4 v, Vector4 h)
	{	
		//Diffuse component
		double leadingCoeffDiff = ((28.0 * diffuseReflectance) / (23.0 * Math.PI)) * (1.0 - specularReflectance);
		
		double diffuseCoeff = leadingCoeffDiff * 
				(1.0 - Math.pow( (1.0 - 0.5 * Math.abs(n.dot3(k1))) , 5.0 )) * 
				(1.0 - Math.pow( (1.0 - 0.5 * Math.abs(n.dot3(k2))) , 5.0 ));
		
		return diffuseTint.multiply3(light).multiply3M(diffuseCoeff);
	}
	
	private Color ashikminSpecular(Color light, Vector4 k1/*rayDir*/, Vector4 k2/*lightDir*/, Vector4 n, Vector4 u, Vector4 v, Vector4 h)
	{	
		//Specular component
		double leadingCoeffSpec = (1.0 / (8.0 * Math.PI)) * Math.sqrt( (uExponent+ 1.0) * (vExponent + 1.0) );

		//Fresnel component
		double fspec = specularReflectance + (1.0 - specularReflectance) * Math.pow(1.0 - h.dot3(k2), 5.0);
		
		double topSpec = Math.pow( h.dot3(n) , ( 
				(uExponent * Math.pow(h.dot3(u), 2.0) + vExponent * Math.pow(h.dot3(v), 2.0)) / (1.0 - Math.pow(h.dot3(n), 2.0)) 
				) );
		
		double bottomSpec = Math.abs(h.dot3(k2)) * Math.max(Math.abs(n.dot3(k1)), Math.abs(n.dot3(k2)));
		
		double middleSpec = topSpec / bottomSpec;
		
		//If middle spec is over 1.0 then the specular reflection is generating energy! So clamp it
		if(middleSpec > 1.0)
			middleSpec = 1.0;

		
		double specularCoeff = leadingCoeffSpec * middleSpec * fspec;
		
		if(specularCoeff != specularCoeff || specularCoeff < 0.0)
			specularCoeff = 0.0;
		
		Color spec = specularTint.multiply3(light).multiply3M(specularCoeff);
		
		return spec;
	}
	
	private Vector4 createHalfSample(Vector4 normal, Vector4 uTangent, Vector4 vTangent)
	{
		double epsilon1 = Math.random();
		double epsilon2 = Math.random();
		
		double phi = Math.atan(Math.sqrt( (uExponent + 1.0) / (vExponent + 1.0) ) * Math.tan(0.5 * Math.PI * epsilon1));
		
		double cosPhiSqrd = Math.cos(phi);
		cosPhiSqrd *= cosPhiSqrd;
		double sinPhiSqrd = Math.sin(phi);
		sinPhiSqrd *= sinPhiSqrd;
		
		double theta = Math.acos(Math.pow( (1.0 - epsilon2), ( 1.0 / (uExponent * cosPhiSqrd + vExponent * sinPhiSqrd + 1.0) ) ));

		double xCoeff = Math.sin(theta) * Math.cos(phi);
		double yCoeff = Math.sin(theta) * Math.sin(phi);
		double zCoeff = Math.cos(theta);
		
		Vector4 half = new Vector4();
		
		double quad = Math.random();
		double quad2 = quad < 0.5 ? -1.0 : 1.0;
		double quad1 = quad < 0.25 || quad >= 0.75 ? 1.0 : -1.0;
		
		half = half.addMultiRight3(normal, zCoeff).addMultiRight3(vTangent, yCoeff * quad2).addMultiRight3(uTangent, xCoeff * quad1);
		
		return half.normalize3();
	}
	
	private Vector4 createSample(Vector4 half, Vector4 k1/*rayDir*/)
	{
		return k1.multiply3(-1.0).add3(half.multiply3(half.dot3(k1) * 2.0)).normalize3();
	}

}