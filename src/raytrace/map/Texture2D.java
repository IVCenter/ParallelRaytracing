package raytrace.map;

import raytrace.color.Color;

public interface Texture2D extends Map2D<Color> {
	
	/*
	 * A base class for 2D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x, Double y);

}
