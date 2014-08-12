package tests;

import process.logging.Logger;
import math.Vector3;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class DiffuseGlassTestScene extends Scene
{	

	/*
	 * A scene for testing diffuse glass materials
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
		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0x5c66eeff), 3.0)),
				1
				);

		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(Configuration.getScreenWidth());
		Configuration.setRenderHeight(Configuration.getScreenHeight());
		
		//Camera
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(20);
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
			model.setMaterial(new DielectricPTMaterial(new Color(1000000.0, 1000000.0, 10000.0),  1.31, 0.0));
			
			this.addChild(model);
		}
		
		
		//Dragon 2
		Instance model2 = ResourceManager.create("dragon_smooth.obj");
		
		if(model2 != null)
		{
			model2.getTransform().scale(0.1);
			model2.getTransform().translate(0.0, 0.055, -0.1);
			model2.bake(null);
			model2.setMaterial(new DielectricPTMaterial(new Color(1000000.0, 1000000.0, 10000.0),  1.31, 0.12));
			
			this.addChild(model2);
		}
		

		//Dragon 3
		Instance model3 = ResourceManager.create("dragon_smooth.obj");
		
		if(model3 != null)
		{
			model3.getTransform().scale(0.1);
			model3.getTransform().translate(0.0, 0.055, -0.2);
			model3.bake(null);
			model3.setMaterial(new DielectricPTMaterial(new Color(1000000.0, 1000000.0, 10000.0),  1.31, 0.4));
			
			this.addChild(model3);
		}
		

		//Dragon 4
		Instance model4 = ResourceManager.create("dragon_smooth.obj");
		//Instance model4 = ResourceManager.create("bunny_n.obj");
		
		if(model4 != null)
		{
			model4.getTransform().scale(0.1);//was 0.1
			model4.getTransform().translate(0.0, 0.055, -0.3);//was -0.3 in z
			model4.bake(null);
			model4.setMaterial(new DielectricPTMaterial(new Color(1000000.0, 1000000.0, 10000.0),  1.31, 0.9));
			this.addChild(model4);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(new Color(1.0, 1.0, 0.9));
		directionalLight.setIntensity(1.0);
		directionalLight.setDirection(new Vector3(2, -3, -2));
		lightManager.addLight(directionalLight);

		
		//Sphere Light
		{
			Sphere sphere = new Sphere(2.0, new Vector3(-2.0, 1.5, -2.0));
			sphere.setMaterial(new ColorMaterial(new Color(5.0, 5.0, 5.0)));
			this.addChild(sphere);
		}
		
		
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