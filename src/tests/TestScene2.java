package tests;

import math.Vector4;
import process.logging.Logger;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircleAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene2 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = Math.PI / 16.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(10);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircleAperture(0.2, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(6.0);
		((ProgrammableCamera)activeCamera).forceUpdate();
		
		
		
		//Diffuse Sphere
		{
			Sphere sphere = new Sphere();
			//sphere.setMaterial(new DiffuseMaterial(new Color(0.9, 0.3, 0.3)));
			sphere.setMaterial(new FresnelDiffusePTMaterial(new Color(0.5, 0.2, 0.2), 0.9, 1.5, 1));
			sphere.setPosition(new Vector4(-3.5, 1, -1, 0));
			sphere.setRadius(1.0);
			this.addChild(sphere);
		}
		
		//Dielectric Sphere
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(new DielectricMaterial(new Color(1.0, 0.9, 1.0), 1.30));
			sphere.setPosition(new Vector4(0, 1, -1, 0));
			sphere.setRadius(1.0);
			this.addChild(sphere);
		}
		
		//Reflective Sphere
		{
			Color copper = new Color(0.98, 0.8, 0.6);
			Sphere sphere = new Sphere();
			//sphere.setMaterial(new ReflectiveMaterial(new Color(1.0, 0.8, 0.7), 0.74));
			sphere.setMaterial(new FresnelMetalMaterial(copper.multiply3M(copper), 0.317, 2.63));
			sphere.setPosition(new Vector4(3.5, 1, -1, 0));
			sphere.setRadius(1.0);
			this.addChild(sphere);
		}
		

		//Reflective Sphere
		{
			Sphere sphere = new Sphere();
			//sphere.setMaterial(new ReflectiveMaterial(new Color(1.0, 0.8, 0.7), 0.74));
			sphere.setMaterial(new ColorMaterial(new Color(1.0, 1.0, 1.0)));
			sphere.setPosition(new Vector4(0, 4, -10, 0));
			sphere.setRadius(1.0);
			//this.addChild(sphere);
		}
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
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
		
		
		

		//Make a plane
		//This is down here since infinitely large objects cause problems with BVHs...
		Plane plane = new Plane();
		plane.setMaterial(new ReflectiveMaterial(new Color(0xffeeddff), 0.35));
		this.addChild(plane);
		
		
	}
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		Vector4 position = activeCamera.getPosition();
		//position.set(Math.cos(elapsed * 8) * 5, 3, Math.sin(elapsed * 8) * 5, 0);
		//activeCamera.setPosition(position);
		
		//activeCamera.setViewingDirection(position.multiply3(-1.0));
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		this.updateBoundingBox();
	}
}
