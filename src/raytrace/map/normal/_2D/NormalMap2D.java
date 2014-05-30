package raytrace.map.normal._2D;

import math.Vector4;
import math.map.Map2D;
import raytrace.data.IntersectionData;
import raytrace.map.normal.NormalMap;

public abstract class NormalMap2D implements Map2D<Vector4>, NormalMap {
	
	/*
	 * A base class for 2D normal maps
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Vector4 evaluate(Double x, Double y);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 evaluate(IntersectionData data)
	{
		double[] texcoords = data.getTexcoord().getArray();
		return evaluate(texcoords[0], texcoords[1]);
	}

}
