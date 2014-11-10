package tests;

import math.Vector3;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import resource.ResourceManager;
import system.Configuration;

public class PerformanceTest1 extends Scene
{	

	/*
	 * A scene for performance testing
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Sky Material
		skyMaterial = new ColorMaterial(new Color(0.8, 0.9, 1.0));

		Configuration.setScreenWidth(800);
		Configuration.setScreenHeight(600);
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(1);
		activeCamera.setPosition(new Vector3(-0.5, 0.25, -0.2));
		activeCamera.setViewingDirection(new Vector3(0.5, -0.1, 0.05));
		//activeCamera.setPosition(new Vector4(-0.2, 0.077, 0.1, 0));
		//activeCamera.setViewingDirection(new Vector4(0.65, 0.3, -1.0, 0));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.00005, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(0.50);
	
		
		

		//Dragon
		Instance model = ResourceManager.create("dragon_smooth.obj");
		
		if(model != null)
		{
			model.getTransform().scale(0.15);
			model.getTransform().translate(0.1, 0.005, -0.0);
			model.bake(null);
			//model4.setMaterial(new AshikhminPTMaterial(new Color(1.0, 0.1, 0.1), new Color(1.0, 1.0, 1.0), 0.20,
			//		0.80, 1000, 1000));
			model.setMaterial(new DiffuseMaterial(Color.gray(0.8)));
			this.addChild(model);
		}
		

		//Dragon
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{
			model2.getTransform().scale(0.15);
			model2.getTransform().translate(0.1, 0.005, -0.4);
			model2.bake(null);
			//model4.setMaterial(new AshikhminPTMaterial(new Color(1.0, 0.1, 0.1), new Color(1.0, 1.0, 1.0), 0.20,
			//		0.80, 1000, 1000));
			model2.setMaterial(new DiffuseMaterial(Color.gray(0.8)));
			//this.addChild(model2);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector3(2, -3, -2));
		lightManager.addLight(directionalLight);
		
		
		
		/*
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		long startTime = System.currentTimeMillis();
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(this.getChildren(), 1, 1);
		this.getChildren().clear();
		this.addChild(aabvh);
		
		//Refresh
		this.updateBoundingBox();
		
		Logger.progress(-1, "Ending AABVH creation... (" + (System.currentTimeMillis() - startTime) + "ms).");
		*/

		//Ground
		Plane ground = new Plane();
		ground.setMaterial(new DiffuseMaterial(new Color(0.3, 0.3, 0.35)));
		this.addChild(ground);
		
	}
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		super.bake(data);
	}
}