package raytrace.medium;

import math.Vector3;
import math.ray.Ray;
import raytrace.color.Color;
import raytrace.data.RayData;
import system.Constants;

public class VacuumMedium extends Medium {

	/*
	 * An implementation of a vacuum media
	 */
	
	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public VacuumMedium()
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
		return light;
	}

	@Override
	public RayData sample(Vector3 startPoint, Vector3 endPoint)
	{
		Vector3 direction = startPoint.subtract(endPoint).normalizeM();
		
		RayData rdata = new RayData(new Ray(startPoint, direction, 0, 0), Constants.RECURSIVE_EPSILON, Double.POSITIVE_INFINITY);
		
		return rdata;
	}

}
