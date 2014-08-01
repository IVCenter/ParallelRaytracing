package tests;

import process.logging.Logger;
import math.Vector3;
import raytrace.camera.ProgrammableCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class CSE168_Project2_Scene extends Scene
{	

	/*
	 * A scene for project 2 of CSE 168
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Sky Material
		skyMaterial = new ColorMaterial(new Color(0.8, 0.8, 1.0));
		
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(false);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(1);
		activeCamera.setPosition(new Vector3(-0.1, 0.1, 0.2));
		activeCamera.setViewingDirection(new Vector3(0.05, 0.02, -0.2));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
	
		
		//Ground
		MeshSurface ground = new Cube(5.0, 0.1, 5.0);
		ground.setMaterial(new DiffuseMaterial(new Color(0xffffffff)));
		this.addChild(ground);
		
		
		//Dragon 1
		Instance model = ResourceManager.create("dragon_smooth.obj");
		
		if(model != null)
		{
			model.getTransform().scale(0.1);
			model.getTransform().translate(0, 0.05, 0);
			model.bake(null);
			model.setMaterial(new DiffuseMaterial(new Color(0xffffffff)));
			this.addChild(model);
		}
		
		
		//Dragon 2
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{
			model2.getTransform().scale(0.1);
			model2.getTransform().translate(-0.05, 0.05, -0.1);
			model2.getTransform().rotateY(Math.PI);
			model2.bake(null);
			model2.setMaterial(new DiffuseMaterial(new Color(0xffffffff)));
			this.addChild(model2);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector3(2, -3, -2));
		lightManager.addLight(directionalLight);
		

		//Red Light
		PointLight redlgt = new PointLight();
		redlgt.setColor(new Color(1.0, 0.2, 0.2));
		redlgt.setIntensity(0.02);
		redlgt.setConstantAttenuation(0.0);
		redlgt.setLinearAttenuation(0.0);
		redlgt.setQuadraticAttenuation(1.0);
		redlgt.setPosition(new Vector3(-0.2, 0.2, 0.2));
		lightManager.addLight(redlgt);
		

		//Blue Light
		PointLight bluelgt = new PointLight();
		bluelgt.setColor(new Color(0.2, 0.2, 1.0));
		bluelgt.setIntensity(0.02);
		bluelgt.setConstantAttenuation(0.0);
		bluelgt.setLinearAttenuation(0.0);
		bluelgt.setQuadraticAttenuation(1.0);
		bluelgt.setPosition(new Vector3(0.1, 0.1, 0.3));
		lightManager.addLight(bluelgt);
		
		
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
		elapsed = Math.PI/4.0;
		
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