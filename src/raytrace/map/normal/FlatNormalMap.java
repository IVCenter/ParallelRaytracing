package raytrace.map.normal;

import math.Vector4;
import raytrace.data.IntersectionData;

public class FlatNormalMap implements NormalMap {

	/*
	 * A normal map that is always flat (returns the original normal)
	 */
	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 evaluate(IntersectionData data)
	{
		return data.getNormal();
	}

}
