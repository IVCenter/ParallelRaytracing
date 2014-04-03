package raytrace.surfaces;

import math.Matrix4;
import math.Ray;
import math.Vector4;
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
	public MatrixTransformSurface()
	{
		transform = new Matrix4();
		transform.inverse();
		
		inverseTransform = new Matrix4();
		inverseTransform.identity();
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		//TODO: Check against bounding box first
		
		Ray oldRay = data.getRay();
		
		Vector4 transOrigin = inverseTransform.multiplyPt(oldRay.getOrigin());
		Vector4 transDirection = inverseTransform.multiply3(oldRay.getDirection());
		
		Ray newRay = new Ray(transOrigin, transDirection);
		
		data.setRay(newRay);
		
		IntersectionData idata;
		IntersectionData closest = null;
		for(CompositeSurface cs : this)
		{
			idata = cs.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getDistance() < closest.getDistance()))
			{
				closest = idata;
			}
		}
		
		data.setRay(oldRay);
		
		//Translate back into parent coords
		if(closest != null)
		{
			closest.setRay(oldRay);
			closest.setNormal(transform.multiply3(closest.getNormal()));
			closest.setPoint(transform.multiplyPt(closest.getPoint()));
			closest.setDistance(closest.getPoint().subtract3(oldRay.getOrigin()).magnitude3());
		}
		
		return closest;
	}

	@Override
	public TraceData trace(RayData data)
	{
		/*
		 * Material class as a programmable shader?
		 * shade method
		 * Pass in ShadingData (which includes IntersectionData)
		 */
		/*
		 * Check if Ray intersects with this
		 */
		
		// TODO: trace
		return null;
	}

	@Override
	public void bake(BakeData data)
	{
		//TODO:Consider using a mutation flag in Matrix4 to reduce the number of inverse() calls
		inverseTransform = transform.inverse();
	}

	@Override
	public void updateBoundingBox()
	{
		// TODO Auto-generated method stub
		//Push child bounding boxes back into parent space
		
		
	}

}
