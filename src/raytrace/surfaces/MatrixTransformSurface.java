package raytrace.surfaces;

import math.Matrix4;
import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;

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
		transform.identity();
		
		inverseTransform = new Matrix4();
		inverseTransform.identity();
	}
	
	public MatrixTransformSurface(Matrix4 transform)
	{
		this.transform = transform;
		inverseTransform = transform.inverse();
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		if(children == null)
			return null;
		
		//TODO: Make sure this actually works...
		//TODO: Check against bounding box first
		
		Ray oldRay = data.getRay();
		
		Vector4 transOrigin = inverseTransform.multiplyPt(oldRay.getOrigin());
		Vector4 transDirection = inverseTransform.multiply3(oldRay.getDirection());//.normalize3();
		
		Ray newRay = new Ray(transOrigin, transDirection, oldRay.getPixelX(), oldRay.getPixelY());
		
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
		
		//Transform back into parent coords
		if(closest != null)
		{
			closest.setRay(oldRay);
			closest.setNormal(transform.multiply3(closest.getNormal()).normalize3());
			closest.setPoint(transform.multiplyPt(closest.getPoint()));
			closest.setDistance(closest.getPoint().subtract3(oldRay.getOrigin()).magnitude3());
		}
		
		return closest;
	}

	@Override
	public void bake(BakeData data)
	{
		//TODO:Consider using a mutation flag in Matrix4 to reduce the number of inverse() calls
		inverseTransform = transform.inverse();
		
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
	public Matrix4 getTransform() {
		return transform;
	}

	public void setTransform(Matrix4 transform)
	{
		this.transform = transform;
		this.inverseTransform = transform.inverse();
	}

}
