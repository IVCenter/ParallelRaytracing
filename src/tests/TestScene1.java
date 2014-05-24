package tests;

import java.util.ArrayList;

import process.logging.Logger;
import math.Matrix4;
import math.Vector4;
import raytrace.camera.ProgrammableCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.acceleration.AABVHSurface;
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
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(1);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).forceUpdate();
		//this.addChild(activeCamera);
		
		
		
		//Make a sphere
		sphere = new Sphere();
		sphere.setRadius(1.8);
		//sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7)));
		sphere.setMaterial(new ReflectiveMaterial(Color.grey(0.7), 0.98));
		//sphere.setMaterial(new DielectricMaterial(new Color(1.01, 1.01, 0.99), 1.45));
		//this.addChild(sphere);
		
		/*
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
		*/
		
		
		//Test Cube
		{
			//Cube cube = new Cube(2,1,3);
			//cube.setMaterial(new ReflectiveMaterial(Color.grey(1.0), 0.35));
			//cube.setMaterial(new DiffuseMaterial(Color.white()));
			//matTrans4.addChild(cube);
		}
		
		//Test cube for dielectric material
		{
			Matrix4 mat = new Matrix4();
			mat.identity();
			mat.translate(0, 0.22501, 2);
			mat.rotateY(Math.PI/7.0);
			mat.nonUniformScale(2.0, 0.5, 2.0);
			
			//MatrixTransformSurface mts = new MatrixTransformSurface(mat);
			//this.addChild(mts);
			
			Cube cube = new Cube(0.5,1.9,0.5);
			//cube.setMaterial(new DielectricMaterial(new Color(1.05, 1.0, 1.05), 1.1));
			cube.setMaterial(new ReflectiveMaterial(new Color(1.0, 0.6, 0.4), 0.6));
			//cube.setMaterial(new DiffuseMaterial(Color.white()));
			//mts.addChild(cube);
		}
		
		
		//For big loads, allocate the size we'l need ahead of time
		ArrayList<CompositeSurface> spheres = new ArrayList<CompositeSurface>(11600);
		
		for(int i = 0; i < 10012; i++)
		{
			Sphere sphere = new Sphere();
			
			double rand = Math.random();
			if(rand < 0.0) {
				sphere.setMaterial(new ReflectiveMaterial(Color.random(0.0), Math.random()/2.0 + 0.5));
			}else if(rand < 0.0){
				sphere.setMaterial(new DiffuseMaterial(Color.grey(0.7 + (Math.random()/2.0 - 0.25)   )));
			}else if(rand < 0.0){
				sphere.setMaterial(new DiffusePTMaterial(Color.grey(0.7 + (Math.random()/2.0 - 0.25))));
			}else if(rand < 0.0){
				sphere.setMaterial(new DielectricMaterial(Color.random(0.7 + (Math.random()/16.0)), randInRange(1.01, 2.0)));
			}else if(rand < 1.0){
				sphere.setMaterial(new FresnelMetalMaterial(Color.random(0.7 + (Math.random()/16.0)), randInRange(0.51, 2.0), randInRange(2.01, 5.0)));
			}else{
				sphere.setMaterial(new ColorMaterial(Color.random(0.7 + (Math.random()/16.0))));
			}

			//sphere.setPosition(new Vector4(10 * Math.random() - 5.0, 6 * Math.random(), 10 * Math.random() - 8.0, 0));
			sphere.setPosition(new Vector4(80 * Math.random() - 40.0, 2.2 * Math.random(), 60 * Math.random() - 58.5, 0));
			sphere.setRadius(Math.pow(Math.random() * 0.8, 1.15));
			spheres.add(sphere);
			
			//if(i % 10000 == 0)
			//	Logger.progress(-1, "Loaded " + i + " spheres into this surface.");
		}
		
		for(int i = 0; i < 812; i++)
		{
			Sphere sphere = new Sphere();
			
			//double rand = Math.random();
			//sphere.setMaterial(new ReflectiveMaterial(Color.random(0.0), Math.random()/2.0 + 0.5));
			sphere.setMaterial(new DielectricMaterial(Color.random(0.7 + (Math.random()/16.0)), randInRange(1.01, 2.0)));

			//sphere.setPosition(new Vector4(10 * Math.random() - 5.0, 6 * Math.random(), 10 * Math.random() - 8.0, 0));
			sphere.setPosition(new Vector4(8 * Math.random() - 4.0, 3 * Math.random(), 6 * Math.random() - 1.0, 0));
			sphere.setRadius(Math.pow(Math.random() * 0.1, 1.15));
			spheres.add(sphere);
			
			//if(i % 10000 == 0)
			//	Logger.progress(-1, "Loaded " + i + " spheres into this surface.");
		}
		
		//Place a sphere infront of the camera
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(new DielectricMaterial(Color.white(), 0.85));

			//sphere.setPosition(new Vector4(10 * Math.random() - 5.0, 6 * Math.random(), 10 * Math.random() - 8.0, 0));
			sphere.setPosition(new Vector4(0.0, 2.0, 4.40, 0));
			sphere.setRadius(0.5);
			//spheres.add(sphere);
		}
		
		
		
		//Add the spheres to this
		this.addChildrenUnsafe(spheres);
		
		
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
			//lightManager.addLight(pointLight);
		}
		{
			PointLight pointLight = new PointLight();
			pointLight.setColor(new Color(0x68ff00ff));
			pointLight.setPosition(new Vector4(-3,0.5,1.0,0));
			pointLight.setIntensity(8.0);
			//lightManager.addLight(pointLight);
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
		//plane.setMaterial(new DielectricMaterial(new Color(0xffffffff), 1.35));
		//plane.setMaterial(new DiffuseMaterial(Color.grey(0.2)));
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
		
		
		Vector4 pos = sphere.getPosition();
		pos.set(Math.cos(elapsed), 1.0 + Math.sin(elapsed), 0.0, 1);
		sphere.setPosition(pos);

		
		
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
