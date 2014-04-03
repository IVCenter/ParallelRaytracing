package raytrace.surfaces;

import math.Matrix4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.TraceData;

public class MatrixTransformSurface extends CompositeSurface {
	
	/*
	 * A simple matrix transform for transforming child surfaces
	 */

	/* *********************************************************************************************
	 * Instnace Vars
	 * *********************************************************************************************/
	protected Matrix4 transform;
	protected Matrix4 inverseTransform;

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		// TODO: intersect
		return null;
	}

	@Override
	public TraceData trace(RayData data)
	{
		// TODO: trace
		return null;
	}

	@Override
	public void bake(BakeData data)
	{
		// TODO: bake
	}

	@Override
	public void updateBoundingBox()
	{
		// TODO Auto-generated method stub
		//Push child bounding boxes back into parent space
		
		
	}

}
