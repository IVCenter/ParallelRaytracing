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
		
		

		Cube cubeBase = new Cube(1.0, 1.0, 1.0);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.6), Color.gray(1.0));
		//MultiplicativeT3DBlend mblend = new MultiplicativeT3DBlend(simplex, simplex);
		//MultiplicativeT3DBlend m3blend = new MultiplicativeT3DBlend(simplex, mblend);
		//MultiplicativeT3DBlend m3cblend = new MultiplicativeT3DBlend(new ColorTexture3D(Color.gray(4.0)), m3blend);
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().scale(2.0);
		
		ArrayList<Triangle> triangles = cubeBase.tessellate(100).getTriangles();
		
		for(Triangle tri : triangles)
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				vert.setPosition(pos.multiply3(1.0 + noise.intensity3()));
			}
			tri.generateFaceNormal();
		}
		
		CompositeSurface cube = AABVHSurface.makeAABVH(triangles);
		
		
		
		
		cube.updateBoundingBox();
		cube.setDynamic(false);
		int cubeCount = 2480 * 0;
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
			inst.setDynamic(false);
			cubes.add(inst);
		}
		
		{
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					new Color(0xff0068ff), 
					new Color(0xffff11ff), 
					0.5
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			
			Instance inst = new Instance();
			inst.addChild(cube);
			
			inst.getTransform().translate(0, 2, 0);
			inst.getTransform().scale(3.0);
			inst.setMaterial(new DiffusePTMaterial(gradientTrans));

			inst.updateBoundingBox();
			inst.bake(null);
			inst.setDynamic(false);
			cubes.add(inst);
		}
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(cubes, 1, 4);
		//Add all spheres at once
		this.addChild(aabvh);
		
		
		
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
