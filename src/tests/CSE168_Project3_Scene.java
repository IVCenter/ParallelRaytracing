package tests;

import process.logging.Logger;
import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
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

		Configuration.setScreenWidth(800);
		Configuration.setScreenHeight(600);
		
		//Camera
		activeCamera = new PinholeCamera();
		((PinholeCamera)activeCamera).setStratifiedSampling(true);
		((PinholeCamera)activeCamera).setSuperSamplingLevel(4);
		activeCamera.setPosition(new Vector4(-0.5, 0.25, -0.2, 0));
		activeCamera.setViewingDirection(new Vector4(0.5, -0.1, 0.05, 0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).forceUpdate();
		((PinholeCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		((PinholeCamera)activeCamera).forceUpdate();
	
		
		//Ground
		MeshSurface ground = new Cube(2.0, 0.11, 2.0);
		ground.setMaterial(new DiffusePTMaterial(new Color(0.3, 0.3, 0.35), 1));
		this.addChild(ground);
		
		
		//Dragon 1
		Instance model = ResourceManager.create("dragon_smooth.obj");
		
		if(model != null)
		{
			model.getTransform().scale(0.1);
			model.getTransform().translation(0.0, 0.055, 0.0);
			model.bake(null);
			model.setMaterial(new AshikhminPTMaterial(Color.grey(0.7), Color.black(), 0.0,
					1.0, 0, 0, 1));
			//model.setMaterial(new FresnelDiffusePTMaterial(Color.grey(0.7), 0.1, 1.5, 1));
			this.addChild(model);
		}
		
		
		//Dragon 2
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{
			model2.getTransform().scale(0.1);
			model2.getTransform().translation(0.0, 0.055, -0.1);
			model2.bake(null);
			model2.setMaterial(new AshikhminPTMaterial(Color.grey(0.0), new Color(0.9, 0.6, 0.5), 1.0,
					0.0, 100, 100, 1));
			this.addChild(model2);
		}
		

		//Dragon 3
		Instance model3 = ResourceManager.create("dragon_smooth.obj");
		
		if(model3 != null)
		{
			model3.getTransform().scale(0.1);
			model3.getTransform().translation(0.0, 0.055, -0.2);
			model3.bake(null);
			model3.setMaterial(new AshikhminPTMaterial(Color.grey(0.0), new Color(0.95, 0.7, 0.3), 1.0,
					0.0, 1, 1000, 1));
			this.addChild(model3);
		}
		

		//Dragon 4
		Instance model4 = ResourceManager.create("dragon_smooth.obj");
		
		if(model4 != null)
		{
			model4.getTransform().scale(0.1);
			model4.getTransform().translation(0.0, 0.055, -0.3);
			model4.bake(null);
			model4.setMaterial(new AshikhminPTMaterial(new Color(1.0, 0.1, 0.1), new Color(1.0, 1.0, 1.0), 0.20,
					0.80, 1000, 1000, 1));
			this.addChild(model4);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector4(2, -3, -2, 0));
		lightManager.addLight(directionalLight);
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		long startTime = System.currentTimeMillis();
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(this.getChildren());
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