package raytrace.data;

import system.Constants;
import math.Vector3;
import math.ray.Ray;

public class RayData {
	
	/*
	 * Used for passing ray casting data down the recursive call chain
	 */
	
	/* *********************************************************************************************
	 * Enums
	 * *********************************************************************************************/
	public enum Type {REFLECT, TRASMIT, NONE};
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Ray ray;
	protected Type type = Type.NONE;
	protected int subType = -1;

	protected double t0;
	protected double t1;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public RayData()
	{
		t0 = 0;
		t1 = Double.POSITIVE_INFINITY;
	}
	
	public RayData(Ray ray)
	{
		this();
		this.ray = ray;
	}
	
	public RayData(Ray ray, double t0, double t1)
	{
		this();
		this.t0 = t0;
		this.t1 = t1;
		this.ray = ray;
	}
	
	public RayData(Vector3 origin, Vector3 direction, Type type)
	{
		this();
		this.t0 = Constants.RECURSIVE_EPSILON;
		this.ray = new Ray(origin, direction, 0, 0);
		this.type = type;
	}
	
	public RayData(Vector3 origin, Vector3 direction, Type type, int subType)
	{
		this(origin, direction, type);
		this.subType = subType;
	}
	

	/* *********************************************************************************************
	 * Clear Methods
	 * *********************************************************************************************/
	public void clear()
	{
		t0 = 0;
		t1 = Double.POSITIVE_INFINITY;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}
	
	public double getTStart() {
		return t0;
	}

	public void setTStart(double t0) {
		this.t0 = t0;
	}

	public double getTEnd() {
		return t1;
	}

	public void setTEnd(double t1) {
		this.t1 = t1;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getSubType() {
		return subType;
	}

	public void setSubType(int subType) {
		this.subType = subType;
	}
}
