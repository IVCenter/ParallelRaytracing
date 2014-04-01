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
		return null;
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
