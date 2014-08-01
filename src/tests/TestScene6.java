package tests;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import math.Spline;
import math.Vector4;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.TchebyshevDistance3D;
import math.noise.SimplexNoise3D;
import math.noise.WorleyNoise3D;
import process.logging.Logger;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.ProgrammableCameraController;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Cylinder;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.map.normal._3D.TextureGradientNormalMap3D;
import raytrace.map.texture._3D.CircularGradientTexture3D;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.WorleyNoiseTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.MaskT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.PosterMaskT3DBlend;
import raytrace.map.texture._3D.blend.SimplexInterpolationT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.blend.binary.InterpolationBBlend;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.blend.binary.TextureMaskBBlend;
import raytrace.material.blend.unary.FullySaturateUBlend;
import raytrace.material.blend.unary.GrayscaleUBlend;
import raytrace.material.composite.NormalMapCMaterial;
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
	PosterMaskBBlend goldAndFrostMat;
	ProgrammableCameraController camController;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);

		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(1);
		activeCamera.setPosition(new Vector4(0,2.5,5,0));
		activeCamera.setViewingDirection(new Vector4(0.1,-0.15,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getRenderWidth());
		activeCamera.setPixelHeight(Configuration.getRenderHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.02, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(4.5);
		
		
		
		camController = new ProgrammableCameraController((ProgrammableCamera)activeCamera);
		
		//Position Spline
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 2.5, 5.0, 0.0));
			spline.add(new Vector4(-5.0, 2.5, 5.0, 0.0));
			spline.add(new Vector4(-5.0, 2.5, 0.0, 0.0));
			camController.addPositionSpline(spline, 0.2);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(-5.0, 2.5, 0.0, 0.0));
			spline.add(new Vector4(-5.0, 2.5, -5.0, 0.0));
			spline.add(new Vector4(0.0, 2.5, -5.0, 0.0));
			camController.addPositionSpline(spline, 0.2);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 2.5, -5.0, 0.0));
			spline.add(new Vector4(5.0, 2.5, -5.0, 0.0));
			spline.add(new Vector4(5.0, 2.5, 0.0, 0.0));
			camController.addPositionSpline(spline, 0.2);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(5.0, 2.5, 0.0, 0.0));
			spline.add(new Vector4(5.0, 2.5, 5.0, 0.0));
			spline.add(new Vector4(0.0, 2.5, 5.0, 0.0));
			camController.addPositionSpline(spline, 0.2);
		}
		
		//Look At
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 1.0, 0.0, 0.0));
			spline.add(new Vector4(0.0, 1.0, 0.0, 0.0));
			camController.addLookAtSpline(spline, 0.8);
		}
		
		//Camera Radius
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(0.5, 0.0, 0.0, 0.0));
			spline.add(new Vector4(0.0, 0.0, 0.0, 0.0));
			camController.addApertureRadiusSpline(spline, 0.8);
		}
		
		//Focal Distance
		{
			Spline spline = new Spline();
			spline.add(new Vector4(4.5, 0.0, 0.0, 0.0));
			spline.add(new Vector4(8.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(1.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(4.5, 0.0, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 0.8);
		}
		
		//Field of View
		{
			Spline spline = new Spline();
			spline.add(new Vector4(Math.PI/2.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(Math.PI/1.2, 0.0, 0.0, 0.0));
			spline.add(new Vector4(Math.PI/2.0, 0.0, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 0.8);
		}
		
		
		
		//Make a cylinder
		{
			//SimplexInterpolationT3DBlend sit3d = new SimplexInterpolationT3DBlend(
			//	new Color(1.4, 1.0, 0.95), new Color(1.3, 0.95, 1.3));
			
			SimplexNoiseTexture3D sntex1 = new SimplexNoiseTexture3D();
			sntex1.setFirstColor(new Color(0.0, 0.0, 0.0));
			sntex1.setSecondColor(new Color(1.0, 1.0, 1.0));
			
			MatrixTransformTexture3D mtrans1 = new MatrixTransformTexture3D(sntex1);
			mtrans1.getTransform().nonUniformScale(1, 32, 1);
			mtrans1.getTransform().rotateZ(-0.1);
			

			SimplexNoiseTexture3D sntex2 = new SimplexNoiseTexture3D();
			sntex2.setFirstColor(new Color(0.0, 0.0, 0.0));
			sntex2.setSecondColor(new Color(1.0, 1.0, 1.0));
			
			MatrixTransformTexture3D mtrans2 = new MatrixTransformTexture3D(sntex2);
			mtrans2.getTransform().nonUniformScale(2, 24, 4);
			mtrans2.getTransform().rotateZ(0.4);
			
			
			MultiplicativeT3DBlend multiBlend = new MultiplicativeT3DBlend();
			multiBlend.setFirstTexture(mtrans1);
			multiBlend.setSecondTexture(mtrans2);

			AdditiveT3DBlend addBlend = new AdditiveT3DBlend();
			addBlend.setFirstTexture(mtrans1);
			addBlend.setSecondTexture(mtrans2);

			//MaskT3DBlend maskblend = new MaskT3DBlend();
			//maskblend.setFirstTexture(mtrans1);
			//maskblend.setSecondTexture(mtrans2);
			
			WorleyNoiseTexture3D worleyTex1 = new WorleyNoiseTexture3D();
			worleyTex1.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
			worleyTex1.getNoiseFunction().setSelectionFunction(new SelectDifferenceNthMthNearest2D(7,1));
			
			MatrixTransformTexture3D worleyTex1Trans = new MatrixTransformTexture3D(worleyTex1);
			worleyTex1Trans.getTransform().scale(32);
			
			WorleyNoiseTexture3D worleyTex2 = new WorleyNoiseTexture3D();
			worleyTex2.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
			worleyTex2.getNoiseFunction().setSelectionFunction(new SelectNthNearest2D(3));
			worleyTex2.setSecondColor(new Color(1.1, 1.1, 1.1));
			
			MatrixTransformTexture3D worleyTex2Trans = new MatrixTransformTexture3D(worleyTex2);
			worleyTex2Trans.getTransform().scale(8);
			
			SubtractiveT3DBlend subBlend = new SubtractiveT3DBlend(worleyTex2Trans, worleyTex1Trans);
		
			double temperature = 0.5;
			
			goldAndFrostMat = new PosterMaskBBlend(
					//new DielectricPTMaterial(new Color(1.3, 1.3, 0.92), 3.01),
					//new DielectricPTMaterial(new Color(1.8, 1.95, 1.8), 1.31),
					//new DiffusePTMaterial(new Color(1.0, 0.6, 0.65)),
					//new AshikhminPTMaterial(Color.gray(0.0), Color.gray(1.0), 0.2, 0.8, 10, 10),
					//new AshikhminPTMaterial(new OneMinusT3DBlend(new MultiplicativeT3DBlend(subBlend, subBlend)), 
					//		Color.gray(1.0), 0.2, 0.95, 10, 1000),
					new DiffusePTMaterial(new OneMinusT3DBlend(new MultiplicativeT3DBlend(subBlend, subBlend))),
					//new FresnelMetalMaterial(new Color(0.95, 0.7, 0.3), 0.8, 2.1),
					new AshikhminPTMaterial(Color.gray(0.0), new Color(0.95, 0.7, 0.3), 1.0, 0.0, 256, 256),
					subBlend,
					temperature
				);
			
			//OneMinusT3DBlend oneminus = new OneMinusT3DBlend(subBlend);
			PosterMaskT3DBlend posterBlend = new PosterMaskT3DBlend(
					new OneMinusT3DBlend(new MultiplicativeT3DBlend(subBlend, subBlend)), 
					new ColorTexture3D(Color.gray(0.4)), 
					subBlend, 
					temperature-0.1
					);
			
			TextureGradientNormalMap3D normalMap = new TextureGradientNormalMap3D(posterBlend);
			normalMap.setSamplingRadius(0.01);
			normalMap.setStrength(100.01);
			//NormalMapCMaterial normMat = new NormalMapCMaterial(goldAndFrostMat, normalMap);
			//NormalMapCMaterial normMat = new NormalMapCMaterial(new DielectricPTMaterial(new Color(1.1, 1.1, 1.0), 1.31), normalMap);
			
			
			Sphere sphere = new Sphere();
			Instance inst = new Instance();
			inst.addChild(sphere);
			//inst.addChild(new Cube(1.0, 1.0, 1.0));
			inst.setMaterial(
					new ColorMaterial(new GradientTexture3D(new Color(0xff0066ff), new Color(0xffff00ff), 1.0))//normMat
					);
			//inst.setMaterial(new DiffusePTMaterial(subBlend));
			inst.getTransform().translate(0.0, 2.0, 0.0);
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
	
	//private double lvlMulti = 1.0;
	//private double level = 0.0;
	
	@Override
	public void update(UpdateData data)
	{
		
		elapsed += data.getDt();
		
		camController.upate(data.getDt());
		/*
		level += lvlMulti * data.getDt() * 4.0;
		
		if(level > 0.8 || level < 0.0)
			lvlMulti *= -1.0;
		
		System.out.println("Level: " + level);
		
		posterMat.setLevel(level);
		*/
		
		//Vector4 lotusCenter = model.getBoundingBox().getMidpoint();
		
		//ProgrammableCamera acam = (ProgrammableCamera)activeCamera;
		/*
		CircularAperture cap = (CircularAperture)acam.getAperture();

		cap.setRadius(Math.max(1.0-elapsed*1.5, 0.06));
		*/
		/*
		Vector4 camPos = acam.getPosition();
		camPos.set(Math.cos(elapsed + Math.PI/2.0) * 5.0, 
							2.5,//Math.min(0.2 + elapsed*2, 3.6), 
							Math.sin(elapsed + Math.PI/2.0) * 5.0, 
							1.0);
		
		Vector4 center = new Vector4();
		//lotusPos.set(1, 0);
		center = center.subtract3(camPos);
		center.set(1, 0);
		center.normalize3();
		center.set(1, -0.15);
		center.normalize3();
		acam.setViewingDirection(center);
		*/

		//acam.setFocalPlaneDistance(Math.min(1.0 + elapsed*1.5, lotusPos.subtract3(camPos).magnitude3()));
		
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
