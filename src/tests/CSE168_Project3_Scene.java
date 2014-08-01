package tests;

import process.logging.Logger;
import math.Vector3;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class CSE168_Project3_Scene extends Scene
{	

	/*
	 * A scene for project 3 of CSE 168
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
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(6);
		activeCamera.setPosition(new Vector3(-0.5, 0.25, -0.2));
		activeCamera.setViewingDirection(new Vector3(0.5, -0.1, 0.05));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.00005, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(0.50);
	
		
		//Ground
		MeshSurface ground = new Cube(2.0, 0.11, 2.0);
		ground.setMaterial(new DiffusePTMaterial(new Color(0.3, 0.3, 0.35)));
		this.addChild(ground);
		
		
		//Dragon 1
		Instance model = ResourceManager.create("dragon_smooth.obj");
		
		if(model != null)
		{
			model.getTransform().scale(0.1);
			model.getTransform().translate(0.0, 0.055, 0.0);
			model.bake(null);
			model.setMaterial(new AshikhminPTMaterial(Color.gray(0.7), Color.black(), 0.0,
					1.0, 0, 0));
			
			this.addChild(model);
		}
		
		
		//Dragon 2
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{
			model2.getTransform().scale(0.1);
			model2.getTransform().translate(0.0, 0.055, -0.1);
			model2.bake(null);
			model2.setMaterial(new AshikhminPTMaterial(Color.gray(0.0), new Color(0.9, 0.6, 0.5), 1.0,
					0.0, 100, 100));

			this.addChild(model2);
		}
		

		//Dragon 3
		Instance model3 = ResourceManager.create("dragon_smooth.obj");
		
		if(model3 != null)
		{
			model3.getTransform().scale(0.1);
			model3.getTransform().translate(0.0, 0.055, -0.2);
			model3.bake(null);
			model3.setMaterial(new AshikhminPTMaterial(Color.gray(0.0), new Color(0.95, 0.7, 0.3), 1.0,
					0.0, 1, 1000));

			this.addChild(model3);
		}
		

		//Dragon 4
		Instance model4 = ResourceManager.create("dragon_smooth.obj");
		
		if(model4 != null)
		{
			model4.getTransform().scale(0.1);//was 0.1
			model4.getTransform().translate(0.0, 0.055, -0.3);//was -0.3 in z
			model4.bake(null);
			model4.setMaterial(new AshikhminPTMaterial(new Color(1.0, 0.1, 0.1), new Color(1.0, 1.0, 1.0), 0.20,
					0.80, 1000, 1000));
			
			this.addChild(model4);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector3(2, -3, -2));
		lightManager.addLight(directionalLight);
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		long startTime = System.currentTimeMillis();
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(this.getChildren(), 1, 2);
		this.getChildren().clear();
		this.addChild(aabvh);
		
		//Refresh
		this.updateBoundingBox();
		
		Logger.progress(-1, "Ending AABVH creation... (" + (System.currentTimeMillis() - startTime) + "ms).");
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
		this.updateBoundingBox();
		super.bake(data);
	}
}