package raytrace.map.normal._3D;

import math.Vector3;
import math.map.Map3D;
import raytrace.data.IntersectionData;
import raytrace.map.normal.NormalMap;

public abstract class NormalMap3D implements Map3D<Vector3>, NormalMap {
	
	/*
	 * A base class for 3D normal maps
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Vector3 evaluate(Double x, Double y, Double z);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector3 evaluate(IntersectionData data)
	{
		double[] point = data.getLocalPoint().getArray();
		return evaluate(point[0], point[1], point[2]);
	}

}
