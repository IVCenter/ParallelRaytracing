package raytrace.data;

import math.ray.Ray;
import raytrace.color.Color;
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
	protected RayData rayData;

	protected int shadingRecursionDepth;
	protected int actualRecursionDepth;
	
	protected double refractiveIndex;
	
	protected Color recursiveSample;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ShadingData()
	{
		shadingRecursionDepth = 0;
		actualRecursionDepth = 0;
		refractiveIndex = Material.AIR_REFRACTIVE_INDEX;
		recursiveSample = Color.black();
	}
	

	/* *********************************************************************************************
	 * Clear Methods
	 * *********************************************************************************************/
	public void clear()
	{
		shadingRecursionDepth = 0;
		actualRecursionDepth = 0;	
		refractiveIndex = Material.AIR_REFRACTIVE_INDEX;
		recursiveSample = Color.black();
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

	public RayData getRayData() {
		return rayData;
	}

	public void setRayData(RayData rayData) {
		this.rayData = rayData;
	}

	public Ray getRay() {
		return rayData.getRay();
	}

	public void setRay(Ray ray) {
		this.rayData.setRay(ray);
	}

	public int getRecursionDepth() {
		return shadingRecursionDepth;
	}

	public void setRecursionDepth(int shadingRecursionDepth) {
		this.shadingRecursionDepth = shadingRecursionDepth;
	}

	public int getShadingRecursionDepth() {
		return shadingRecursionDepth;
	}

	public void setShadingRecursionDepth(int shadingRecursionDepth) {
		this.shadingRecursionDepth = shadingRecursionDepth;
	}

	public int getActualRecursionDepth() {
		return actualRecursionDepth;
	}

	public void setActualRecursionDepth(int actualRecursionDepth) {
		this.actualRecursionDepth = actualRecursionDepth;
	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	public void setRefractiveIndex(double refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}

}
