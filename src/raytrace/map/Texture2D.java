package raytrace.map;

import raytrace.color.Color;
import raytrace.data.IntersectionData;

public abstract class Texture2D implements Map2D<Color>, Texture {
	
	/*
	 * A base class for 2D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x, Double y);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Color evaluate(IntersectionData data)
	{
		double[] texcoords = data.getTexcoord().getArray();
		return evaluate(texcoords[0], texcoords[1]);
	}

}
