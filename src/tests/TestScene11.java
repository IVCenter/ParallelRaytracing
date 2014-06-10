package tests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import math.Spline;
import math.Vector4;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.TchebyshevDistance3D;
import process.logging.Logger;
import raytrace.bounding.BoundingBox;
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
import raytrace.light.PointLight;
import raytrace.map.normal._3D.TextureGradientNormalMap3D;
import raytrace.map.texture._3D.CircularGradientTexture3D;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.SphericalSineWaveTexture3D;
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
import raytrace.material.PassThroughMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.SubSurfaceDiffusePTTestMaterial;
import raytrace.material.blend.binary.FastIntensityMaskBBlend;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.composite.NormalMapCMaterial;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.MatrixTransformSurface;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene11 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = 0.0;
	Instance model;
	PosterMaskBBlend goldAndFrostMat;
	ProgrammableCameraController camController;
	
	Random seedGen;// = new Random(0x1a7bf4543L);
	Random random;// = new Random(0x07b56c7168a2dL);
	
	LinkedList<Triangle> globalTriangles;
	
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Configuration.setScreenWidth(800);
		//Configuration.setScreenHeight(600);

		
		//seedGen = new Random(0x1a7bf4543L);
		//seedGen = new Random(0x5a7bf90c243L);
		seedGen = new Random(0xd53ec24013L);
		random = new Random(0x07b56c7168a2dL);
		
		globalTriangles = new LinkedList<Triangle>();
		
		
		//Setup the sky
		setupSky();
		
		//Setup the camera
		setupCamera();
		camController.pause();
		
		
		


		//If this node is a middle tier, we dont need any data at all
		if(!Configuration.isClock() && !Configuration.isLeaf())
			return;
		
		//If this nose is a controller, we don't need any more loaded
		if(Configuration.isController() && !Configuration.isLeaf())
			return;
		
		
		
		
		
		//setupGridLines();
		
		
		setupMountainLine();
		
		System.gc();
		
		setupFloor();
		
		System.gc();
		
		//Accelerate the terrain
		//CompositeSurface terrain = AABVHSurface.makeAABVH(globalTriangles);
		//this.addChild(terrain);
		
		

		setupCenterPiece();
		
		System.gc();
		
		setupRocks();
		
		System.gc();
		
		setupGroundFruit();
		
		System.gc();
		
		setupKoi();
		
		System.gc();
		

		BoundingBox bb = new BoundingBox(new Vector4(-10, -0, -10, 0), new Vector4(10, 10, 10, 0));
		setupSnowParticles(bb, 1, 0.02, 0.01);
		
		System.gc();
		
		setupGrass();
		
		System.gc();
		
		
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
	
	@Override
	public void update(UpdateData data)
	{
		
		elapsed += data.getDt();

		
		if(Configuration.isClock())
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
	
	private double randInRange(double min, double max)
	{
		return min + random.nextDouble() * (max-min);
	}
	

	

	/* *********************************************************************************************
	 * Sky
	 * *********************************************************************************************/
	private void setupSky()
	{
		//skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		skyMaterial = new SkyGradientMaterial(new GradientTexture3D(new Color(0xe8f8ffff), new Color(0xddeeffff)));
		//skyMaterial = new SkyGradientMaterial(new GradientTexture3D(new Color(0x1828ffff), new Color(0xcce2ffff), 1.0));

		//skyMaterial = new RecursionMinimumCMaterial(
		//		new SkyGradientMaterial(new GradientTexture3D(new Color(0xffddeeff), new Color(0xddeeffff))), 1
		//		);
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
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(/*0.02*/0.026, 0.5));//0604 shot at 0.035
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(6.6);
		
		
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
			Grid base = new Grid(28.0, 28.0);
			
			SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.004), Color.gray(0.11));//was .12
			MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
			simplexTrans.getTransform().nonUniformScale(10.0, 10.0, 10.0);
	
			SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0), Color.gray(0.001));//was .001
			//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
			MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
			simplexTrans2.getTransform().nonUniformScale(31.0, 31.0, 31.0);
			
			SimplexNoiseTexture3D simplex3 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0.12), Color.gray(-1.8));
			MatrixTransformTexture3D simplexTrans3 = new MatrixTransformTexture3D(simplex3);
			simplexTrans3.getTransform().nonUniformScale(0.15, 0.15, 0.2);

			
			AdditiveT3DBlend add1 = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
			AdditiveT3DBlend add2 = new AdditiveT3DBlend(add1, simplexTrans3);
			
			
			
			//Radial raise
			CircularGradientTexture3D circGrad = new CircularGradientTexture3D(Color.black(), Color.white(), 1.2);
			MatrixTransformTexture3D circGradTrans = new MatrixTransformTexture3D(circGrad);
			circGradTrans.getTransform().scale(0.05);
			
			SimplexNoiseTexture3D hills = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.2), Color.gray(3.4));
			MatrixTransformTexture3D hillsTrans = new MatrixTransformTexture3D(hills);
			hillsTrans.getTransform().nonUniformScale(0.45, 0.45, 0.8);
			
			MultiplicativeT3DBlend multiHills = new MultiplicativeT3DBlend(circGradTrans, hillsTrans);
			
			AdditiveT3DBlend add3 = new AdditiveT3DBlend(multiHills, add2);
			
			
			MeshSurface mesh = base.tessellate(700);
			
			
			//Shift Y
			Vector4 multi = new Vector4();
			for(Triangle tri : mesh.getTriangles())
			{
				for(Vertex vert : tri.getVertices())
				{
					Vector4 pos = vert.getPosition();
					Color noise = add3.evaluate(pos.get(0), pos.get(1), pos.get(2));
					multi.set(0, noise.intensity3(), 0, 0);
					vert.setPosition(pos.add3(multi));
				}
				tri.generateFaceNormal();
				tri.setDynamic(true);
				tri.updateBoundingBox();
				tri.setDynamic(false);
			}
			

			mesh.smoothNormals();
			
			
			
			CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
			
			
			
			
			cube.updateBoundingBox();
			cube.setDynamic(false);
			
			{
				GradientTexture3D gradient = new GradientTexture3D(Color.black(), Color.white(), 1.0);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(4.0);
				

				SimplexNoiseTexture3D simplex4 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans4 = new MatrixTransformTexture3D(simplex4);
				simplexTrans4.getTransform().nonUniformScale(27, 27, 29);

				SimplexNoiseTexture3D simplex5 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans5 = new MatrixTransformTexture3D(simplex5);
				simplexTrans5.getTransform().nonUniformScale(73, 71, 73);//0604 was 63, 61, 62

				SimplexNoiseTexture3D simplex6 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans6 = new MatrixTransformTexture3D(simplex6);
				simplexTrans6.getTransform().nonUniformScale(37, 21, 33);
				
				AdditiveT3DBlend add4 = new AdditiveT3DBlend(simplexTrans4, simplexTrans5);
				SubtractiveT3DBlend sub1 = new SubtractiveT3DBlend(simplexTrans6, simplexTrans5);
				
				OneMinusT3DBlend one1 = new OneMinusT3DBlend(sub1);
				
				AdditiveT3DBlend multi1 = new AdditiveT3DBlend(sub1, add4);

				//MaskT3DBlend mask1 = new MaskT3DBlend(add3, sub1, multi1);
				MaskT3DBlend mask1 = new MaskT3DBlend(
						//new ColorTexture3D(new Color(0.5, 0.5, 1.0)), 
						//new ColorTexture3D(new Color(1.0, 0.5, 0.9)), 
						new ColorTexture3D(new Color(0.05, 0.05, 0.05)), 
						new ColorTexture3D(new Color(0.25, 0.15, 0.05)), 
						multi1);
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(0, 0, 0);
				inst.getTransform().scale(2.0);
				inst.setMaterial(addFrost(new FresnelDiffusePTMaterial(mask1, 0.86, 2.0), 0.5, 1.0));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
				//Material groundMat = addFrost(new FresnelDiffusePTMaterial(mask1, 0.86, 2.0), 0.5, 1.0);
				//for(Triangle tri : mesh.getTriangles())
				//{
				//	tri.setMaterial(groundMat);
				//	globalTriangles.add(tri);
				//}
				
				MeshSurface.removeMeshReference(mesh.getMeshID());
			}
		}
		
		
		
		//Water?
		{
			Grid base = new Grid(20.0, 20.0);
			
			SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.01), Color.gray(0.01));
			MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
			simplexTrans.getTransform().nonUniformScale(10.0, 10.0, 10.0);

			SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0), Color.gray(0.001));
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
			
			
			mesh.smoothNormals();
			
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
				inst.setMaterial(new DielectricPTMaterial(new Color(1.02, 1.018, 1.015), 1.91, 0.1));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));

				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
				
				MeshSurface.removeMeshReference(mesh.getMeshID());
			}
		}
	}
	
	private Material addFrost(Material base, double temperature, double scale)
	{
		WorleyNoiseTexture3D worleyTex1 = new WorleyNoiseTexture3D(seedGen.nextLong());
		worleyTex1.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
		worleyTex1.getNoiseFunction().setSelectionFunction(new SelectDifferenceNthMthNearest2D(7,1));
		
		MatrixTransformTexture3D worleyTex1Trans = new MatrixTransformTexture3D(worleyTex1);
		worleyTex1Trans.getTransform().scale(32);
		
		WorleyNoiseTexture3D worleyTex2 = new WorleyNoiseTexture3D(seedGen.nextLong());
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
		trans2.getTransform().scale(scale);
	
		Material frostedMat = new PosterMaskBBlend(
				new DiffusePTMaterial(new OneMinusT3DBlend(new MultiplicativeT3DBlend(trans2, trans2))),
				base,
				trans2,
				temperature
			);
		
		return frostedMat;
	}
	
	private void setupRocks()
	{
		{
			double r = 3.0;
			double rrange = 5.0;
			double rmin = 4.0;
			double rmax = rmin + rrange;
			double rratio;
			
			GradientTexture3D gradient = new GradientTexture3D(
					new Color(0xefefefff), 
					(new Color(0x111111ff)), 
					1.0
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			Material rockMat = new FresnelDiffusePTMaterial(gradientTrans, 1.0, 1.8);
			
			for(int i = 0; i < 16; i++)
			{
				r = rmin + rrange * random.nextDouble();
				rratio = r / rmax;
				Cube base = new Cube(1.5 + random.nextDouble()-0.5, 1 + random.nextDouble()-0.5, 1.5 + random.nextDouble()-0.5);
				Instance rock = makeVerticalRock(
						base.tessellate(80),
						rockMat, i >= 10 ? true : false);
				rock.getTransform().translate(
						r * Math.cos(2.0*Math.PI*random.nextDouble()), 
						0.5, 
						r * Math.sin(2.0*Math.PI*random.nextDouble()) - 2);
				rock.getTransform().rotateY(random.nextDouble() * Math.PI * 2.0);
				rock.getTransform().rotateX(random.nextDouble()*0.2);
				rock.getTransform().scale(0.5 + rratio * rratio);
				
				if(i >= 10)
				{
					rock.getTransform().rotateY(Math.PI);
					rock.getTransform().rotateX(Math.PI/ 12.0);
					rock.getTransform().translate(0, -0.8, 0);
				}
				
				rock.setDynamic(true);
				rock.updateBoundingBox();
				rock.bake(null);
				rock.setDynamic(false);
				this.addChild(rock);
			}
		}
	}
	
	private void setupMountainLine()
	{
		/*
		{
			double r = 3.0;
			double rrange = 5.0;
			double rmin = 4.0;
			
			GradientTexture3D gradient = new GradientTexture3D(
					new Color(0xefefefff), 
					(new Color(0x111111ff)), 
					1.0
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			Material rockMat = new FresnelDiffusePTMaterial(gradientTrans, 1.0, 1.8);
			
			for(int i = 0; i < 7; i++)
			{
				r = rmin + rrange * random.nextDouble();

				Sphere base = new Sphere(100 * (1 + random.nextDouble()-0.5));
				Instance rock = makeVerticalRock(
						base.tessellate(20),
						rockMat);
				rock.getTransform().translate(
						r * Math.cos(2.0*Math.PI*random.nextDouble()), 
						0.5, 
						r * Math.sin(2.0*Math.PI*random.nextDouble()));
				rock.getTransform().rotateY(random.nextDouble() * Math.PI * 2.0);
				rock.getTransform().rotateX(random.nextDouble()*0.2);
				rock.setDynamic(true);
				rock.updateBoundingBox();
				rock.bake(null);
				rock.setDynamic(false);
				this.addChild(rock);
			}
		}
		*/
		{
			Grid base = new Grid(1000.0, 1000.0);
			/*
			SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.004), Color.gray(0.12));
			MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
			simplexTrans.getTransform().nonUniformScale(10.0, 10.0, 10.0);
	
			SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0), Color.gray(0.001));
			//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
			MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
			simplexTrans2.getTransform().nonUniformScale(31.0, 31.0, 31.0);
			*/
			SimplexNoiseTexture3D simplex3 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(8.2), Color.gray(20.8));
			MatrixTransformTexture3D simplexTrans3 = new MatrixTransformTexture3D(simplex3);
			simplexTrans3.getTransform().nonUniformScale(0.15, 0.15, 0.2);

			
			//AdditiveT3DBlend add1 = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
			//AdditiveT3DBlend add2 = new AdditiveT3DBlend(add1, simplexTrans3);
			
			
			
			//Radial raise
			CircularGradientTexture3D circGrad = new CircularGradientTexture3D(Color.black(), Color.white(), 1.5);
			MatrixTransformTexture3D circGradTrans = new MatrixTransformTexture3D(circGrad);
			circGradTrans.getTransform().scale(0.02);
			
			SimplexNoiseTexture3D hills = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-3), Color.gray(10.8));
			MatrixTransformTexture3D hillsTrans = new MatrixTransformTexture3D(hills);
			hillsTrans.getTransform().nonUniformScale(0.45, 0.45, 0.8);

			AdditiveT3DBlend add3 = new AdditiveT3DBlend(hillsTrans, simplexTrans3);
			
			

			MatrixTransformTexture3D add3Trans = new MatrixTransformTexture3D(add3);
			add3Trans.getTransform().scale(0.2);
			
			MultiplicativeT3DBlend multiHills = new MultiplicativeT3DBlend(circGradTrans, add3Trans);
			
			
			MeshSurface mesh = base.tessellate(300);
			
			
			//Shift Y
			Vector4 multi = new Vector4();
			for(Triangle tri : mesh.getTriangles())
			{
				for(Vertex vert : tri.getVertices())
				{
					Vector4 pos = vert.getPosition();
					Color noise = multiHills.evaluate(pos.get(0), pos.get(1), pos.get(2));
					multi.set(0, noise.intensity3() + 2.0 * (pos.distance(new Vector4())/100.0), 0, 0);
					vert.setPosition(pos.add3(multi));
				}
				tri.generateFaceNormal();
				tri.setDynamic(true);
				tri.updateBoundingBox();
				tri.setDynamic(false);
			}
			

			//mesh.smoothNormals();
			CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
			
			
			
			
			cube.updateBoundingBox();
			cube.setDynamic(false);
			
			{
				GradientTexture3D gradient = new GradientTexture3D(Color.black(), Color.white(), 1.0);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(4.0);
				

				SimplexNoiseTexture3D simplex4 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans4 = new MatrixTransformTexture3D(simplex4);
				simplexTrans4.getTransform().nonUniformScale(27, 27, 29);

				SimplexNoiseTexture3D simplex5 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans5 = new MatrixTransformTexture3D(simplex5);
				simplexTrans5.getTransform().nonUniformScale(63, 61, 63);

				SimplexNoiseTexture3D simplex6 = new SimplexNoiseTexture3D(seedGen.nextLong());
				MatrixTransformTexture3D simplexTrans6 = new MatrixTransformTexture3D(simplex6);
				simplexTrans6.getTransform().nonUniformScale(37, 21, 33);
				
				AdditiveT3DBlend add4 = new AdditiveT3DBlend(simplexTrans4, simplexTrans5);
				SubtractiveT3DBlend sub1 = new SubtractiveT3DBlend(simplexTrans6, simplexTrans5);
				
				OneMinusT3DBlend one1 = new OneMinusT3DBlend(sub1);
				
				AdditiveT3DBlend multi1 = new AdditiveT3DBlend(sub1, add4);

				//MaskT3DBlend mask1 = new MaskT3DBlend(add3, sub1, multi1);
				MaskT3DBlend mask1 = new MaskT3DBlend(
						//new ColorTexture3D(new Color(0.5, 0.5, 1.0)), 
						//new ColorTexture3D(new Color(1.0, 0.5, 0.9)), 
						//new ColorTexture3D(new Color(0.92, 0.92, 0.92)), 
						//new ColorTexture3D(new Color(0.90, 0.87, 0.61)), 
						new ColorTexture3D(new Color(0.92, 0.92, 0.92)), 
						new ColorTexture3D(new Color(0.80, 0.80, 0.81)), 
						multi1);
				
				MaskT3DBlend mask2 = new MaskT3DBlend(
						new ColorTexture3D(new Color(0.22, 0.22, 0.22)), 
						new ColorTexture3D(new Color(0.10, 0.07, 0.03)), 
						multi1);
				
				GradientTexture3D grad2 = new GradientTexture3D();
				MatrixTransformTexture3D grad2Trans = new MatrixTransformTexture3D(grad2);
				grad2Trans.getTransform().scale(0.07);
				grad2Trans.getTransform().translate(0, -0.9, 0);
				
				MaskT3DBlend mask3 = new MaskT3DBlend(
						mask2, 
						mask1, 
						grad2Trans);
				
				
				Material frostline = addFrost(new FresnelDiffusePTMaterial(mask3, 0.86, 2.0), 0.4, 0.3);
				
				
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(0, -65, 0);
				inst.getTransform().scale(8.0);
				inst.setMaterial(frostline);
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
		}
	}
	
	private Instance makeVerticalRock(MeshSurface mesh, Material material, boolean smooth)
	{
		

		WorleyNoiseTexture3D worleyTex1 = new WorleyNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.4), Color.gray(0.8));
		worleyTex1.getNoiseFunction().setDistanceFunction(new TchebyshevDistance3D());
		worleyTex1.getNoiseFunction().setSelectionFunction(new SelectDifferenceNthMthNearest2D(7,1));
		
		MatrixTransformTexture3D worleyTex1Trans = new MatrixTransformTexture3D(worleyTex1);
		worleyTex1Trans.getTransform().scale(1.5);//8 has a cool shard look
		worleyTex1Trans.getTransform().translate(1.0, 1.0, 1.0);

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.00004), Color.gray(0.00004));
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(16.0, 16.0, 64.0);
		
		//MeshSurface mesh = base.tessellate(100);
		//mesh.synchronizeVertices();
		//ArrayList<Triangle> triangles =;
		
		Vector4 multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = worleyTex1Trans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.2 + noise.intensity3(), 1.0 + noise.intensity3(), 0);
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
		
		if(smooth)
			mesh.smoothNormals();
		
		CompositeSurface cube = AABVHSurface.makeAABVH(mesh.getTriangles());
		
		
		cube.updateBoundingBox();
		cube.setDynamic(false);
	
	
		/*
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				new Color(0xff0068ff), 
				new Color(0xffff44ff), 
				0.8
				);
				*/
		/*
		GradientTexture3D gradient = new GradientTexture3D(
				new Color(0xefefefff), 
				(new Color(0x111111ff)), 
				1.0
				);
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
		gradientTrans.getTransform().scale(1.0);
		*/
		
		Instance inst = new Instance();
		inst.addChild(cube);
		
		//inst.getTransform().translate(0, 2, 0);
		//inst.getTransform().nonUniformScale(0.5, 1.5, 0.5);
		//inst.setMaterial(new DiffusePTMaterial(gradientTrans));
		//inst.setMaterial(new FresnelDiffusePTMaterial(gradientTrans, 1.0, 1.8));
		//inst.setMaterial(new DiffuseMaterial(Color.white()));
		inst.setMaterial(material);

		inst.updateBoundingBox();
		inst.bake(null);
		inst.setDynamic(false);
	
		return inst;
	}
	
	private void setupCenterPiece()
	{
		//Cube base = new Cube(1.0, 1.0, 1.0);
		Sphere base = new Sphere();
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.6), Color.gray(0.8));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(2.0, 8.0, 2.0);

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.004), Color.gray(0.004));
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
		//mesh.smoothNormals();
		
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
			/*
			SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
					(new Color(0xff1178ff)).multiply3M(1.05), //was 1.05 when there was no sss
					new Color(0xefefefff), 
					0.8
					);
			SphericalGradientTexture3D gradientInv = new SphericalGradientTexture3D(
					Color.white().subtract3M((new Color(0xff1178ff)).multiply3M(1.15)).add3M(Color.white()), //was 1.05
					Color.white().subtract3M(new Color(0xefefefff)).add3M(Color.white()).multiply3M(0.9), 
					0.8
					);
			MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
			gradientTrans.getTransform().scale(1.0);
			
			DiffusePTMaterial surfaceColor = new DiffusePTMaterial(gradientTrans);
			Material ssmat = new SubSurfaceDiffusePTTestMaterial(surfaceColor, 
					new ColorTexture3D(Color.white()), 
					0.25, //scatter coeff 
					1.0, //refractive index
					1.0, //roughness
					10);
			
			FastIntensityMaskBBlend intMask = new FastIntensityMaskBBlend(
					ssmat, 
					surfaceColor, 
					new SphericalGradientTexture3D(Color.black(), Color.white(),1.5));
			*/
			{
				SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
						(new Color(0xff1178ff)).multiply3M(1.05), //was 1.05 when there was no sss
						new Color(0xefefefff), 
						0.8
						);
				SphericalGradientTexture3D gradientInv = new SphericalGradientTexture3D(
						Color.white().subtract3M((new Color(0xff1178ff)).multiply3M(1.15)).add3M(Color.white()), //was 1.05
						Color.white().subtract3M(new Color(0xefefefff)).add3M(Color.white()).multiply3M(0.9), 
						0.8
						);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(1.0);
				
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(0, 2.5, -2);
				inst.getTransform().scale(2.7);
				//inst.setMaterial(intMask);
				inst.setMaterial(new DiffusePTMaterial(gradientTrans));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
			
			{
				SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
						(new Color(0xff1178ff)).multiply3M(0.75), //was 1.05 when there was no sss
						new Color(0xefefefff), 
						0.8
						);
				SphericalGradientTexture3D gradientInv = new SphericalGradientTexture3D(
						Color.white().subtract3M((new Color(0xff1178ff)).multiply3M(1.15)).add3M(Color.white()), //was 1.05
						Color.white().subtract3M(new Color(0xefefefff)).add3M(Color.white()).multiply3M(0.9), 
						0.8
						);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(1.0);
				
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(8, 1.5, -8);
				inst.getTransform().scale(1.7);
				inst.getTransform().rotateY(Math.PI * 0.75);
				//inst.setMaterial(intMask);
				inst.setMaterial(new DiffusePTMaterial(gradientTrans));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
			
			{
				SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
						(new Color(0xff1178ff)).multiply3M(0.75), //was 1.05 when there was no sss
						new Color(0xefefefff), 
						0.8
						);
				SphericalGradientTexture3D gradientInv = new SphericalGradientTexture3D(
						Color.white().subtract3M((new Color(0xff1178ff)).multiply3M(1.15)).add3M(Color.white()), //was 1.05
						Color.white().subtract3M(new Color(0xefefefff)).add3M(Color.white()).multiply3M(0.9), 
						0.8
						);
				MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
				gradientTrans.getTransform().scale(1.0);
				
				
				Instance inst = new Instance();
				inst.addChild(cube);
				
				inst.getTransform().translate(-9, 1.5, -9);
				inst.getTransform().scale(0.9);
				inst.getTransform().rotateY(Math.PI * 0.75);
				//inst.setMaterial(intMask);
				inst.setMaterial(new DiffusePTMaterial(gradientTrans));
				//inst.setMaterial(new DiffuseMaterial(Color.white()));
	
				inst.updateBoundingBox();
				inst.bake(null);
				inst.setDynamic(false);
				this.addChild(inst);
			}
		}
		
		//AABVHSurface aabvh = AABVHSurface.makeAABVH(cubes, 1, 4);
		//Add all spheres at once
		//this.addChild(aabvh);
	}
	
	private void setupGroundFruit()
	{
		MeshSurface mesh = (new Sphere(0.5, new Vector4())).tessellate(150);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.8), Color.gray(1.0));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(12.0, 12.0, 12.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.00), Color.gray(-0.08));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(64.0, 64.0, 64.0);
		
		Vector4 multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 0);
				Vector4 newPos = pos.multiply3(multi);
				vert.setPosition(newPos.add3M(
						new Vector4(0, Math.pow((newPos.get(0) * newPos.get(0) + newPos.get(2) * newPos.get(2)), 2.0) * 0.5, 0, 0))
						);
			}
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		//Add mode noise!
		multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans2.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 0);
				vert.setPosition(pos.multiply3(multi));
			}
		}
		for(Triangle tri : mesh.getTriangles())
		{
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}

		//mesh.tessellateMeshByTriangleLongestSideConstraint(0.05);
		mesh.smoothNormals();
		CompositeSurface accelerated = AABVHSurface.makeAABVH(mesh.getTriangles());
		
		
		
		
		accelerated.updateBoundingBox();
		accelerated.setDynamic(false);
		
		/*
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				(new Color(0xff0068ff)).multiply3M(0.9), 
				(new Color(0xf8f840ff)).multiply3M(0.94), 
				0.5
				);
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
		gradientTrans.getTransform().scale(1.0);
		*/
		
		
		
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				(new Color(0xff0068ff)),//.mixWithWhite(0.5, 0.5), 
				(new Color(0xf8f840ff)),//.mixWithWhite(0.5, 0.5), 
				1.0
				);
		SphericalGradientTexture3D beer = new SphericalGradientTexture3D(
				new Color(0.9, 200.6, 80.0), 
				new Color(0.9, 3.2, 1.83), 
				//(new Color(2.2, 1.63, 0.9)).mixWithWhite(0.9, 0.1), 
				//new Color(0.95, 0.95, 0.95), 
				300000.0
				);
		Material ssmat = new SubSurfaceDiffusePTTestMaterial(new DiffusePTMaterial(gradient), 
				beer, 
				0.97, //scatter coeff 
				1.0, //refractive index
				1.0, //roughness
				3);
		
		
		{
			Instance instance = new Instance();
			instance.addChild(accelerated);
			//instance.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.8), 0.9, 2.0));//
			//instance.setMaterial(new AshikhminPTMaterial(gradientTrans, Color.gray(1.0), 0.3, 0.7, 0, 0));//
			//instance.setMaterial(new DiffusePTMaterial(gradientTrans));//
			instance.setMaterial(ssmat);//
			instance.getTransform().rotateY(Math.PI/4.0);
			instance.getTransform().scale(2.0);
			instance.getTransform().translate(4.8, 0, -1);
			instance.bake(null);
			instance.updateBoundingBox();
			
			//Add fruit
			this.addChild(instance);

			{
				PointLight plight = new PointLight();
				plight.setColor(new Color(0xffeeddff));
				plight.setIntensity(3.0);
				plight.setPosition(new Vector4(3, 0.8, -1, 0));
				//lightManager.addLight(plight);
			}
		}
		
		{
			Instance instance = new Instance();
			instance.addChild(accelerated);
			//instance.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.8), 0.9, 2.0));//
			//instance.setMaterial(new AshikhminPTMaterial(gradientTrans, Color.gray(1.0), 0.3, 0.7, 0, 0));//
			instance.setMaterial(ssmat);//
			instance.getTransform().rotateY(Math.PI * (2.0/3.0));
			instance.getTransform().scale(0.9);
			instance.getTransform().translate(2.9, 0.4, 2.2);
			instance.bake(null);
			instance.updateBoundingBox();
			
			//Add fruit
			this.addChild(instance);
			
		}

		{
			Instance instance = new Instance();
			instance.addChild(accelerated);
			//instance.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.8), 0.9, 2.0));//
			//instance.setMaterial(new AshikhminPTMaterial(gradientTrans, Color.gray(1.0), 0.3, 0.7, 0, 0));//
			instance.setMaterial(ssmat);//
			instance.getTransform().rotateY(Math.PI * (2.0/3.0));
			instance.getTransform().scale(0.4);
			instance.getTransform().translate(1.5, 0.4, 2.1);
			instance.bake(null);
			instance.updateBoundingBox();
			
			//Add fruit
			this.addChild(instance);
			
		}
		
	}
	
	private void setupGridLines()
	{
		for(int x = -5; x <= 5; x++)
		{
			for(int z = -5; z <= 5; z++)
			{
				Cylinder cyl = new Cylinder(10.0, 0.03);
				cyl.setMaterial(new ColorMaterial(Color.black()));
				
				MatrixTransformSurface trans = new MatrixTransformSurface();
				trans.addChild(cyl);
				trans.getTransform().translate(x*2, 0, z*2);

				trans.updateBoundingBox();
				trans.bake(null);
				trans.setDynamic(false);
				this.addChild(trans);
			}
		}
	}
	
	
	private void setupKoi()
	{
		Instance koi = makeKoiObject();
		Material koiMat = koi.getMaterial();
		
		{
			Instance instance = new Instance();
			instance.addChild(koi);
			instance.setMaterial(koiMat);
			instance.getTransform().scale(0.18);
			instance.getTransform().translate(-2.0, -0.34, 0.8);
			
			instance.setDynamic(true);
			instance.updateBoundingBox();
			instance.bake(null);
			instance.setDynamic(false);
			
			this.addChild(instance);
		}
		
		{
			Instance instance = new Instance();
			instance.addChild(koi);
			instance.setMaterial(koiMat);
			instance.getTransform().scale(0.15);
			instance.getTransform().translate(-2.4, -0.28, 0.35);
			
			instance.setDynamic(true);
			instance.updateBoundingBox();
			instance.bake(null);
			instance.setDynamic(false);
			
			this.addChild(instance);
		}
		
		{
			Instance instance = new Instance();
			instance.addChild(koi);
			instance.setMaterial(koiMat);
			instance.getTransform().scale(0.12);
			instance.getTransform().translate(-2.0, -0.20, 0.05);
			
			instance.setDynamic(true);
			instance.updateBoundingBox();
			instance.bake(null);
			instance.setDynamic(false);
			
			this.addChild(instance);
		}
		
		{
			Instance instance = new Instance();
			instance.addChild(koi);
			instance.setMaterial(koiMat);
			instance.getTransform().scale(0.18);
			instance.getTransform().translate(-2.5, -0.30, -1.8);
			instance.getTransform().rotateY(Math.PI/2.0);
			
			instance.setDynamic(true);
			instance.updateBoundingBox();
			instance.bake(null);
			instance.setDynamic(false);
			
			this.addChild(instance);
		}
	}
	
	private Instance makeKoiObject()
	{
		MeshSurface mesh = (new Sphere()).tessellate(20);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.2), Color.gray(0.2));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(1.0, 1.0, 1.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.05), Color.gray(0.05));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(1.2, 1.2, 1.2);
		
		Vector4 multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 0);
				Vector4 newPos = pos.multiply3(multi);
				vert.setPosition(newPos);
			}
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		//Add mode noise!
		multi = new Vector4();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector4 pos = vert.getPosition();
				Color noise = simplexTrans2.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 0);
				vert.setPosition(pos.multiply3(multi));
			}
		}
		
		for(Triangle tri : mesh.getTriangles())
		{
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}

		//mesh.tessellateMeshByTriangleLongestSideConstraint(0.05);
		mesh.smoothNormals();
		CompositeSurface accelerated = AABVHSurface.makeAABVH(mesh.getTriangles());
		
		
		accelerated.updateBoundingBox();
		//accelerated.setDynamic(false);
		
		
		SphericalSineWaveTexture3D sine3D = new SphericalSineWaveTexture3D(
				(new Color(0xfefefeff)), 
				(new Color(0xff6800ff))
				);
		
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(sine3D);
		gradientTrans.getTransform().scale(32.0);
		gradientTrans.getTransform().translate(10, 0, 0);
		
		
		
		Instance instance = new Instance();
		instance.addChild(accelerated);
		//instance.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.8), 0.9, 2.0));//
		//instance.setMaterial(new AshikhminPTMaterial(gradientTrans, Color.gray(1.0), 0.3, 0.7, 0, 0));//
		instance.setMaterial(new DiffusePTMaterial(gradientTrans));//
		instance.getTransform().nonUniformScale(2.0, 1.0, 1.0);
		instance.getTransform().scale(1.0);
		
		instance.setDynamic(true);
		instance.updateBoundingBox();
		instance.bake(null);
		instance.setDynamic(false);
		
		return instance;
	}
	

	
	private void setupSnowParticles(BoundingBox volume, double particlesPerUnit, double particleSize, double particleSizeVariation)
	{
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0.5), Color.gray(0.6));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().scale(89.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0.55), Color.gray(0.6));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().scale(96.0);
		
		AdditiveT3DBlend addTex = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
		
		ColorMaterial diffmat = new ColorMaterial(addTex);
		PassThroughMaterial pmat = new PassThroughMaterial(Color.white(), 1.02);
		
		FastIntensityMaskBBlend maskmat = new FastIntensityMaskBBlend(diffmat, pmat, Color.gray(0.9));
		
		
		double volumeDelta = 1.0 / particlesPerUnit;

		for(double x = volume.min.get(0); x < volume.max.get(0); x+=volumeDelta)
		{
			for(double y = volume.min.get(1); y < volume.max.get(1); y+=volumeDelta)
			{
				for(double z = volume.min.get(2); z < volume.max.get(2); z+=volumeDelta)
				{
					Vector4 position = stratifiedVector(x, y, z, volumeDelta);
					
					
					Sphere particle = new Sphere(random.nextDouble() * particleSizeVariation + particleSize, position);
					particle.setMaterial(maskmat);
					this.addChild(particle);
				}
			}
		}
		
	}
	
	private Vector4 stratifiedVector(double xmin, double ymin, double zmin, double range)
	{
		return new Vector4(
				xmin + random.nextDouble() * range,
				ymin + random.nextDouble() * range,
				zmin + random.nextDouble() * range,
				0);
	}
	
	
	
	private void setupGrass()
	{
		this.addChild(grassPatch(new Color(0xffffaaff), 
				new Color(0x885522ff), 
				6.0, 1.0, 
				0.05, 1.2,
				300, 
				new Vector4(3.0, 0.2, 0, 0), 
				0.5, 1.9, 1.5, 
				0.001)
			);
		
		//
		//instance.getTransform().translate();
		this.addChild(grassPatch(new Color(0xffffaaff), 
				new Color(0x885522ff), 
				0.3, 1.0, 
				0.02, 1.2,
				30, 
				new Vector4(5.6, 0, -0.6, 0), 
				3.0, 1.9, 1.0, 
				0.001)
			);
	}
	
	private Instance grassPatch(
	
		Color color1,
		Color color2,
		
		double baseDistRad,
		double baseDistPower,
		double topDistRad,
		double topDistPower,
		
		int count,
		Vector4 basePos,
		
		double bladeHeightMin,
		double bladeHeightVariation,
		double bladeHeightPower,
		
		double middleOffset
		)
	{

		Vector4 thisBase;
		double distFromBase;
		double bladeHeight = 0.0;
		
		LinkedList<Triangle> grassTriangles = new LinkedList<Triangle>();
		MeshSurface tempMesh = new MeshSurface();;
		
		for(int i = 0; i < count; ++i)
		{
			thisBase = basePos.add3(new Vector4(
					Math.pow(random.nextDouble(), baseDistPower) * baseDistRad * randSign(0.5), 
					0, 
					Math.pow(random.nextDouble(), baseDistPower) * baseDistRad * randSign(0.5), 
					0));
			
			distFromBase = thisBase.distance(basePos);
			
			bladeHeight = (bladeHeightMin + random.nextDouble() * bladeHeightVariation) / Math.pow(1.0 + distFromBase, bladeHeightPower);
			
			Spline spline = new Spline();
			spline.add(thisBase);
			
			spline.add(thisBase.add3(new Vector4(
					randInRange(-middleOffset, middleOffset),
					bladeHeight/2.0,
					randInRange(-middleOffset, middleOffset),
					0)));
			
			spline.add(thisBase.add3(new Vector4(
					Math.pow(random.nextDouble(), topDistPower) * topDistRad * randSign(0.5), 
					bladeHeight, 
					Math.pow(random.nextDouble(), topDistPower) * topDistRad * randSign(0.5), 
					0)));
			
			
			tempMesh.setTriangles(spline.tessellate(8, 3, 0.008, 0.002));
			tempMesh.smoothNormals();
			tempMesh.setMaterial(new DiffusePTMaterial(color1.interpolate(color1, color2, random.nextDouble())));
			
			grassTriangles.addAll(tempMesh.getTriangles());
			
			
		}
		
		AABVHSurface accel = AABVHSurface.makeAABVH(grassTriangles);
		
		Instance inst = new Instance();
		inst.addChild(accel);
		
		return inst;
	}
	
	private double randSign(double percentFlip)
	{
		return random.nextDouble() < percentFlip ? -1.0 : 1.0;
	}
	
}
