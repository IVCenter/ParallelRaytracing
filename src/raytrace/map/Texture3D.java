package raytrace.map;

import raytrace.color.Color;

public interface Texture3D extends Map3D<Color> {
	
	/*
	 * A base class for 3D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x, Double y, Double z);

}
