package raytrace.data;

import math.Ray;
import math.Vector4;

public class IntersectionData {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double time;
	protected double distance;
	protected Vector4 point;
	protected Ray ray;

	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public IntersectionData()
	{
		//
	}

	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public Vector4 getPoint() {
		return point;
	}

	public void setPoint(Vector4 point) {
		this.point = point;
	}

	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}
}
