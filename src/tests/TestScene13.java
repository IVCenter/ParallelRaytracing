package tests;

import java.util.LinkedList;
import java.util.Random;

import math.Spline;
import math.Vector3;
import process.logging.Logger;
import raytrace.bounding.BoundingBox;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.ProgrammableCameraController;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Sphere;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.DirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.SphericalSineWaveTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.PassThroughMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.blend.binary.FastIntensityMaskBBlend;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene13 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	Random seedGen;
	Random random;
	
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
		seedGen = new Random(0x07b56c7168a2dL);
		random = new Random(0x07b56c7168a2dL);
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);

		//skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		skyMaterial = new RecursionMinimumCMaterial(new ColorMaterial(new Color(0xddeeffff)), 1);
		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffddeeff), new Color(0xddeeffff))), 1
				);
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(4);
		activeCamera.setPosition(new Vector3(0,2.5,5));
		activeCamera.setViewingDirection(new Vector3(0.1,-0.15,-1));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.02, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(4.5);
		
		
		
		camController = new ProgrammableCameraController((ProgrammableCamera)activeCamera);
		
		//Position Spline
		{
			Spline spline = new Spline();
			spline.add(new Vector3(0.0, 0.5, 5.0));
			spline.add(new Vector3(-5.0, 0.5, 5.0));
			spline.add(new Vector3(-5.0, 0.5, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector3(-5.0, 0.5, 0.0));
			spline.add(new Vector3(-5.0, 0.5, -5.0));
			spline.add(new Vector3(0.0, 0.5, -5.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector3(0.0, 0.5, -5.0));
			spline.add(new Vector3(5.0, 0.5, -5.0));
			spline.add(new Vector3(5.0, 0.5, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector3(5.0, 0.5, 0.0));
			spline.add(new Vector3(5.0, 0.5, 5.0));
			spline.add(new Vector3(0.0, 0.5, 5.0));
			camController.addPositionSpline(spline, 2.5);
		}
		
		//Look At
		{
			Spline spline = new Spline();
			spline.add(new Vector3(0.0, 0.0, 0.0));
			spline.add(new Vector3(0.0, 0.0, 0.0));
			camController.addLookAtSpline(spline, 10.0);
		}
		
		//Camera Radius
		{
			Spline spline = new Spline();
			spline.add(new Vector3(0.002, 0.0, 0.0));
			spline.add(new Vector3(0.002, 0.0, 0.0));
			camController.addApertureRadiusSpline(spline, 10.0);
		}
		
		//Focal Distance
		{
			Spline spline = new Spline();
			spline.add(new Vector3(4.5, 0.0, 0.0));
			spline.add(new Vector3(5.0, 0.0, 0.0));
			spline.add(new Vector3(4.0, 0.0, 0.0));
			spline.add(new Vector3(4.5, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 10.0);
		}
		
		//Field of View
		{
			Spline spline = new Spline();
			spline.add(new Vector3(Math.PI/2.0, 0.0, 0.0));
			spline.add(new Vector3(Math.PI/2.0, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 10.0);
		}
		

		//If this node is a middle tier, we dont need any data at all
		if(!Configuration.isClock() && !Configuration.isLeaf())
			return;
		
		//If this nose is a controller, we don't need any more loaded
		if(Configuration.isController() && !Configuration.isLeaf())
			return;
		
		
		
		//Setup scene geometry
		
		//Make koi object
		//makeKoiObject();
		
		
		//Make snow particles
		//BoundingBox bb = new BoundingBox(new Vector4(-1, -1, -1, 0), new Vector4(1, 1, 1, 0));
		//setupSnowParticles(bb, 4, 0.02, 0.01);
		
		/*
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0, 0, 0, 0));
			spline.add(new Vector4(-1, 0, 0, 0));
			spline.add(new Vector4(-1, 0.5, 0, 0));
			spline.add(new Vector4(0, 0.5, 0, 0));
			spline.add(new Vector4(1, 0.5, 0, 0));
			spline.add(new Vector4(1, 1, 0, 0));
			spline.add(new Vector4(0, 1, 0, 0));
			spline.add(new Vector4(-1, 1, 0, 0));
			
			AABVHSurface accel = AABVHSurface.makeAABVH(spline.tessellate(30, 10, 0.2, 0.01));
			
			Instance inst = new Instance();
			inst.addChild(accel);
			inst.setMaterial(new DiffusePTMaterial(new Color(0xbbbbbbff)));
			
			this.addChild(inst);
		}
		*/
		Random random = new Random(0xf463a23bcd139L);

		Color color1 = new Color(0xffffaaff);
		Color color2 = new Color(0x885522ff);
		
		double baseDistRad = 0.6;
		double baseDistPower = 1.0;
		double topDistRad = 1.25;
		double topDistPower = 0.8;
		double distFromBase = 0;
		int count = 100;
		Vector3 basePos = new Vector3(0.0, 0.0, 0.0);
		Vector3 thisBase;
		
		double bladeHeightMin = 1.8;
		double bladeHeightVariation = 1.2;
		double bladeHeightPower = 1.0;
		double bladeHeight = 0.0;
		
		double middleOffset = 0.001;
		
		LinkedList<Triangle> grassTriangles = new LinkedList<Triangle>();
		MeshSurface tempMesh = new MeshSurface();;
		
		for(int i = 0; i < count; ++i)
		{
			thisBase = basePos.add(new Vector3(
					Math.pow(random.nextDouble(), baseDistPower) * baseDistRad * randSign(0.5), 
					0, 
					Math.pow(random.nextDouble(), baseDistPower) * baseDistRad * randSign(0.5)));
			
			distFromBase = thisBase.distance(basePos);
			
			bladeHeight = (bladeHeightMin + random.nextDouble() * bladeHeightVariation) / Math.pow(1.0 + distFromBase, bladeHeightPower);
			
			Spline spline = new Spline();
			spline.add(thisBase);
			
			spline.add(thisBase.add(new Vector3(
					randInRange(-middleOffset, middleOffset),
					bladeHeight/2.0,
					randInRange(-middleOffset, middleOffset))));
			
			spline.add(thisBase.add(new Vector3(
					Math.pow(random.nextDouble(), topDistPower) * topDistRad * randSign(0.5), 
					bladeHeight, 
					Math.pow(random.nextDouble(), topDistPower) * topDistRad * randSign(0.5))));
			
			
			tempMesh.setTriangles(spline.tessellate(10, 3, 0.03, 0.01));
			tempMesh.setMaterial(new DiffusePTMaterial(color1.interpolate(color1, color2, random.nextDouble())));
			
			grassTriangles.addAll(tempMesh.getTriangles());
			
			
		}
		
		AABVHSurface accel = AABVHSurface.makeAABVH(grassTriangles);
		
		Instance inst = new Instance();
		inst.addChild(accel);
		//inst.setMaterial(new DiffusePTMaterial(color1.interpolate(color1, color2, random.nextDouble())));
		
		this.addChild(inst);
		
		
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector3(1,-1,-1));
		lightManager.addLight(directionalLight);
		
		
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		
		AABVHSurface aabvh2 = AABVHSurface.makeAABVH(this.getChildren(), 1, 2);
		this.getChildren().clear();
		this.addChild(aabvh2);
		
		//Refresh
		this.updateBoundingBox();
		
	}

	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	private double randSign(double percentFlip)
	{
		return random.nextDouble() < percentFlip ? -1.0 : 1.0;
	}
	
	
	
	private void makeKoiObject()
	{
		MeshSurface mesh = (new Sphere()).tessellate(20);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.2), Color.gray(0.2));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(1.0, 1.0, 1.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.05), Color.gray(0.05));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(1.2, 1.2, 1.2);
		
		Vector3 multi = new Vector3();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector3 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3());
				Vector3 newPos = pos.multiply(multi);
				vert.setPosition(newPos);
			}
			tri.generateFaceNormal();
			tri.setDynamic(true);
			tri.updateBoundingBox();
			tri.setDynamic(false);
		}
		
		//Add mode noise!
		multi = new Vector3();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector3 pos = vert.getPosition();
				Color noise = simplexTrans2.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3());
				vert.setPosition(pos.multiply(multi));
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
		
		this.addChild(instance);
	}
	
	private void setupSnowParticles(BoundingBox volume, double particlesPerUnit, double particleSize, double particleSizeVariation)
	{
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0.4), Color.gray(0.6));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().scale(89.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(0.45), Color.gray(0.6));
		//SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(0.1), Color.gray(1.0));//Used for visualizing
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().scale(96.0);
		
		AdditiveT3DBlend addTex = new AdditiveT3DBlend(simplexTrans, simplexTrans2);
		
		DiffusePTMaterial diffmat = new DiffusePTMaterial(addTex);
		PassThroughMaterial pmat = new PassThroughMaterial(Color.white(), 1.02);
		
		FastIntensityMaskBBlend maskmat = new FastIntensityMaskBBlend(diffmat, pmat, Color.gray(0.8));
		
		
		double volumeDelta = 1.0 / particlesPerUnit;

		for(double x = volume.min.get(0); x < volume.max.get(0); x+=volumeDelta)
		{
			for(double y = volume.min.get(1); y < volume.max.get(1); y+=volumeDelta)
			{
				for(double z = volume.min.get(2); z < volume.max.get(2); z+=volumeDelta)
				{
					Vector3 position = stratifiedVector(x, y, z, volumeDelta);
					
					
					Sphere particle = new Sphere(random.nextDouble() * particleSizeVariation + particleSize, position);
					particle.setMaterial(maskmat);
					this.addChild(particle);
				}
			}
		}
		
	}
	
	private Vector3 stratifiedVector(double xmin, double ymin, double zmin, double range)
	{
		return new Vector3(
				xmin + random.nextDouble() * range,
				ymin + random.nextDouble() * range,
				zmin + random.nextDouble() * range);
	}
	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		camController.upate(0.5);
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		//this.updateBoundingBox();
		//super.bake(data);
	}
}