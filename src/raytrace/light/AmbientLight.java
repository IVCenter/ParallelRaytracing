package raytrace.light;

import math.Vector3;
import raytrace.data.IlluminationData;
import raytrace.scene.Scene;

public class AmbientLight extends Light {
	
	/*
	 * A basic point Light
	 */
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/

	@Override
	public IlluminationData illuminate(Scene scene, Vector3 point)
	{
		IlluminationData ildata = new IlluminationData();
		
		ildata.setColor(color.multiply3(intensity));
		ildata.setDirection(new Vector3());
		ildata.setDistance(Double.POSITIVE_INFINITY);
				
		return ildata;
	}

}
