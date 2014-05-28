package raytrace.map.texture._2D;

import math.map.Map2D;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.map.texture.Texture;

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
