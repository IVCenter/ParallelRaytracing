package raytrace.medium;

import math.Vector3;
import math.ray.Ray;
import raytrace.color.Color;
import raytrace.data.RayData;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.Texture3D;
import system.Constants;

public class DielectricMedium extends Medium {
	
	/*
	 * An implementation of dielectric media
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D absorption = new ColorTexture3D(Color.white());
	
	
	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public DielectricMedium()
	{
		this.type = Type.PURE;
	}

	
	/* *********************************************************************************************
	 * Medium Overrides
	 * *********************************************************************************************/
	@Override
	public Color scatterIn(Vector3 startPoint, Vector3 endPoint, Color light, Vector3 lightDirection)
	{
		return Color.black();
	}

	@Override
	public Color transmit(Vector3 startPoint, Vector3 endPoint, Color light)
	{
		Vector3 exitDirection = endPoint.subtract(startPoint);
		double distance = exitDirection.magnitude();
		
		Color a = absorption.evaluate(startPoint);
		
		//TODO: BEERS LAW
		
		return a;
	}

	@Override
	public RayData sample(Vector3 startPoint, Vector3 endPoint)
	{
		Vector3 direction = startPoint.subtract(endPoint).normalizeM();
		
		RayData rdata = new RayData(new Ray(startPoint, direction, 0, 0), Constants.RECURSIVE_EPSILON, Double.POSITIVE_INFINITY);
		
		return rdata;
	}

}
