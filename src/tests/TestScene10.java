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
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.composite.NormalMapCMaterial;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene10 extends Scene
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
		//If this node is a middle tier, we dont need any data at all
		if(!Configuration.isClock() && !Configuration.isLeaf())
			return;
		
		//Configuration.setScreenWidth(800);
		//Configuration.setScreenHeight(600);

		//skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		skyMaterial = new RecursionMinimumCMaterial(new ColorMaterial(new Color(0xddeeffff)), 1);
		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffddeeff), new Color(0xddeeffff))), 1
				);
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(10);
		activeCamera.setPosition(new Vector4(0,2.5,5,0));
		activeCamera.setViewingDirection(new Vector4(0.1,-0.15,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.02, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(4.5);
		
		
		
		camController = new ProgrammableCameraController((ProgrammableCamera)activeCamera);
		
		//Position Spline
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 0.5, 5.0, 0.0));
			spline.add(new Vector4(-5.0, 0.5, 5.0, 0.0));
			spline.add(new Vector4(-5.0, 0.5, 0.0, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(-5.0, 0.5, 0.0, 0.0));
			spline.add(new Vector4(-5.0, 0.5, -5.0, 0.0));
			spline.add(new Vector4(0.0, 0.5, -5.0, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 0.5, -5.0, 0.0));
			spline.add(new Vector4(5.0, 0.5, -5.0, 0.0));
			spline.add(new Vector4(5.0, 0.5, 0.0, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		{
			Spline spline = new Spline();
			spline.add(new Vector4(5.0, 0.5, 0.0, 0.0));
			spline.add(new Vector4(5.0, 0.5, 5.0, 0.0));
			spline.add(new Vector4(0.0, 0.5, 5.0, 0.0));
			camController.addPositionSpline(spline, 2.5);
		}
		
		//Look At
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(0.0, 0.0, 0.0, 0.0));
			camController.addLookAtSpline(spline, 10.0);
		}
		
		//Camera Radius
		{
			Spline spline = new Spline();
			spline.add(new Vector4(0.002, 0.0, 0.0, 0.0));
			spline.add(new Vector4(0.002, 0.0, 0.0, 0.0));
			camController.addApertureRadiusSpline(spline, 10.0);
		}
		
		//Focal Distance
		{
			Spline spline = new Spline();
			spline.add(new Vector4(4.5, 0.0, 0.0, 0.0));
			spline.add(new Vector4(5.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(4.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(4.5, 0.0, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 10.0);
		}
		
		//Field of View
		{
			Spline spline = new Spline();
			spline.add(new Vector4(Math.PI/2.0, 0.0, 0.0, 0.0));
			spline.add(new Vector4(Math.PI/2.0, 0.0, 0.0, 0.0));
			camController.addFocalDistanceSpline(spline, 10.0);
		}
		
		
		//If this nose is a controller, we don't need any more loaded
		if(Configuration.isController())
			return;
		
		
		
		//Setup scene geometry
		
		MeshSurface mesh = (new Cube(1, 1, 1)).tessellate(100);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(Color.gray(-0.8), Color.gray(1.0));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(4.0, 4.0, 4.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(Color.gray(-0.00), Color.gray(-0.08));
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
		CompositeSurface accelerated = AABVHSurface.makeAABVH(mesh.getTriangles());
		
		
		
		
		accelerated.updateBoundingBox();
		accelerated.setDynamic(false);
		
		
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				new Color(0xff0068ff), 
				new Color(0xf8f840ff), 
				0.5
				);
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
		gradientTrans.getTransform().scale(1.0);
		
		
		
		Instance instance = new Instance();
		instance.addChild(accelerated);
		//instance.setMaterial(new FresnelDiffusePTMaterial(Color.gray(0.8), 0.9, 2.0));//
		//instance.setMaterial(new AshikhminPTMaterial(gradientTrans, Color.gray(1.0), 0.3, 0.7, 0, 0));//
		instance.setMaterial(new DiffusePTMaterial(gradientTrans));//
		instance.getTransform().rotateY(Math.PI/4.0);
		instance.getTransform().scale(3.0);
		instance.bake(null);
		instance.updateBoundingBox();
		
		this.addChild(instance);
		
		
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		
		//AABVHSurface aabvh2 = AABVHSurface.makeAABVH(this.getChildren(), 1, 2);
		//this.getChildren().clear();
		//this.addChild(aabvh2);
		
		//Refresh
		this.updateBoundingBox();
		
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
