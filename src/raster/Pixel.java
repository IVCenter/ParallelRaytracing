package raster;

import math.Vector3;
import raytrace.color.Color;

public class Pixel {


	/*
	 * A storage structure for pixel data
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected Vector3 point;
	protected Vector3 normal;
	protected double depth;


	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public Pixel()
	{
		color = new Color();
		point = new Vector3();
		normal = new Vector3();
		depth = Double.POSITIVE_INFINITY;
	}

	
	/* *********************************************************************************************
	 * Copy method
	 * *********************************************************************************************/
	public void copy(Pixel p)
	{
		this.color.set(p.color);
		this.point.set(p.point);
		this.normal.set(p.normal);
		this.depth = p.depth;
	}
	
	
	/* *********************************************************************************************
	 * Getter/Setters
	 * *********************************************************************************************/
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector3 getPoint() {
		return point;
	}

	public void setPoint(Vector3 point) {
		this.point = point;
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}
	
}
