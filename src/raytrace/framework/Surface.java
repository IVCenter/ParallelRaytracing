package raytrace.framework;

import raytrace.data.IntersectionData;
import math.Ray;

public interface Surface {
	
	/*
	 * An interface for surfaces
	 */
	
	/**
	 * 
	 */
	public IntersectionData intersects(Ray ray);
	
	/**
	 * 
	 */

}
