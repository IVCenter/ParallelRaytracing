package raytrace.framework;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.TraceData;

public interface Surface {
	
	/*
	 * An interface for surfaces
	 */
	
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data);
	
	/**
	 * 
	 */
	public TraceData trace(RayData data);
	
	/**
	 * 
	 */
	public void bake(BakeData data);
	
	/**
	 * 
	 */
	public void updateBoundingBox();
	
	/**
	 * 
	 */
	public BoundingBox getBoundingBox();

}
