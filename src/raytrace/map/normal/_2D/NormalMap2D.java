package raytrace.map.normal._2D;

import math.Vector3;
import math.map.Map2D;
import raytrace.data.IntersectionData;
import raytrace.map.normal.NormalMap;

public abstract class NormalMap2D implements Map2D<Vector3>, NormalMap {
	
	/*
	 * A base class for 2D normal maps
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Vector3 evaluate(Double x, Double y);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector3 evaluate(IntersectionData data)
	{
		double[] texcoords = data.getTexcoord().getArray();
		return evaluate(texcoords[0], texcoords[1]);
	}

}
