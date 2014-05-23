package raytrace.map;

import raytrace.color.Color;
import raytrace.data.IntersectionData;

public abstract class Texture3D implements Map3D<Color>, Texture {
	
	/*
	 * A base class for 3D textures
	 */

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Override
	public abstract Color evaluate(Double x, Double y, Double z);

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Color evaluate(IntersectionData data)
	{
		double[] point = data.getLocalPoint().getArray();
		return evaluate(point[0], point[1], point[2]);
	}
}
