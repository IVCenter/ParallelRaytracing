package raytrace.map;

import math.function._3D.Function3D;

public abstract class Map2D<DEPENDENT_Z> implements Function3D<Double, Double, DEPENDENT_Z> {
	
	/*
	 * A base class for maps like textures, normal maps, height fields, masks, etc.
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract DEPENDENT_Z eval(Double x, Double y);

}
