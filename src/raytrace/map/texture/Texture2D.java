package raytrace.map.texture;

import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.map.Map2D;

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
