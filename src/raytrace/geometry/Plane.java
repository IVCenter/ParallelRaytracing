package raytrace.geometry;

import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.surfaces.TerminalSurface;

public class Plane extends TerminalSurface implements Positionable {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 normal;
	protected Vector4 position;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Plane()
	{
		normal = new Vector4(0,1,0,0);
		position = new Vector4(0,0,0,1);
	}
	
	public Plane(Vector4 normal, Vector4 center)
	{
		this.normal = normal;
		this.position = center;
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data)
	{
		Ray ray = data.getRay();
		double t0 = data.getTStart();
		double t1 = data.getTEnd();
		
		Vector4 o = ray.getOrigin();
		Vector4 d = ray.getDirection();
		
		//Precalc quotient terms
		double DdotN = d.dot3(normal);
		
		//If D is orthogonal to N, then it is parallel to the plane and will never intersect
		if(DdotN == 0.0)
			return null;
		
		//Calclate the rest of thw quatient terms
		double PdotN = position.dot3(normal);
		double OdotN = o.dot3(normal);
		
		double t = (PdotN - OdotN) / DdotN;
		
		//Test if t is in the given time range
		if(t <= t0 || t > t1)
			return null;
		
		//Point
		Vector4 point = ray.evaluateAtTime(t);
		
		//Calculate texcoords
		//TODO: This will cause NaNs at exactly when the normal faces towards the z-axis or the negative z-axis
		Vector4 tangent = normal.cross3(Vector4.ZAXIS).normalize3();
		Vector4 pointRelativeToPosition = point.subtract3(position);
		
		double relativePointDistanceSqrd = pointRelativeToPosition.magnitude3Sqrd();
		double uCoord = tangent.dot3(pointRelativeToPosition);
		double vCoord = Math.sqrt(relativePointDistanceSqrd - uCoord * uCoord);
		
			
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(new Vector4(normal));
		idata.setTwoSided(true);
		idata.setMaterial(material);

		idata.setSurface(this);
		idata.setTexcoord(new Vector4(uCoord, vCoord, 0, 0));
		idata.setLocalPoint(new Vector4(point));
		
		return idata;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 getPosition()
	{
		return position;
	}

	@Override
	public void setPosition(Vector4 position)
	{
		this.position = position;
	}

	public Vector4 getNormal() {
		return normal;
	}

	public void setNormal(Vector4 normal) {
		this.normal = normal;
	}
}
	
