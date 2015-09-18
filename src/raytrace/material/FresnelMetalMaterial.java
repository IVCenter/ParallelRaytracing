package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class FresnelMetalMaterial extends Material {
	
	/*
	 * An implementation of a fresnel metal material with n and k parameters
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	double refractiveReal = 2.485;//Steel
	double refractiveImaginary = 3.433;//Steel
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FresnelMetalMaterial(Texture tintTexture, double refractiveReal, double refractiveImaginary)
	{
		this.tintTexture = tintTexture;
		this.refractiveReal = refractiveReal;
		this.refractiveImaginary = refractiveImaginary;
		
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
		
		//Calculate the reflective percent
		double reflectivePercent = reflectivePercent(normal, ray);

		//If reflective, go divin'
		RayData newRData = new RayData(point, null, RayData.Type.REFLECT);
		if(reflectivePercent != 0.0)
		{
			newRData.getRay().setDirection(reflect(ray, point, normal));
			return newRData;
		}
		
		return null;
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
		
		Vector3 normal = idata.getNormal().normalizeM();
		Vector3 ray = rdata.getRay().getDirection();
		
		double DdotN = normal.dot(ray.normalize());
		//If the normal is facing in the wrong direction, flip it
		if(DdotN > 0.0) {
			normal = normal.multiply(-1.0);
		}
		
		//Calculate the reflective percent
		double baseReflectivePercent = reflectivePercent(normal, normal.multiply(-1.0));
		double reflectivePercent = reflectivePercent(normal, ray);
		double falloffReflection = 0.0;
				
		if(baseReflectivePercent < 1.0)
			falloffReflection = (reflectivePercent - baseReflectivePercent) / (1.0 - baseReflectivePercent);
		
		if(falloffReflection < 0.0)
			falloffReflection = 0.0;
		
		//If reflective, calculate how much light was not absorbed
		Color unabsorbedLight;
		if(reflectivePercent != 0.0)
		{
			unabsorbedLight = light.multiply3(reflectivePercent);
		}else{
			return Color.black();
		}
		
		//Calculate the reflected light
		Color reflectedLight = unabsorbedLight.multiply3M(tint.mixWithWhite(1.0 - falloffReflection, falloffReflection));
		
		return reflectedLight;
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}
	
	
	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected double reflectivePercent(Vector3 normal, Vector3 ray)
	{
		//Calculate percent reflective
		double NdotD = normal.dot(ray.normalize());
		double NdotD2 = NdotD * NdotD;
		double riReal2 = refractiveReal * refractiveReal;
		double riImag2 = refractiveImaginary * refractiveImaginary;
		double ri2Sum = (riReal2 + riImag2);
		
		double rpLeft = ri2Sum * NdotD2;
		double rpRight = 2.0 * refractiveReal * NdotD;
		
		double reflParallel = (rpLeft + rpRight + 1.0) / (rpLeft - rpRight + 1.0);
		double reflPerp = (ri2Sum + NdotD2 + rpRight) / (ri2Sum + NdotD2 - rpRight);

		double reflectivePercent = 0.5 * (reflParallel * reflParallel + reflPerp * reflPerp);
		reflectivePercent = reflectivePercent < 0.0 ? 0.0 : (reflectivePercent > 1.0 ? 1.0 : reflectivePercent);
		
		return reflectivePercent;
	}

}