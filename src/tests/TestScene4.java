package tests;

import process.logging.Logger;
import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import resource.ResourceManager;
import system.Configuration;

public class TestScene4 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{

		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
		//super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		activeCamera = new PinholeCamera();
		((PinholeCamera)activeCamera).setStratifiedSampling(true);
		((PinholeCamera)activeCamera).setSuperSamplingLevel(1);
		((PinholeCamera)activeCamera).setUseRayCaching(false);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).forceUpdate();
	
		
		
		
		//TODO: Load model here
		Instance model = ResourceManager.create("ia.obj");
		//Instance model = ResourceManager.create("miku_v2.obj");
		
		if(model != null) {
			model.getTransform().scale(0.2);//ia
			//model.getTransform().scale(1.2);//miku
			model.getTransform().translation(0, 0, 2);
			model.bake(null);
			model.setMaterial(new DiffuseMaterial(new Color(0x999999ff)));
			this.addChild(model);
		}else{
			Logger.error(-13, "TestScene4: Model was null!");
		}
		
		
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		
		//Add a plane to the scene
		Plane plane = new Plane();
		plane.setMaterial(new ReflectiveMaterial(new Color(0xffeeddff), 0.35));
		this.addChild(plane);
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