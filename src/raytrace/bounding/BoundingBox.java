package raytrace.bounding;

import math.Vector4;

public class BoundingBox {
	
	/*
	 * A simple Bounding Box
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	public Vector4 min;
	public Vector4 max;
	

	/* *********************************************************************************************
	 * Main
	 * *********************************************************************************************/
	public BoundingBox()
	{
		min = new Vector4();
		max = new Vector4();
		
		clear();
	}
	
	public BoundingBox(Vector4 min, Vector4 max)
	{
		this.min = min;
		this.max = max;
		
		clear();
	}

	/* *********************************************************************************************
	 * Reset Method
	 * *********************************************************************************************/
	public void clear()
	{
		min.x = Double.POSITIVE_INFINITY;
		min.y = Double.POSITIVE_INFINITY;
		min.z = Double.POSITIVE_INFINITY;

		max.x = Double.NEGATIVE_INFINITY;
		max.y = Double.NEGATIVE_INFINITY;
		max.z = Double.NEGATIVE_INFINITY;
	}
	
}
