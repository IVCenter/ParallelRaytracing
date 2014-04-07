package tests;

import math.Matrix4;
import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.UpdateData;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.CSE168Proj1_DiffuseMaterial;
import raytrace.material.ColorMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.MatrixTransformSurface;
import system.Configuration;

public class CSE168_Project1_Scene extends Scene{
	
	/*
	 * A scene for CSE 168's Project 1
	 */
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{	
		skyMaterial = new ColorMaterial(new Color(0.8, 0.9, 1.0));
		

		// Create camera
		activeCamera = new PinholeCamera();
		activeCamera.setPosition(new Vector4(2.0, 2.0, 5.0, 0));
		activeCamera.setViewingDirection(activeCamera.getPosition().multiply3(-1.0).normalize3());
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).setVerticalFieldOfView(Math.PI * (40.0 / 180.0));
		
		// Create boxes
		//Box 1
		MeshSurface box1 = new Cube(5.0, 0.1, 5.0);
		box1.setMaterial(new CSE168Proj1_DiffuseMaterial(Color.white()));
		this.addChild(box1);
		
		//Box 2
		MeshSurface box2 = new Cube(1.0, 1.0, 1.0);
		box2.setMaterial(new CSE168Proj1_DiffuseMaterial(Color.white()));

		//Instance 1
		MatrixTransformSurface inst1 = new MatrixTransformSurface();
		Matrix4 mtx = new Matrix4();
		mtx.identity();
		mtx.rotateX(0.5);
		mtx.translation(0.0, 1.0, 0.0);
		inst1.setTransform(mtx);
		inst1.addChild(box2);
		this.addChild(inst1);
		

		//Instance 2
		//TODO: This seemed to use the same matrix as the previous instance....does that affect this implementation?
		MatrixTransformSurface inst2 = new MatrixTransformSurface();
		Matrix4 mtx2 = new Matrix4();
		mtx2.identity();
		mtx2.rotateY(1.0);
		mtx2.translation(-1.0, 0.0, 1.0);
		inst2.setTransform(mtx2);
		inst2.addChild(box2);
		this.addChild(inst2);

		//Sun Light
		DirectionalLight sunlgt = new DirectionalLight();
		sunlgt.setColor(new Color(1.0, 1.0, 0.9));
		sunlgt.setIntensity(0.5);
		sunlgt.setDirection(new Vector4(-0.5, -1.0, -0.5, 0));
		lightManager.addLight(sunlgt);
		
		//Red Light
		PointLight redlgt = new PointLight();
		redlgt.setColor(new Color(1.0, 0.2, 0.2));
		redlgt.setIntensity(2.0);
		redlgt.setConstantAttenuation(0.0);
		redlgt.setPosition(new Vector4(2.0, 2.0, 0.0, 0));
		lightManager.addLight(redlgt);
		
	}
	
	@Override
	public void update(UpdateData data)
	{
		//Update the children
		super.update(data);
	}

}
