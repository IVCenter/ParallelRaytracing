package raytrace.map;

import raytrace.color.Color;

public interface Texture1D extends Map1D<Color> {
	
	/*
	 * A base class for 1D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x);

}
