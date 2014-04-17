package raytrace.light;

import math.Ray;
import math.Vector4;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;

public class PointLight extends Light {
	
	/*
	 * A basic point Light
	 */
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/

	@Override
	public IlluminationData illuminate(ShadingData data, Vector4 point)
	{
		IlluminationData ildata = new IlluminationData();
		Vector4 toPoint = point.subtract3(position);
		double toPointMagSqrd = toPoint.magnitude3Sqrd();
		double distance = Math.sqrt(toPointMagSqrd);
		
		double attenuation = constantAttenuation +
							 linearAttenuation * distance +
							 quadraticAttenuation * toPointMagSqrd;
		
		
		IntersectionData shadowData = shadowed(data.getRootScene(), 
				new Ray(point, toPoint.multiply3(-1.0), 0, 0), distance);
		
		double blocked = shadowData == null ? 1.0 : 0.0;
		
		
		ildata.setColor(color.multiply3( intensity * ( 1.0 / attenuation) * blocked));
		ildata.setDirection(toPoint.normalize3());
		ildata.setDistance(distance);
		
		return ildata;
	}

}
