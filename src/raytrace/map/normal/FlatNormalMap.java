package raytrace.map.normal;

import math.Vector4;
import raytrace.data.IntersectionData;

public class FlatNormalMap implements NormalMap {

	/*
	 * A normal map that is always flat
	 */
	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 evaluate(IntersectionData data)
	{
		return new Vector4(0, 1, 0, 0);
	}

}
