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
		plane.setMaterial(new DiffuseMaterial(Color.grey(0.2)));
		this.addChild(plane);
		
		//Make a sphere
		sphere = new Sphere();
		sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7)));
		this.addChild(sphere);
		
		
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
			pointLight.setPosition(new Vector4(2,0.5,1.0,0));
			pointLight.setIntensity(4.0);
			lightManager.addLight(pointLight);
		}
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x68ff00ff));
			pointLight.setPosition(new Vector4(-2,0.5,1.0,0));
			pointLight.setIntensity(4.0);
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
		elapsed += 0.2;
		
		Vector4 pos = sphere.getPosition();
		pos.set(Math.cos(elapsed/8.0), 1.0 + Math.sin(elapsed/8.0), 0.0, 1);
		sphere.setPosition(pos);
		
		
		//Update the children
		super.update(data);
	}

}
