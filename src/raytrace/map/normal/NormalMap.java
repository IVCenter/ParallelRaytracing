package raytrace.map.normal;

import math.Vector3;
import raytrace.data.IntersectionData;

public interface NormalMap {
	
	/*
	 * A base class for normal maps
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public abstract Vector3 evaluate(IntersectionData data);

}