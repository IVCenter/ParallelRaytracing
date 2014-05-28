package math.map;

import math.function._3D.Function3D;

public interface Map2D<DEPENDENT_Z> extends Function3D<Double, Double, DEPENDENT_Z> {
	
	/*
	 * A base class for maps like textures, normal maps, height fields, masks, etc.
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public DEPENDENT_Z evaluate(Double x, Double y);

}
