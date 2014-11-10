package raytrace.surfaces;

import math.Matrix4;
import math.Vector3;
import math.ray.Ray;
import raytrace.bounding.BoundingBox;
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
		
		
		Ray ray = data.getRay();
		
		Vector3 oldOrigin = ray.getOrigin();
		Vector3 oldDirection = ray.getDirection();
		
		ray.setOrigin(inverseTransform.multiplyPt(oldOrigin));
		ray.setDirection(inverseTransform.multiply3(oldDirection).normalizeM());
		
		IntersectionData idata;
		IntersectionData closest = null;
		for(AbstractSurface cs : this)
		{
			idata = cs.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getTime() < closest.getTime()))
			{
				closest = idata;
			}
		}

		ray.setOrigin(oldOrigin);
		ray.setDirection(oldDirection);
		
		//Transform back into parent coords
		if(closest != null)
		{
			closest.setRay(ray);
			closest.setNormal(transform.multiply3(closest.getNormal()).normalizeM());
			closest.setPoint(transform.multiplyPt(closest.getPoint()));
			closest.setDistance(closest.getPoint().subtract(ray.getOrigin()).magnitude());
			closest.setTime(closest.getDistance());
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
	public BoundingBox getBoundingBox()
	{
		//Create a vector for each corner, and transform it
		BoundingBox boundingBox = super.getBoundingBox();
		
		Vector3[] corners = new Vector3[8];
		for(int mask = 0x0; mask < 0x8; ++mask)
		{
			corners[mask] = transform.multiplyPt(new Vector3(
					(mask & 0x1) == 0 ? boundingBox.min.get(0) : boundingBox.max.get(0),
					(mask & 0x2) == 0 ? boundingBox.min.get(1) : boundingBox.max.get(1),
					(mask & 0x4) == 0 ? boundingBox.min.get(2) : boundingBox.max.get(2)));
		}

		
		//Calculate the bounding box for the transformed corners
		boundingBox.clear();
		for(int i = 0; i < 8; ++i)
		{
			boundingBox.min.minimizeM(corners[i]);
			boundingBox.max.maximizeM(corners[i]);
		}
		
		return boundingBox;
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
