package raytrace.bounding;

import raytrace.data.RayData;
import math.Ray;
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
	 * Constructors
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
	}
	

	/* *********************************************************************************************
	 * Test Methods
	 * *********************************************************************************************/
	public boolean intersects(RayData data)
	{
		//TODO
		Ray ray = data.getRay();
		double[] minm = min.getM();
		double[] maxm = max.getM();
		double[] o = ray.getOrigin().getM();
		double[] d = ray.getDirection().getM();

		double[] tmin = {(minm[0] - o[0]) / d[0], (minm[1] - o[1]) / d[1], (minm[2] - o[2]) / d[2]};
		double[] tmax = {(maxm[0] - o[0]) / d[0], (maxm[1] - o[1]) / d[1], (maxm[2] - o[2]) / d[2]};

		double absmin = Math.max(Math.min(tmin[0], tmax[0]), Math.max(Math.min(tmin[1], tmax[1]), Math.min(tmin[2], tmax[2])));
		double absmax = Math.min(Math.max(tmin[0], tmax[0]), Math.min(Math.max(tmin[1], tmax[1]), Math.max(tmin[2], tmax[2])));
		
		if(absmin > absmax)
			return false;
		
		if(absmin >= data.getTStart() && absmin < data.getTEnd())
			return true;
		
		if(absmin < data.getTStart() && absmax >= data.getTStart() && absmax < data.getTEnd())
			return true;
		
		return false;
	}
	
	public Vector4 getMidpoint()
	{
		return min.add3(max).multiply3(0.5);
	}
	
	public double getSurfaceArea()
	{
		double[] minm = min.getM();
		double[] maxm = max.getM();

		double x = maxm[0]-minm[0];
		double y = maxm[1]-minm[1];
		double z = maxm[2]-minm[2];
		
		return 2.0 * ( (x * y) + (x * z) + (y * z) );
	}

	/* *********************************************************************************************
	 * Reset Method
	 * *********************************************************************************************/
	public void clear()
	{
		min.set(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, 0);
		//max.set(-1.0 * (Double.MAX_VALUE-1.0), -1.0 * (Double.MAX_VALUE-1.0), -1.0 * (Double.MAX_VALUE-1.0), 0);
		max.set((-9999999.0), (-9999999.0), (-9999999.0), 0);
	}
	
}
