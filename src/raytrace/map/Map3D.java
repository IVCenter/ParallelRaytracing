package raytrace.map;

import math.function._4D.Function4D;

public interface Map3D<DEPENDENT_W> extends Function4D<Double, Double, Double, DEPENDENT_W> {
	
	/*
	 * A base class for maps like textures, normal maps, height fields, masks, etc.
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract DEPENDENT_W evaluate(Double x, Double y, Double z);

}