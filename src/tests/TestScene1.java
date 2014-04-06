package tests;

import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import system.Configuration;

public class TestScene1 extends Scene {

	/*
	 * A simple test scene for debugging
	 */
	Sphere sphere;
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		// TODO Auto-generated method stub
		
		skyMaterial = new ColorMaterial(new Color(0xff0068ff));
		
		//super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		activeCamera = new PinholeCamera();
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		//this.addChild(activeCamera);
		
		
		//Make a plane
		Plane plane = new Plane();
		//plane.setMaterial(new DiffuseMaterial(Color.grey(0.2)));
		plane.setMaterial(new ReflectiveMaterial(Color.grey(0.9), 0.6));
		this.addChild(plane);
		
		//Make a sphere
		sphere = new Sphere();
		//sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7)));
		sphere.setMaterial(new ReflectiveMaterial(Color.grey(0.7), 1.0));
		this.addChild(sphere);
		
		for(int i = 0; i < 256; i++)
		{
			Sphere sphere = new Sphere();
			
			if(Math.random() < 0.8) {
				sphere.setMaterial(new ReflectiveMaterial(Color.grey(0.9), Math.random()));
			}else{
				sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7 + (Math.random()/2.0 - 0.25)   )));
			}
			
			sphere.setPosition(new Vector4(6 * Math.random() - 3.0, 6 * Math.random(), 8 * Math.random() - 6.0, 0));
			sphere.setRadius(Math.random() * 0.3);
			this.addChild(sphere);
		}
		
		
		//Make a point light
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x00ff68ff));
			pointLight.setPosition(new Vector4(0,4,4,0));
			pointLight.setIntensity(16.0);
			lightManager.addLight(pointLight);
		}
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x6800ffff));
			pointLight.setPosition(new Vector4(3,0.5,1.0,0));
			pointLight.setIntensity(8.0);
			lightManager.addLight(pointLight);
		}
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x68ff00ff));
			pointLight.setPosition(new Vector4(-3,0.5,1.0,0));
			pointLight.setIntensity(8.0);
			lightManager.addLight(pointLight);
		}
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0xff6800ff));
			pointLight.setPosition(new Vector4(0.0,2.0,0.5,0));
			pointLight.setIntensity(3.0);
			lightManager.addLight(pointLight);
		}
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-0.2,0));
		lightManager.addLight(directionalLight);
		
	}
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += Math.PI/4.0;
		
		Vector4 pos = sphere.getPosition();
		pos.set(Math.cos(elapsed), 1.0 + Math.sin(elapsed), 0.0, 1);
		sphere.setPosition(pos);

		
		
		//Update the children
		super.update(data);
	}

}
