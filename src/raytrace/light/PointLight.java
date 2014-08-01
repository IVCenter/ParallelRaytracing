package raytrace.light;

import math.Ray;
import math.Vector3;
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
	public IlluminationData illuminate(ShadingData data, Vector3 point)
	{
		IlluminationData ildata = new IlluminationData();
		Vector3 toPoint = point.subtract(position);
		double toPointMagSqrd = toPoint.magnitudeSqrd();
		double distance = Math.sqrt(toPointMagSqrd);
		
		double attenuation = constantAttenuation +
							 linearAttenuation * distance +
							 quadraticAttenuation * toPointMagSqrd;
		
		
		IntersectionData shadowData = shadowed(data.getRootScene(), 
				new Ray(point, toPoint.multiply(-1.0), 0, 0), distance);
		
		double blocked = shadowData == null ? 1.0 : 0.0;
		
		
		ildata.setColor(color.multiply3( intensity * ( 1.0 / attenuation) * blocked));
		ildata.setDirection(toPoint.normalizeM());
		ildata.setDistance(distance);
		
		return ildata;
	}

}
