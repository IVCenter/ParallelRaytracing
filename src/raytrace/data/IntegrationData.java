package raytrace.data;

import math.Vector3;
import raytrace.color.Color;

public class IntegrationData {
	
	/*
	 * A storage class for the results of Integrator.integrate()
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected Vector3 normal;
	protected Vector3 point;
	protected double distance;
	protected boolean didIntersect;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public IntegrationData()
	{
		color = Color.black();
		normal = new Vector3();
		point = new Vector3();
		distance = 0.0;
		didIntersect = false;
	}


	/* *********************************************************************************************
	 * Copy Method
	 * *********************************************************************************************/
	public void copy(IntegrationData idata)
	{
		this.color.set(idata.color);
		this.normal.set(idata.normal);
		this.point.set(idata.point);
		this.distance = idata.distance;
		this.didIntersect = idata.didIntersect;
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public Vector3 getPoint() {
		return point;
	}

	public void setPoint(Vector3 point) {
		this.point = point;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean didIntersect() {
		return didIntersect;
	}

	public void setDidIntersect(boolean didIntersect) {
		this.didIntersect = didIntersect;
	}

}
