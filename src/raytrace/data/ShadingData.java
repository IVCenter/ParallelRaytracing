package raytrace.data;

import math.Ray;
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
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ShadingData()
	{
		
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

}
