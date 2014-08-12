package raytrace.light;

import math.Vector3;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;

public class AmbientLight extends Light {
	
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
		
		ildata.setColor(color.multiply3(intensity));
		ildata.setDirection(new Vector3());
		ildata.setDistance(Double.MAX_VALUE);
				
		return ildata;
	}

}
