package raytrace.geometry;

import math.Ray;
import math.Vector4;
import raytrace.data.IntersectionData;
import raytrace.data.TraceData;
import raytrace.framework.Positionable;
import raytrace.surfaces.CompositeSurface;

public class Sphere extends CompositeSurface implements Positionable {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double radius;
	protected Vector4 center;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Sphere()
	{
		radius = 1.0;
		center = new Vector4();
	}
	
	public Sphere(double radius, Vector4 center)
	{
		this.radius = radius;
		this.center = center;
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(Ray ray, double t0, double t1)
	{
		//TODO: Intersect
		Vector4 e = ray.getOrigin();
		Vector4 d = ray.getDirection();
		
		//Precalc frequently used values/vectors
		Vector4 EminusC = new Vector4(e.x - center.x, e.y - center.y, e.z - center.z, 0);
		double DdotD = d.dot(d);
		double DdotEminusC = d.dot(EminusC);
		
		double discrim = Math.pow(DdotEminusC, 2.0) - DdotD * (EminusC.dot(EminusC) - Math.pow(radius, 2.0));
		
		//If the discriminant is negative then the ray doesn't intersect in real space
		if(discrim < 0.0) {
			return null;
		}
		
		//Now that we know its >= 0, root it
		discrim = Math.pow(discrim, 0.5);
		
		//Get the negation of d
		Vector4 negD = d.multiply3(-1);
		double negDdotEminusC = negD.dot(EminusC);
		
		//Get the time of intersection
		double t = (negDdotEminusC - discrim) / DdotD;
		//double tOut = (negDdotEminusC + discrim) / DdotD;//Time of second intersection, not used
		
		//Test if t is in the given time range
		if(t <= t0 || t > t1)
			return null;
			
		//Return data about the intersection
		IntersectionData data = new IntersectionData();
		data.setTime(t);
		data.setRay(ray);
		data.setPoint(ray.evaluateAtTime(t));
		data.setDistance(ray.getDirection().magnitude3() * t);
		
		return data;
	}
	
	/**
	 * 
	 */
	public TraceData trace(Ray ray, double t0, double t1)
	{
		//TODO: Trace
		return null;
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	public Vector4 getPosition()
	{
		return center;
	}

	@Override
	public void setPosition(Vector4 position)
	{
		center = position;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	

}
