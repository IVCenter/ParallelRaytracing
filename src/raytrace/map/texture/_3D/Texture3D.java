package raytrace.map.texture._3D;

import math.Vector3;
import math.map.Map3D;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.map.texture.Texture;

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
	
	public Color evaluate(Vector3 point)
	{
		return evaluate(point.get(0), point.get(1), point.get(2));
	}
}
