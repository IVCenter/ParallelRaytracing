package raytrace.framework;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.TraceData;
import raytrace.data.UpdateData;

public interface Surface {
	
	/*
	 * An interface for surfaces
	 */
	
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data);
	
	//No longer supported
	/**
	 * 
	 *
	public TraceData trace(RayData data);
	*/
	
	/**
	 * 
	 */
	public void bake(BakeData data);
	
	/**
	 * 
	 */
	public void update(UpdateData data);
	
	/**
	 * 
	 */
	public void updateBoundingBox();
	
	/**
	 * 
	 */
	public BoundingBox getBoundingBox();

}
