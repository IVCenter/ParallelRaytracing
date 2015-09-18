package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class ReflectiveMaterial  extends Material {
	
	/*
	 * An implementation of a material that uniformly reflects light across its surface normal
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	double reflectivePercent = 0.5;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ReflectiveMaterial(Texture tintTexture, double reflectivePercent)
	{
		this.tintTexture = tintTexture;
		this.reflectivePercent = reflectivePercent;
		
		this.affectedByLightSources = false;
		this.emitsLight = false;
	}	
	

	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 point = idata.getPoint();
		Vector3 normal = idata.getNormal().normalizeM();
		Vector3 ray = rdata.getRay().getDirection();
		
		double DdotN = normal.dot(ray);
		
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}

		//Create a new sample reflected across the normal
		RayData newRData = new RayData(point, reflect(ray, point, normal), RayData.Type.REFLECT);
		
		return newRData;
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		return Color.black();
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(idata);	
		
		//If reflective, calculate how much light was not absorbed
		Color unabsorbedLight;
		unabsorbedLight = light.multiply3(reflectivePercent);
		
		//Calculate the reflected light
		Color reflectedLight = unabsorbedLight.multiply3M(tint);
		
		return reflectedLight;
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}
	
}