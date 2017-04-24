package raytrace.bounding;

import raytrace.data.RayData;
import math.Vector3;
import math.ray.Ray;

public class BoundingBox {
	
	/*
	 * A simple Bounding Box
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	public Vector3 min;
	public Vector3 max;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public BoundingBox()
	{
		min = new Vector3();
		max = new Vector3();
		
		clear();
	}
	
	public BoundingBox(Vector3 min, Vector3 max)
	{
		this.min = min;
		this.max = max;
	}
	

	/* *********************************************************************************************
	 * Test Methods
	 * *********************************************************************************************/
	public double intersects(RayData data)
	{
		Ray ray = data.getRay();
		double[] minm = min.getArray();
		double[] maxm = max.getArray();
		double[] o = ray.getOrigin().getArray();
		double[] d = ray.getDirection().getArray();

		double[] tmin = {(minm[0] - o[0]) / d[0], (minm[1] - o[1]) / d[1], (minm[2] - o[2]) / d[2]};
		double[] tmax = {(maxm[0] - o[0]) / d[0], (maxm[1] - o[1]) / d[1], (maxm[2] - o[2]) / d[2]};

		//TODO: is this too slow?
		//double absmin = Math.max(Math.min(tmin[0], tmax[0]), Math.max(Math.min(tmin[1], tmax[1]), Math.min(tmin[2], tmax[2])));
		//double absmax = Math.min(Math.max(tmin[0], tmax[0]), Math.min(Math.max(tmin[1], tmax[1]), Math.max(tmin[2], tmax[2])));
		
		//TODO: Is this any faster?
		double[] amin = {tmin[0] < tmax[0] ? tmin[0] : tmax[0], 
						 tmin[1] < tmax[1] ? tmin[1] : tmax[1], 
						 tmin[2] < tmax[2] ? tmin[2] : tmax[2]};
		
		double absmin = amin[0] > amin[1] ? amin[0] : amin[1];
		absmin = absmin > amin[2] ? absmin : amin[2];
		
		double[] amax = {tmin[0] > tmax[0] ? tmin[0] : tmax[0], 
				 		 tmin[1] > tmax[1] ? tmin[1] : tmax[1], 
				 		 tmin[2] > tmax[2] ? tmin[2] : tmax[2]};

		double absmax = amax[0] < amax[1] ? amax[0] : amax[1];
		absmax = absmax < amax[2] ? absmax : amax[2];
		
		
		double tend = data.getTEnd();
		double tstart = data.getTStart();
		
		if(absmin > absmax || absmin >= tend || absmax < tstart || absmax >= tend)
			return Double.POSITIVE_INFINITY;
		
		//double tstart = data.getTStart();
		
		//if(absmin >= tstart)
			return absmin;
		
		//if(absmax >= tstart && absmax < tend) 
		//	return absmax;
		
		//return Double.MAX_VALUE;
	}
	
	public Vector3 getMidpoint()
	{
		Vector3 mp = min.add(max).multiply(0.5);
		return mp;
	}
	
	public double getSurfaceArea()
	{
		double[] minm = min.getArray();
		double[] maxm = max.getArray();

		double x = maxm[0]-minm[0];
		double y = maxm[1]-minm[1];
		double z = maxm[2]-minm[2];
		
		return 2.0 * ( (x * y) + (x * z) + (y * z) );
	}
	
	
	/* *********************************************************************************************
	 * Sizing Methods
	 * *********************************************************************************************/
	public void fit(Vector3 vec)
	{
		this.min.minimizeM(vec);
		this.max.maximizeM(vec);
	}
	

	/* *********************************************************************************************
	 * Reset Method
	 * *********************************************************************************************/
	public void clear()
	{
		min.set(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		max.set(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		//max.set((-9999999.0), (-9999999.0), (-9999999.0), 0);
	}
	
}
