package raytrace.scene;

import raytrace.camera.Camera;
import raytrace.data.BakeData;
import raytrace.surfaces.CompositeSurface;

public abstract class Scene extends CompositeSurface{
	
	/*
	 * A scene to be rendered
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Camera activeCamera;
	

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

}