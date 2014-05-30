package raytrace.map.normal._1D;

import math.Vector4;
import math.map.Map1D;
import raytrace.data.IntersectionData;
import raytrace.map.normal.NormalMap;

public abstract class NormalMap1D implements Map1D<Vector4>, NormalMap {
	
	/*
	 * A base class for 1D normal maps
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Vector4 evaluate(Double x);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 evaluate(IntersectionData data)
	{
		//TODO: Do something a little more clever here.....
		return evaluate(data.getLocalPoint().magnitude3());
	}

}
