package raytrace.data;

import raytrace.surfaces.CompositeSurface;
import math.ray.Ray;

public class RayData {
	
	/*
	 * Used for passing ray casting data down the recursive call chain
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Ray ray;
	protected CompositeSurface rootSurface;
	//protected IntersectionData idata;

	protected double t0;
	protected double t1;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public RayData()
	{
		t0 = 0;
		t1 = Double.MAX_VALUE;
	}
	
	public RayData(Ray ray, CompositeSurface rootSurface/*, IntersectionData idata*/)
	{
		this();
		this.ray = ray;
		this.rootSurface = rootSurface;
		//this.idata = idata;
	}
	
	public RayData(Ray ray, CompositeSurface rootSurface/*, IntersectionData idata*/, double t0, double t1)
	{
		this();
		this.t0 = t0;
		this.t1 = t1;
		this.ray = ray;
		this.rootSurface = rootSurface;
		//this.idata = idata;
	}
	

	/* *********************************************************************************************
	 * Clear Methods
	 * *********************************************************************************************/
	public void clear()
	{
		t0 = 0;
		t1 = Double.MAX_VALUE;
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

	public CompositeSurface getRootSurface() {
		return rootSurface;
	}

	public void setRootSurface(CompositeSurface rootSurface) {
		this.rootSurface = rootSurface;
	}
	/*
	public IntersectionData getIdata() {
		return idata;
	}

	public void setIdata(IntersectionData idata) {
		this.idata = idata;
	}
	 */
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
}
