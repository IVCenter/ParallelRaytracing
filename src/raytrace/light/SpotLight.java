package raytrace.light;

import process.logging.Logger;
import math.Vector3;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;

public class SpotLight extends Light {

	/*
	 * A simple Spot Light class
	 */
	@Override
	public IlluminationData illuminate(ShadingData data, Vector3 point)
	{
		//TODO: Implement spot light
		
		Logger.warning(-1, "SpotLight is currently not supported. TODO");
		
		return null;
	}

}
