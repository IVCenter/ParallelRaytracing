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
	protected Vector4 normal;
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
	//Time
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	//Distance
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	//Point
	public Vector4 getPoint() {
		return point;
	}

	public void setPoint(Vector4 point) {
		this.point = point;
	}

	//Normal
	public Vector4 getNormal() {
		return normal;
	}

	public void setNormal(Vector4 normal) {
		this.normal = normal;
	}

	//Ray
	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}
}
