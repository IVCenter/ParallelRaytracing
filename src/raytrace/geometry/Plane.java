package raytrace.geometry;

import math.Vector3;
import math.ray.Ray;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.surfaces.TerminalSurface;

public class Plane extends TerminalSurface implements Positionable {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 normal;
	protected Vector3 position;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Plane()
	{
		normal = new Vector3(0, 1, 0);
		position = new Vector3(0, 0, 0);
	}
	
	public Plane(Vector3 normal, Vector3 center)
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
		
		Vector3 o = ray.getOrigin();
		Vector3 d = ray.getDirection();
		
		//Precalc quotient terms
		double DdotN = d.dot(normal);
		
		//If D is orthogonal to N, then it is parallel to the plane and will never intersect
		if(DdotN == 0.0)
			return null;
		
		//Calclate the rest of thw quatient terms
		double PdotN = position.dot(normal);
		double OdotN = o.dot(normal);
		
		double t = (PdotN - OdotN) / DdotN;
		
		//Test if t is in the given time range
		if(t <= t0 || t > t1)
			return null;
		
		//Point
		Vector3 point = ray.evaluateAtTime(t);
		
		//Calculate texcoords
		//TODO: This will cause NaNs at exactly when the normal faces towards the z-axis or the negative z-axis
		Vector3 tangent = normal.cross(Vector3.positiveZAxis).normalizeM();
		Vector3 pointRelativeToPosition = point.subtract(position);
		
		double relativePointDistanceSqrd = pointRelativeToPosition.magnitudeSqrd();
		double uCoord = tangent.dot(pointRelativeToPosition);
		double vCoord = Math.sqrt(relativePointDistanceSqrd - uCoord * uCoord);
		
			
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude() * t);
		idata.setNormal(new Vector3(normal));
		idata.setTwoSided(true);
		idata.setMaterial(material);

		idata.setSurface(this);
		idata.setTexcoord(new Vector3(uCoord, vCoord, 0));
		idata.setLocalPoint(new Vector3(point));
		idata.setSurfaceID(surfaceID);
		
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
	public Vector3 getPosition()
	{
		return position;
	}

	@Override
	public void setPosition(Vector3 position)
	{
		this.position = position;
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}
}
	
