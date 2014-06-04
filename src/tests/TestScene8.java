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
import raytrace.geometry.meshes.Grid;
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
import raytrace.map.texture._3D.blend.MaskT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.PosterMaskT3DBlend;
import raytrace.map.texture._3D.blend.SimplexInterpolationT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.Material;
import raytrace.material.SkyGradientMaterial;
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		setupFloor();

		setupCenterPiece();
		
		
		
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
		skyMaterial = new SkyGradientMaterial(new GradientTexture3D(new Color(0xe8f8ffff), new Color(0xddeeffff)));
	}
	

	/* *********************************************************************************************
	 * Camera
	 * *********************************************************************************************/
	private void setupCamera()
	{
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(2);
		activeCamera.setPosition(new Vector4(0,2.8,5.5,0));
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
	
	

	/* *********************************************************************************************
	 * Geometry
	 * *********************************************************************************************/
	private void setupFloor()
	{
		//Stone?
		{
			Grid base = new Grid(20.0, 20.0);
			
			SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.004), Color.gray(0.001));
			MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
			simplexTrans.getTransform().nonUniformScale(10.0, 10.0, 10.0);
	
			SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0), Color.gray(0.001));
			//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
			MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
			simplexTrans2.getTransform().nonUniformScale(31.0, 31.0, 31.0);
			
			SimplexNoiseTexture3D simplex3 = new SimplexNoiseTexture3D(Color.gray(0.08), Color.gray(-0.2));
			MatrixTransformTexture3D simplexTrans3 = new MatrixTransformTexture3D(simplex3);
			simplexTrans3.getTransform().nonUniformScale(0.5, 0.5, 1.5);
			
			
			AdditiveT3DBlend add1 = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
			AdditiveT3DBlend add2 = new AdditiveT3DBlend(add1, simplexTrans3);
			
			MeshSurface mesh = base.tessellate(600);
			
			
			//Shift Y
			Vector4 multi = new Vector4();
			for(Triangle tri : mesh.getTriangles())
			{
				for(Vertex vert : tri.getVertices())
				{
					Vector4 pos = vert.getPosition();
					Color noise = add2.evaluate(pos.get(0), pos.get(1), pos.get(2));
					multi.set(0, noise.intensity3(), 0, 0);
					vert.setPosition(pos.add3(multi));
				}
				tri.generateFaceNormal();
				tri.setDynamic(true);
				tri.updateBoundingBox();
				tri.setDynamic(false);
			}
			
			
			CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
			
			
			
			
			cube.updateBoundingBox();
			cube.setDynamic(false);
			
			{
				GradientTexture3D gradient = new GradientTexture3D(Color.black(), Color.white(), 1.0);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(4.0);
				

				SimplexNoiseTexture3D simplex4 = new SimplexNoiseTexture3D();
				MatrixTransformTexture3D simplexTrans4 = new MatrixTransformTexture3D(simplex4);
				simplexTrans4.getTransform().nonUniformScale(27, 27, 29);

				SimplexNoiseTexture3D simplex5 = new SimplexNoiseTexture3D();
				MatrixTransformTexture3D simplexTrans5 = new MatrixTransformTexture3D(simplex5);
				simplexTrans5.getTransform().nonUniformScale(63, 61, 63);

				SimplexNoiseTexture3D simplex6 = new SimplexNoiseTexture3D();
				MatrixTransformTexture3D simplexTrans6 = new MatrixTransformTexture3D(simplex6);
				simplexTrans6.getTransform().nonUniformScale(37, 21, 33);
				
				AdditiveT3DBlend add3 = new AdditiveT3DBlend(simplexTrans4, simplexTrans5);
				SubtractiveT3DBlend sub1 = new SubtractiveT3DBlend(simplexTrans5, simplexTrans6);
				
				OneMinusT3DBlend one1 = new OneMinusT3DBlend(sub1);
				
				MultiplicativeT3DBlend multi1 = new MultiplicativeT3DBlend(one1, add3);

				//MaskT3DBlend mask1 = new MaskT3DBlend(add3, sub1, multi1);
				MaskT3DBlend mask1 = new MaskT3DBlend(
						new ColorTexture3D(new Color(0.5, 0.5, 1.0)), 
						new ColorTexture3D(new Color(1.0, 0.5, 0.9)), 
						multi1);
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(0, 0, 0);
				inst.getTransform().scale(2.0);
				inst.setMaterial(addFrost(new FresnelDiffusePTMaterial(mask1, 0.5, 2.0), 0.6));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
		}
		
		
		
		//Water?
		{
			Grid base = new Grid(20.0, 20.0);
			
			SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.01), Color.gray(0.1));
			MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
			simplexTrans.getTransform().nonUniformScale(10.0, 10.0, 10.0);

			SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0), Color.gray(0.001));
			//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
			MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
			simplexTrans2.getTransform().nonUniformScale(31.0, 31.0, 31.0);
			
			
			AdditiveT3DBlend add1 = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
			//AdditiveT3DBlend add2 = new AdditiveT3DBlend(add1, simplexTrans3);
			
			MeshSurface mesh = base.tessellate(600);
			
			
			//Shift Y
			Vector4 multi = new Vector4();
			for(Triangle tri : mesh.getTriangles())
			{
				for(Vertex vert : tri.getVertices())
				{
					Vector4 pos = vert.getPosition();
					Color noise = add1.evaluate(pos.get(0), pos.get(1), pos.get(2));
					multi.set(0, noise.intensity3(), 0, 0);
					vert.setPosition(pos.add3(multi));
				}
				tri.generateFaceNormal();
				tri.setDynamic(true);
				tri.updateBoundingBox();
				tri.setDynamic(false);
			}
			
			
			CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
			
			
			
			
			cube.updateBoundingBox();
			cube.setDynamic(false);
			
			{
				//GradientTexture3D gradient = new GradientTexture3D(, Color.white(), 1.0);
				//MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				//gradientTrans.getTransform().scale(4.0);
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(0, 0.09, 0);
				inst.getTransform().scale(2.0);
				inst.setMaterial(new DielectricPTMaterial(new Color(1.02, 1.018, 1.015), 1.31, 0.1));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));

				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
		}
	}
	
	private Material addFrost(Material base, double temperature)
	{
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
		
		MatrixTransformTexture3D trans2 = new MatrixTransformTexture3D(subBlend);
		trans2.getTransform().rotateX(Math.PI/2.0);
		trans2.getTransform().rotateY(-0.1);
		trans2.getTransform().translate(0, 1.73, 0);
	
		Material frostedMat = new PosterMaskBBlend(
				new DiffusePTMaterial(new OneMinusT3DBlend(new MultiplicativeT3DBlend(trans2, trans2))),
				base,
				trans2,
				temperature
			);
		
		return frostedMat;
	}
	
	private void setupCenterPiece()
	{
		//Cube base = new Cube(1.0, 1.0, 1.0);
		Sphere base = new Sphere();
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.6), Color.gray(0.8));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(2.0, 8.0, 2.0);

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(-0.004), Color.gray(0.004));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(16.0, 16.0, 64.0);
		
		MeshSurface mesh = base.tessellate(300);
		//mesh.synchronizeVertices();
		//ArrayList<Triangle> triangles =;
		
		Vector4 multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
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
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans2.evaluate(pos.get(0) * 2.0, pos.get(1) * 2.0, pos.get(2) * 2.0);
				vert.setPosition(pos.add3M(vert.getNormal().multiply3(noise.intensity3())));
				//vert.getPosition().print();
			}
			//tri.generateFaceNormal();
			//tri.setDynamic(true);
			//tri.updateBoundingBox();
			//tri.setDynamic(false);
		}
		for(Triangle tri : mesh.getTriangles())
		{
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		//double maxLength = 0.15;
		Logger.progress(-50, "Tesselating " + mesh.getTriangles().size() + " triangles.");
		//mesh.tessellateMeshByTriangleLongestSideConstraint(maxLength);
		Logger.progress(-50, "Tesselating resulted in " + mesh.getTriangles().size() + " triangles.");

		
		//Smooth out the normals
		mesh.smoothNormals();
		
		/*
		for(Triangle tri : mesh.getTriangles())
		{
			//tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		*/
		
		
		CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
		
		
		
		
		cube.updateBoundingBox();
		cube.setDynamic(false);
		
		{
			/*
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					new Color(0xff0068ff), 
					new Color(0xffff44ff), 
					0.8
					);
					*/
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					(new Color(0xff1178ff)).multiply3M(1.2), 
					new Color(0xefefefff), 
					0.8
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			
			Instance inst = new Instance();
			inst.addChild(cube);
			
			inst.getTransform().translate(0, 2.5, 0);
			inst.getTransform().scale(2.5);
			inst.setMaterial(new DiffusePTMaterial(gradientTrans));
			//inst.setMaterial(new DiffuseMaterial(Color.white()));

			inst.updateBoundingBox();
			inst.bake(null);
			inst.setDynamic(false);
			this.addChild(inst);
		}
		
		//AABVHSurface aabvh = AABVHSurface.makeAABVH(cubes, 1, 4);
		//Add all spheres at once
		//this.addChild(aabvh);
	}
	
}
