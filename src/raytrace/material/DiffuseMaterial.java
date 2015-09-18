package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class DiffuseMaterial extends Material {
	
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


	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
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