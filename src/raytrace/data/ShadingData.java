package raytrace.data;

import math.ray.Ray;
import raytrace.material.Material;
import raytrace.scene.Scene;

public class ShadingData{
	
	/*
	 * A storage class for shading data used by Material.shade()
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Scene rootScene;
	protected IntersectionData intersectionData;
	protected Ray ray;
	
	protected int recursionDepth;
	
	protected double refractiveIndex;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ShadingData()
	{
		recursionDepth = 0;
		refractiveIndex = Material.AIR_REFRACTIVE_INDEX;
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Scene getRootScene() {
		return rootScene;
	}

	public void setRootScene(Scene rootScene) {
		this.rootScene = rootScene;
	}

	public IntersectionData getIntersectionData() {
		return intersectionData;
	}

	public void setIntersectionData(IntersectionData intersectionData) {
		this.intersectionData = intersectionData;
	}

	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}

	public int getRecursionDepth() {
		return recursionDepth;
	}

	public void setRecursionDepth(int recursionDepth) {
		this.recursionDepth = recursionDepth;
	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	public void setRefractiveIndex(double refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}

}
