package tests;

import java.util.ArrayList;

import process.logging.Logger;
import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
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

		//skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		skyMaterial = new ColorMaterial(new Color(0xcce2ffff));
		
		//super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		activeCamera = new PinholeCamera();
		((PinholeCamera)activeCamera).setStratifiedSampling(true);
		((PinholeCamera)activeCamera).setSuperSamplingLevel(3);
		((PinholeCamera)activeCamera).setUseRayCaching(false);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).forceUpdate();
	
		

		ArrayList<CompositeSurface> spheres = new ArrayList<CompositeSurface>(64);
		for(int i = 0; i < 64; i++)
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(new DielectricMaterial(Color.random(0.7 + (Math.random()/16.0)), randInRange(1.01, 2.0)));
			sphere.setPosition(new Vector4(4 * Math.random() - 2.0, 3.2 * Math.random() - 0.4, 4 * Math.random() - 2.0, 0));
			sphere.setRadius(Math.pow(Math.random() * 0.1, 1.15));
			spheres.add(sphere);
		}
		
		AABVHSurface sphereSurface = AABVHSurface.makeAABVH(spheres);
		this.addChild(sphereSurface);
		
		
		
		Instance model = ResourceManager.create("ia.obj");
		
		if(model != null) {
			model.getTransform().scale(0.18);//ia
			model.getTransform().translation(-1.2, 0, 1.2);//IA
			model.getTransform().rotateY(0.15);
			model.bake(null);
			//model.setMaterial(new ReflectiveMaterial(new Color(0xff0068ff), .70));
			model.setMaterial(new DielectricMaterial(new Color(1.9, 1.9, 1.2), 1.45));
			this.addChild(model);
		}else{
			Logger.error(-13, "TestScene4: Model was null!");
		}
		
		
		
		Instance model2 = ResourceManager.create("ia.obj");
		
		if(model2 != null) {
			model2.getTransform().scale(0.20);//ia
			model2.getTransform().translation(1, 0, 0);//IA
			model2.bake(null);
			model2.setMaterial(new FresnelMetalMaterial(new Color(1.0, 0.8, 0.6, 1.0), 0.55, 5.20));
			this.addChild(model2);
		}else{
			Logger.error(-13, "TestScene4: Model2 was null!");
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
		plane.setMaterial(new ReflectiveMaterial(new Color(0x202020ff), 0.35));
		this.addChild(plane);
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
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