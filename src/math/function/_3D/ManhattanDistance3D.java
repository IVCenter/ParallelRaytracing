package math.function._3D;

import math.Vector4;

public class ManhattanDistance3D implements Function3D<Vector4, Vector4, Double> {
	
	/*
	 * A function for calculating the manhattan distance between two points in 3D space.
	 */
	
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	@Override
	public Double evaluate(Vector4 x, Vector4 y)
	{
		return x.manhattanDistance(y);
	}

}
