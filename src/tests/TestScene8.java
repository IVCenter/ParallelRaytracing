package tests;

import java.util.ArrayList;

import math.Spline;
import math.Vector4;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.TchebyshevDistance3D;
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
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.map.normal._3D.TextureGradientNormalMap3D;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.WorleyNoiseTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.PosterMaskT3DBlend;
import raytrace.map.texture._3D.blend.SimplexInterpolationT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.composite.NormalMapCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene8 extends Scene
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
		//Configuration.setScreenWidth(800);
		//Configuration.setScreenHeight(600);

		//Setup the sky
		setupSky();
		
		
		//Setup the camera
		setupCamera();
		camController.pause();
		
		

		//Cube base = new Cube(1.0, 1.0, 1.0);
		Sphere base = new Sphere();
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.6), Color.gray(0.8));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(2.0, 8.0, 2.0);

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(-0.005), Color.gray(0.005));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(16.0, 16.0, 64.0);
		
		MeshSurface mesh = base.tessellate(300);
		//mesh.synchronizeVertices();
		ArrayList<Triangle> triangles = mesh.getTriangles();
		
		Vector4 multi = new Vector4();
		for(Triangle tri : triangles)
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3() * 0.2, 1.0 + noise.intensity3(), 0);
				vert.setPosition(pos.multiply3(multi));
			}
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		//Synchronize vertices
		mesh.synchronizeVertices();
		
		//Add mode noise!
		multi = new Vector4();
		for(Triangle tri : triangles)
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans2.evaluate(pos.get(0), pos.get(1), pos.get(2));
				vert.setPosition(pos.add3M(vert.getNormal().multiply3(noise.intensity3())));
				//vert.getPosition().print();
			}
			//tri.generateFaceNormal();
			//tri.setDynamic(true);
			//tri.updateBoundingBox();
			//tri.setDynamic(false);
		}
		for(Triangle tri : triangles)
		{
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		
		CompositeSurface cube = AABVHSurface.makeAABVH(triangles);
		
		
		
		
		cube.updateBoundingBox();
		cube.setDynamic(false);
		
		{
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					new Color(0xff0068ff), 
					new Color(0xffff44ff), 
					0.8
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			
			Instance inst = new Instance();
			inst.addChild(cube);
			
			inst.getTransform().translate(0, 2, 0);
			inst.getTransform().scale(2.5);
			inst.setMaterial(new DiffusePTMaterial(gradientTrans));

			inst.updateBoundingBox();
			inst.bake(null);
			inst.setDynamic(false);
			this.addChild(inst);
		}
		
		//AABVHSurface aabvh = AABVHSurface.makeAABVH(cubes, 1, 4);
		//Add all spheres at once
		//this.addChild(aabvh);
		
		
		
		//Setup the lighting
		setupLights();
		
		
		
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
		/*
		Plane plane = new Plane();
		plane.setMaterial(new DiffusePTMaterial(Color.gray(0.8)));
		this.addChild(plane);
		*/
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	@Override
	public void update(UpdateData data)
	{
		
		elapsed += data.getDt();
		
		camController.upate(data.getDt());
		
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		this.updateBoundingBox();
		super.bake(data);
	}
	
	


	

	/* *********************************************************************************************
	 * Sky
	 * *********************************************************************************************/
	private void setupSky()
	{
		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
	}
	

	/* *********************************************************************************************
	 * Camera
	 * *********************************************************************************************/
	private void setupCamera()
	{
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(10);
		activeCamera.setPosition(new Vector4(0,2.5,5,0));
		activeCamera.setViewingDirection(new Vector4(0.1,-0.15,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(/*0.02*/0.0002, 0.5));
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
	}
	
	

	

	/* *********************************************************************************************
	 * Lights
	 * *********************************************************************************************/
	private void setupLights()
	{
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		//Light 
	}
	
}
