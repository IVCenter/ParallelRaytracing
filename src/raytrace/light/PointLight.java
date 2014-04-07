package raytrace.light;

import math.Vector4;
import raytrace.data.IlluminationData;

public class PointLight extends Light {
	
	/*
	 * A basic point Light
	 */
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/

	@Override
	public IlluminationData illuminate(Vector4 point)
	{
		IlluminationData ildata = new IlluminationData();
		Vector4 toPoint = point.subtract3(position);
		double toPointMagSqrd = point.subtract3(position).magnitude3Sqrd();
		
		double attenuation = constantAttenuation +
							 linearAttenuation * Math.pow(toPointMagSqrd, 0.5) +
							 quadraticAttenuation * toPointMagSqrd;
		
		ildata.setColor(color.multiply3( intensity * ( 1.0 / attenuation) ));
		ildata.setDirection(toPoint.normalize3());
		ildata.setDistance(Math.pow(toPointMagSqrd, 0.5));
		
		return ildata;
	}

}
