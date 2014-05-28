package tests;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import math.Vector4;
import math.noise.WorleyNoise3D;
import process.logging.Logger;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Cylinder;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.map.texture._3D.SimplexInterpolationTexture3D;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.blend.binary.InterpolationBBlend;
import raytrace.material.blend.unary.FullySaturateUBlend;
import raytrace.material.blend.unary.GrayscaleUBlend;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import resource.ResourceManager;
import system.Configuration;

public class TestScene6 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = 0.0;
	Instance model;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		Configuration.setScreenWidth(800);
		Configuration.setScreenHeight(600);

		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(10);
		activeCamera.setPosition(new Vector4(0,2.5,5,0));
		activeCamera.setViewingDirection(new Vector4(0.1,-0.15,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.06, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(6.0);
		((ProgrammableCamera)activeCamera).forceUpdate();
		
		
		//Make a cylinder
		{
			SimplexInterpolationTexture3D sit3d = new SimplexInterpolationTexture3D(
				new Color(0.96, 0.96, 0.96), new Color(0, 0, 0));
		
			
			Sphere sphere = new Sphere();
			Instance inst = new Instance();
			inst.addChild(sphere);
			inst.setMaterial(
					//new InterpolationBBlend(
					//		new DielectricPTMaterial(new Color(1.0, 1.0, 1.0), 1.4),
							new AshikhminPTMaterial(new Color(0.3, 0.05, 0.05), sit3d, 0.8, 0.2, 100, 1000)//,
					//		0.2
					//)
					);
			inst.getTransform().translate(0, 2.0, 0.0);
			inst.getTransform().scale(1.5);
			//inst.getTransform().rotateX(1);
			
			inst.updateBoundingBox();
			inst.bake(null);
			this.addChild(inst);
		}

		

		Cube cube = new Cube(1.0, 1.0, 1.0);		
		int cubeCount = 2480;
		ArrayList<CompositeSurface> cubes = new ArrayList<CompositeSurface>(cubeCount + 1);
		
		for(int x = 0; x < cubeCount; ++x)
		{
			Instance inst = new Instance();
			inst.addChild(cube);
			
			double radius = 1.0 + randInRange(0.0, 12.0);
			double phi = 2.0 * Math.PI * Math.random();
			inst.getTransform().translate(Math.cos(phi) * radius + 0.5, 0, Math.sin(phi) * radius - 2.0);//IA
			double scale = randInRange(0.1, 0.6);
			scale = Math.pow(scale, 1.2);
			inst.getTransform().scale(scale);
			inst.getTransform().rotateX(Math.random() * 0.5);
			inst.getTransform().rotateY(Math.random() * Math.PI);
			double greyBase = 0.30;
			inst.setMaterial(new DiffusePTMaterial(Color.gray(greyBase + (1.0 - greyBase) * Math.random())));

			inst.updateBoundingBox();
			inst.bake(null);
			cubes.add(inst);
		}
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(cubes, 1, 1);
		//Add all spheres at once
		this.addChild(aabvh);
		
		
		Sphere sphere = new Sphere();
		int sphereCount = 256;
		ArrayList<CompositeSurface> spheres = new ArrayList<CompositeSurface>(sphereCount + 1);
		
		for(int x = 0; x < sphereCount; ++x)
		{
			Instance inst = new Instance();
			inst.addChild(sphere);
			double radius = 36.0 + randInRange(0.0, 24.0);
			double phi = -2.0 * Math.PI * Math.random();
			inst.getTransform().translate(Math.cos(phi) * radius, 0, Math.sin(phi) * radius );//IA
			inst.getTransform().scale(randInRange(3.0, 8.0));
			double greyBase = 0.70;
			inst.setMaterial(new DiffusePTMaterial(Color.gray(greyBase + (1.0 - greyBase) * Math.random())));
			inst.updateBoundingBox();
			inst.bake(null);
			spheres.add(inst);
		}

		AABVHSurface aabvhSpheres = AABVHSurface.makeAABVH(spheres, 1, 4);
		//Add all spheres at once
		this.addChild(aabvhSpheres);
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		


		//Make a cylinder
		{
			Cylinder cylinder = new Cylinder(2.0, 0.5);
			Instance cylInst = new Instance();
			cylInst.addChild(cylinder);
			cylInst.setMaterial(new DiffusePTMaterial(new Color(0.6, 0.6, 0.6)));
			cylInst.getTransform().translate(0, 2.0, 0.0);
			cylInst.getTransform().rotateX(1);
			cylInst.updateBoundingBox();
			cylInst.bake(null);
			//this.addChild(cylInst);
		}
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		
		AABVHSurface aabvh2 = AABVHSurface.makeAABVH(this.getChildren(), 1, 2);
		this.getChildren().clear();
		this.addChild(aabvh2);
		
		//Refresh
		this.updateBoundingBox();
		

		//Make a plane
		Plane plane = new Plane();
		plane.setMaterial(new DiffusePTMaterial(Color.gray(0.8)));
		this.addChild(plane);
		
		
		//Make a cylinder
		{
			Cylinder cylinder = new Cylinder(2.0, 0.5);
			Instance cylInst = new Instance();
			cylInst.addChild(cylinder);
			cylInst.setMaterial(new DiffusePTMaterial(new Color(0.9, 0.6, 0.6)));
			cylInst.getTransform().translate(0, 2.0, 0.0);
			cylInst.getTransform().rotateX(1);
			cylInst.updateBoundingBox();
			cylInst.bake(null);
			//this.addChild(cylInst);
		}
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	@Override
	public void update(UpdateData data)
	{
		/*
		elapsed += data.getDt();
		
		Vector4 lotusCenter = model.getBoundingBox().getMidpoint();
		
		ProgrammableCamera acam = (ProgrammableCamera)activeCamera;
		
		CircularAperture cap = (CircularAperture)acam.getAperture();

		cap.setRadius(Math.max(1.0-elapsed*1.5, 0.06));
		
		Vector4 camPos = acam.getPosition();
		camPos.set(Math.cos(elapsed + Math.PI/2.0) * 8.0 + lotusCenter.get(0), 
							Math.min(0.2 + elapsed*2, 3.6), 
							Math.sin(elapsed + Math.PI/2.0) * 8.0  + lotusCenter.get(2), 
							1.0);
		
		Vector4 lotusPos = new Vector4(lotusCenter);
		lotusPos.set(1, 0);
		lotusPos = lotusPos.subtract3(camPos);
		lotusPos.set(1, Math.min(-2.0 + elapsed*0.40, 1.35));
		acam.setViewingDirection(lotusPos);

		acam.setFocalPlaneDistance(Math.min(1.0 + elapsed*1.5, lotusPos.subtract3(camPos).magnitude3()));
		*/
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
