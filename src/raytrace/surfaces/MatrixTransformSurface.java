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
		
		Ray ray = data.getRay();
		
		Vector4 oldOrigin = ray.getOrigin();
		Vector4 oldDirection = ray.getDirection();
		
		ray.setOrigin(inverseTransform.multiplyPt(oldOrigin));
		ray.setDirection(inverseTransform.multiply3(oldDirection));
		//Vector4 transOrigin = inverseTransform.multiplyPt(oldOrigin);
		//Vector4 transDirection = inverseTransform.multiply3(oldDirection);//.normalize3();
		
		//Ray newRay = new Ray(transOrigin, transDirection, oldRay.getPixelX(), oldRay.getPixelY());
		
		//data.setRay(newRay);
		
		IntersectionData idata;
		IntersectionData closest = null;
		for(CompositeSurface cs : this)
		{
			idata = cs.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getTime() < closest.getTime()))
			{
				closest = idata;
			}
		}
		
		//data.setRay(oldRay);

		ray.setOrigin(oldOrigin);
		ray.setDirection(oldDirection);
		
		//Transform back into parent coords
		if(closest != null)
		{
			closest.setRay(ray);
			closest.setNormal(transform.multiply3(closest.getNormal()).normalize3());
			closest.setPoint(transform.multiplyPt(closest.getPoint()));
			closest.setDistance(closest.getPoint().subtract3(ray.getOrigin()).magnitude3());
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
		if(!dynamic)
			return;
		
		// TODO Auto-generated method stub
		//Push child bounding boxes back into parent space
		//And then calculate a bounding box much like composite surface does
		
		//Or do the opposite (call super.updateBound, then transform the min/max)
		super.updateBoundingBox();
		
		//Create a vector for each corner, and transform it
		Vector4[] corners = new Vector4[8];
		for(int mask = 0x0; mask < 0x8; ++mask)
		{
			corners[mask] = transform.multiplyPt(new Vector4(
					(mask & 0x1) == 0 ? boundingBox.min.get(0) : boundingBox.max.get(0),
					(mask & 0x2) == 0 ? boundingBox.min.get(1) : boundingBox.max.get(1),
					(mask & 0x4) == 0 ? boundingBox.min.get(2) : boundingBox.max.get(2),
					1));
		}

		
		//Calculate the bounding box for the transformed corners
		boundingBox.clear();
		for(int i = 0; i < 8; ++i)
		{
			boundingBox.min.minimize3(corners[i]);
			boundingBox.max.maximize3(corners[i]);
		}
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
