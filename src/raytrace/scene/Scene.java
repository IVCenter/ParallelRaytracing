package raytrace.scene;

import java.util.Iterator;

import raytrace.camera.Camera;
import raytrace.data.BakeData;
import raytrace.light.Light;
import raytrace.light.LightManager;
import raytrace.material.Material;
import raytrace.surfaces.CompositeSurface;

public abstract class Scene extends CompositeSurface{
	
	/*
	 * A scene to be rendered
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Camera activeCamera;
	protected LightManager lightManager;
	protected Material skyMaterial;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Scene()
	{
		initialize();
	}
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	protected abstract void initialize();
	
	
	/* *********************************************************************************************
	 * Surface Overrides
	 * *********************************************************************************************/
	@Override
	public void bake(BakeData data) {
		// TODO Auto-generated method stub
		
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Camera getActiveCamera() {
		return activeCamera;
	}

	public void setActiveCamera(Camera activeCamera) {
		this.activeCamera = activeCamera;
	}

	public LightManager getLightManager() {
		return lightManager;
	}

	public void setLightManager(LightManager lightManager) {
		this.lightManager = lightManager;
	}
	
	public Iterator<Light> lights()
	{
		return lightManager.iterator();
	}

	public Material getSkyMaterial() {
		return skyMaterial;
	}

	public void setSkyMaterial(Material skyMaterial) {
		this.skyMaterial = skyMaterial;
	}
	

}
