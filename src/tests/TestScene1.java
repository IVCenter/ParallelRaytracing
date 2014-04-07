package tests;

import math.Matrix4;
import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.MatrixTransformSurface;
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

		//skyMaterial = new ColorMaterial(new Color(0xff0068ff));
		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
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
		plane.setMaterial(new ReflectiveMaterial(new Color(0xffffffff), 0.35));
		//plane.setMaterial(new DiffuseMaterial(Color.grey(0.2)));
		this.addChild(plane);
		
		//Make a sphere
		sphere = new Sphere();
		//sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7)));
		//sphere.setMaterial(new ReflectiveMaterial(Color.grey(0.7), 1.0));
		sphere.setMaterial(new DielectricMaterial(new Color(0xccddffff), 0.96));
		this.addChild(sphere);
		
		
		//Test transform
		MatrixTransformSurface matTrans = new MatrixTransformSurface();
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.translation(-1.5, -0.2, 0);
			matTrans.setTransform(mat);
			this.addChild(matTrans);
		}

		MatrixTransformSurface matTrans2 = new MatrixTransformSurface();
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.rotateY(Math.PI / 4.0);
			matTrans2.setTransform(mat);
			matTrans.addChild(matTrans2);
		}

		MatrixTransformSurface matTrans3 = new MatrixTransformSurface();
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.scale(2.5);
			matTrans3.setTransform(mat);
			matTrans2.addChild(matTrans3);
		}

		MatrixTransformSurface matTrans4 = new MatrixTransformSurface();
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.translation(1, 0, 0);
			mat.rotateZ(0.2);
			matTrans4.setTransform(mat);
			matTrans3.addChild(matTrans4);
		}
		
		
		//Test Cube
		{
			Cube cube = new Cube(2,1,3);
			cube.setMaterial(new ReflectiveMaterial(Color.grey(1.0), 0.35));
			//cube.setMaterial(new DiffuseMaterial(Color.white()));
			//matTrans4.addChild(cube);
		}
		
		//Test cube for dielectric material
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.translation(0, 2, 2);
			mat.rotateY(0.0);
			
			MatrixTransformSurface mts = new MatrixTransformSurface(mat);
			this.addChild(mts);
			
			Cube cube = new Cube(2,2,0.1);
			cube.setMaterial(new DielectricMaterial(new Color(0xeeeeeeff), 5.50));
			//cube.setMaterial(new DiffuseMaterial(Color.white()));
			//mts.addChild(cube);
		}
		
		
		Triangle tri = new Triangle();
		tri.setMaterial(new DiffuseMaterial(Color.white()));
		Vector4 ZAxis = new Vector4(0,0,1,0);
		tri.setVertex(0, new Vertex(new Vector4(-1,1,0,0), ZAxis, null));
		tri.setVertex(1, new Vertex(new Vector4(1,1,0,0), ZAxis, null));
		tri.setVertex(2, new Vertex(new Vector4(0,2,0,0), ZAxis, null));
		//this.addChild(tri);
		
		for(int i = 0; i < 384; i++)
		{
			Sphere sphere = new Sphere();
			
			double rand = Math.random();
			if(rand < 0.5) {
				sphere.setMaterial(new ReflectiveMaterial(Color.grey(0.9), Math.random()));
			}else if(rand < 0.8){
				sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7 + (Math.random()/2.0 - 0.25)   )));
			}else{
				sphere.setMaterial(new DielectricMaterial(Color.random(0.85 + (Math.random()/16.0)), -0.24 + rand* 2.0));
			}
			
			sphere.setPosition(new Vector4(10 * Math.random() - 5.0, 6 * Math.random(), 10 * Math.random() - 8.0, 0));
			sphere.setRadius(Math.pow(Math.random() * 0.4, 1.15));
			this.addChild(sphere);
		}
		
		
		//Make a point light
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x88ffb8ff));
			pointLight.setPosition(new Vector4(0,4,4,0));
			pointLight.setIntensity(12.0);
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
			pointLight.setPosition(new Vector4(0.0,6.0,1.0,0));
			pointLight.setIntensity(8.0);
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
