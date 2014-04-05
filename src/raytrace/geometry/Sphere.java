package raytrace.geometry;

import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.surfaces.TerminalSurface;

public class Sphere extends TerminalSurface implements Positionable {
	
	/*
	 * A simple sphere class
	 */
	
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
		center = new Vector4(0,0,0,1);
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
	public IntersectionData intersects(RayData data)
	{
		Ray ray = data.getRay();
		double t0 = data.getTStart();
		double t1 = data.getTEnd();
		
		Vector4 e = ray.getOrigin();
		Vector4 d = ray.getDirection();
		
		//Precalc frequently used values/vectors
		Vector4 EminusC = e.subtract3(center);
		double DdotD = d.dot3(d);
		double DdotEminusC = d.dot3(EminusC);
		
		double discrim = Math.pow(DdotEminusC, 2.0) - DdotD * (EminusC.dot3(EminusC) - Math.pow(radius, 2.0));
		
		//If the discriminant is negative then the ray doesn't intersect in real space
		if(discrim < 0.0) {
			return null;
		}
		
		//Now that we know its >= 0, root it
		discrim = Math.pow(discrim, 0.5);
		
		//Get the negation of d
		Vector4 negD = d.multiply3(-1);
		double negDdotEminusC = negD.dot3(EminusC);
		
		//Get the time of intersection
		double t = (negDdotEminusC - discrim) / DdotD;
		//double tOut = (negDdotEminusC + discrim) / DdotD;//Time of second intersection, not used
		
		//Test if t is in the given time range
		if(t <= t0 || t > t1)
			return null;
			
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(ray.evaluateAtTime(t));
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(idata.getPoint().subtract3(center).normalize3());
		//idata.setSurface(this);
		idata.setMaterial(material);
		
		return idata;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
	}
	
	@Override
	/**
	 * 
	 */
	public void updateBoundingBox()
	{
		boundingBox.clear();
		boundingBox.min.set(center.subtract3(radius));
		boundingBox.max.set(center.add3(radius));
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
