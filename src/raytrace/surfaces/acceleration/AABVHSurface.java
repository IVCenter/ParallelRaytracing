package raytrace.surfaces.acceleration;

import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.surfaces.CompositeSurface;

public class AABVHSurface extends CompositeSurface {
	
	/*
	 * An axis aligned bounding volume hierarchy
	 */
	/* *********************************************************************************************
	 * Instnace Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public AABVHSurface()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		if(children == null)
			return null;
		
		//TODO: Check against BB, if miss, return null
		
		return super.intersects(data);
	}

	@Override
	public void bake(BakeData data)
	{
		super.bake(data);
	}

	@Override
	public void updateBoundingBox()
	{
		// TODO Auto-generated method stub
		//Push child bounding boxes back into parent space
		//And then calculate a bounding box much like composite surface does
		
		//Or do the opposite (call super.updateBound, then transform the min/max)
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//

}
