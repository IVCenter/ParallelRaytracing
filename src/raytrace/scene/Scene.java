package raytrace.scene;

import raytrace.camera.Camera;
import raytrace.data.BakeData;
import raytrace.surfaces.CompositeSurface;

public class Scene extends CompositeSurface{
	
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
		//Empty Scene
	}
	
	
	


	/* *********************************************************************************************
	 * Surface Overrides
	 * *********************************************************************************************/
	@Override
	public void bake(BakeData data) {
		// TODO Auto-generated method stub
		
	}

}
