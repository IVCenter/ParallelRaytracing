package raytrace.map.normal;

import math.Vector3;
import raytrace.data.IntersectionData;

public class FlatNormalMap implements NormalMap {

	/*
	 * A normal map that is always flat (returns the original normal)
	 */
	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector3 evaluate(IntersectionData data)
	{
		return data.getNormal();
	}

}
