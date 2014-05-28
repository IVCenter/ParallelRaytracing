package math.map;

import math.function._2D.Function2D;

public interface Map1D<DEPENDENT_Y> extends Function2D<Double, DEPENDENT_Y> {
	
	/*
	 * A base class for maps like textures, normal maps, height fields, masks, etc.
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public DEPENDENT_Y evaluate(Double x);

}
