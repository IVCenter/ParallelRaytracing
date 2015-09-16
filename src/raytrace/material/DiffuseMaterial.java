package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class DiffuseMaterial extends Material{
	
	/*
	 * An implementation of a material that diffusely reflects light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DiffuseMaterial(Texture tintTexture)
	{
		this.tintTexture = tintTexture;
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
		
		double DdotN = normal.dot(data.getRay().getDirection());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}

		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(Vector3.positiveYAxis)) == 1.0)
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
			
			shade.add3M(diffuse(light, idata.getNormal(), lightDirection));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			//Sample a random point
			
			//Add the direct shading and samples shading together
			shade.add3M(recurse(data, point, sampleDir, 1.0));
		}
		
		return shade.multiply3M(tint);
	}
	*/

	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 point = idata.getPoint();
		Vector3 normal = idata.getNormal().normalizeM();
		
		double DdotN = normal.dot(rdata.getRay().getDirection());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}
		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(Vector3.positiveYAxis)) == 1.0)
			uTangent = normal.cross(Vector3.cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(Vector3.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		//Cosine weighted sample
		Vector3 sampleDir = Vector3.cosineWeightedSample(uTangent, normal, vTangent);
		
		RayData newRData = new RayData(point, sampleDir, RayData.Type.REFLECT);
		
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
		
		Color result = diffuse(light, idata.getNormal(), lightDirection);
		
		return result.multiply3M(tint);
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		Color tint = tintTexture.evaluate(idata);
		
		return light.multiply3(tint);
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}
	
	

}