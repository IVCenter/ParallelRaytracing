package raytrace.map.normal;

import math.Vector4;
import raytrace.data.IntersectionData;

public interface NormalMap {
	
	/*
	 * A base class for normal maps
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public abstract Vector4 evaluate(IntersectionData data);

}