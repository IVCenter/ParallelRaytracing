package raytrace.light;

import process.logging.Logger;
import math.Vector4;
import raytrace.data.IlluminationData;

public class SpotLight extends Light {

	/*
	 * A simple Spot Light class
	 */
	@Override
	public IlluminationData illuminate(Vector4 point)
	{
		//TODO: Implement spot light
		
		Logger.warning(-1, "SpotLight is currently not supported. TODO");
		
		return null;
	}

}
