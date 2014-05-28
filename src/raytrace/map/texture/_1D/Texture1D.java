package raytrace.map.texture._1D;

import math.map.Map1D;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.map.texture.Texture;

public abstract class Texture1D implements Map1D<Color>, Texture {
	
	/*
	 * A base class for 1D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Color evaluate(IntersectionData data)
	{
		//TODO: Do something a little more clever here.....
		return evaluate(data.getLocalPoint().magnitude3());
	}

}
