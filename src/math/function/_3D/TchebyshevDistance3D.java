package math.function._3D;

import math.Vector3;

public class TchebyshevDistance3D implements Function3D<Vector3, Vector3, Double> {
	
	/*
	 * A function for calculating the tchebyshev distance between two points in 3D space.
	 */
	
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	@Override
	public Double evaluate(Vector3 x, Vector3 y)
	{
		return x.tchebyshevDistance(y);
	}

}
