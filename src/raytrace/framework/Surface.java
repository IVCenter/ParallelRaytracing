package raytrace.framework;

import raytrace.data.IntersectionData;
import raytrace.data.TraceData;
import math.Ray;

public interface Surface {
	
	/*
	 * An interface for surfaces
	 */
	
	/**
	 * 
	 */
	public IntersectionData intersects(Ray ray);
	public IntersectionData intersects(Ray ray, double t0, double t1);
	
	/**
	 * 
	 */
	public TraceData trace(Ray ray);
	public TraceData trace(Ray ray, double t0, double t1);

}
