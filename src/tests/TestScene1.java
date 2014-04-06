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
			mat.rotateZ(0.1);
			matTrans4.setTransform(mat);
			matTrans3.addChild(matTrans4);
		}
		
		
		//Test Cube
		Cube cube = new Cube(2,1,3);
		cube.setMaterial(new ReflectiveMaterial(Color.grey(1.0), 0.35));
		//cube.setMaterial(new DiffuseMaterial(Color.white()));
		matTrans4.addChild(cube);
		
		
		Triangle tri = new Triangle();
		tri.setMaterial(new DiffuseMaterial(Color.white()));
		Vector4 ZAxis = new Vector4(0,0,1,0);
		tri.setVertex(0, new Vertex(new Vector4(-1,1,0,0), ZAxis, null));
		tri.setVertex(1, new Vertex(new Vector4(1,1,0,0), ZAxis, null));
		tri.setVertex(2, new Vertex(new Vector4(0,2,0,0), ZAxis, null));
		//this.addChild(tri);
		
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
