package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.medium.Medium;

public class MediumMaterial  extends Material{
	
	/*
	 * An implementation of a material that hands off to a medium
	 * Replaces "PassThroughMaterial" in functionality
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MediumMaterial(Medium medium)
	{
		this.medium = medium;
		this.affectedByLightSources = false;
	}
	

	/* *********************************************************************************************
	 * Materail Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 point = idata.getPoint();
		Vector3 direction = rdata.getRay().getDirection();
		
		RayData newRData = new RayData(point, direction, RayData.Type.TRASMIT);
		
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
		return light;
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}
	
}