package raytrace.geometry.pointclouds;

import raytrace.geometry.Sphere;
import raytrace.geometry.Vertex;

public class PointSurface extends Sphere {
	
	/*
	 * A geometric representation of a point
	 */
	/* *********************************************************************************************
	 * Instance Variables
	 * *********************************************************************************************/
	protected Vertex point;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PointSurface()
	{
		point = new Vertex();
		this.center = point.getPosition();
	}
	
	public PointSurface(Vertex point)
	{
		this.point = point;
		this.center = point.getPosition();
	}
	
	public PointSurface(Vertex point, double radius)
	{
		this.point = point;
		this.center = point.getPosition();
		this.radius = radius;
	}

	public Vertex getPoint()
	{
		return point;
	}

	public void setPoint(Vertex point)
	{
		this.point = point;
	}

}
