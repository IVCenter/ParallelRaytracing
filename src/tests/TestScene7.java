package tests;

import math.Vector3;
import process.logging.Logger;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene7 extends Scene
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
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(100);
		activeCamera.setPosition(new Vector3(0,2,3));
		activeCamera.setViewingDirection(new Vector3(0,-0.1,-1));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.2, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(4.0);
		
		
		//Dielectric Sphere
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(new DielectricPTMaterial(new Color(1.0, 0.9, 1.0), 1.30));
			sphere.setPosition(new Vector3(-1, 1, -1));
			sphere.setRadius(1.0);
			this.addChild(sphere);
		}
		
		//Light Sphere
		{
			Sphere sphere = new Sphere();
			//sphere.setMaterial(new ReflectiveMaterial(new Color(1.0, 0.8, 0.7), 0.74));
			sphere.setMaterial(new ColorMaterial(new GradientTexture3D(
					(new Color(0xffff88ff)).multiply3M(100.0), 
					(new Color(0xff0068ff)).multiply3M(10.0), 
					1.0)));
			//sphere.setRadius(0.5);
			
			Instance inst = new Instance();
			inst.addChild(sphere);
			inst.getTransform().scale(0.35);
			inst.getTransform().translate(0, 4, -1);
			inst.bake(null);
			this.addChild(inst);
		}
		
		//Box
		{
			Cube cube = new Cube(1, 1, 1);
			Instance inst = new Instance();
			inst.addChild(cube);
			inst.getTransform().nonUniformScale(8, 6, 8);
			inst.getTransform().translate(0.0, 2.0, 0.0);
			inst.setMaterial(new DiffusePTMaterial(Color.gray(0.9)));
			inst.bake(null);
			this.addChild(inst);
		}
		
		/*
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		*/
		
		

		//Make a plane
		//This is down here since infinitely large objects cause problems with BVHs...
		Plane plane = new Plane();
		plane.setMaterial(new DiffusePTMaterial(Color.gray(0.9)));
		this.addChild(plane);
		
		
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
		super.bake(data);
	}
}
