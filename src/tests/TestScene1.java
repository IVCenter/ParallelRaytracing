package tests;

import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.material.ColorMaterial;
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
		plane.setMaterial(new ColorMaterial(Color.grey(0.2)));
		this.addChild(plane);
		
		//Make a sphere
		sphere = new Sphere();
		sphere.setMaterial(new ColorMaterial(Color.grey(0.7)));
		this.addChild(sphere);
		
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
