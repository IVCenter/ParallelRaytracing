package raytrace.light;

import process.logging.Logger;
import math.Vector3;
import raytrace.data.IlluminationData;
import raytrace.scene.Scene;

public class SpotLight extends Light {

	/*
	 * A simple Spot Light class
	 */
	@Override
	public IlluminationData illuminate(Scene scene, Vector3 point)
	{
		//TODO: Implement spot light
		
		Logger.warning(-1, "SpotLight is currently not supported. TODO");
		
		return null;
	}

}
