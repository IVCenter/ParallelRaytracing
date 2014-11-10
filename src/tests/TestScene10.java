package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import process.logging.Logger;

import math.Spline;
import math.Vector3;
import math.ray.CircularRayStencil;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.ProgrammableCameraController;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.Sphere;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.RayTracer;
import system.Configuration;

public class TestScene10 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	Random seedGen;
	
	double elapsed = 0.0;
	ProgrammableCameraController camController;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected List<Tracer> configureTracers()
	{
		ArrayList<Tracer> tracers = new ArrayList<Tracer>(1);
		
		//Standard ray tracer
		tracers.add(new RayTracer());
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.01, 2, 16));
		tracers.add(outliner);
		
		return tracers;
	}
	
	@Override
	protected void initialize()
	{
		seedGen = new Random(0x07b56c7168a2dL);
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);

		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffddeeff), new Color(0xddeeffff))),
				new ColorMaterial(Color.white()),
				1
				);
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(4);
		activeCamera.setPosition(new Vector3(0,2.5,5));
		activeCamera.setViewingDirection(new Vector3(0.1,-0.15,-1));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		//activeCamera.setPixelWidth(Configuration.getRenderWidth());
		//activeCamera.setPixelHeight(Configuration.getRenderHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.005 * 0, 0.5));
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
			spline.add(new Vector3(0.2, 0.0, 0.0));
			spline.add(new Vector3(0.2, 0.0, 0.0));
			//camController.addApertureRadiusSpline(spline, 10.0);
		}
		
		//Focal Distance
		{
			Spline spline = new Spline();
			spline.add(new Vector3(5.0, 0.0, 0.0));
			spline.add(new Vector3(5.5, 0.0, 0.0));
			spline.add(new Vector3(4.5, 0.0, 0.0));
			spline.add(new Vector3(5.0, 0.0, 0.0));
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
		
		//If this node is a controller, we don't need any more of the scene loaded
		if(Configuration.isController() && !Configuration.isLeaf())
			return;
		
		
		
		//Setup scene geometry
		MeshSurface mesh = (new Cube(1, 1, 1)).tessellate(150);
		//MeshSurface mesh = (new Sphere(0.5, new Vector3())).tessellate(300);
		
		SimplexNoiseTexture3D simplex = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.8), Color.gray(1.0));
		MatrixTransformTexture3D simplexTrans = new MatrixTransformTexture3D(simplex);
		simplexTrans.getTransform().nonUniformScale(4.0, 4.0, 4.0);
		

		SimplexNoiseTexture3D simplex2 = new SimplexNoiseTexture3D(seedGen.nextLong(), Color.gray(-0.00), Color.gray(-0.08));
		MatrixTransformTexture3D simplexTrans2 = new MatrixTransformTexture3D(simplex2);
		simplexTrans2.getTransform().nonUniformScale(64.0, 64.0, 64.0);
		
		Vector3 multi = new Vector3();
		for(Triangle tri : mesh.getTriangles())
		{
			for(Vertex vert : tri.getVertices())
			{
				Vector3 pos = vert.getPosition();
				Color noise = simplexTrans.evaluate(pos.get(0), pos.get(1), pos.get(2));
				multi.set(1.0 + noise.intensity3(), 1.0 + noise.intensity3(), 1.0 + noise.intensity3());
				Vector3 newPos = pos.multiply(multi);
				vert.setPosition(newPos.addM(
						new Vector3(0, Math.pow((newPos.get(0) * newPos.get(0) + newPos.get(2) * newPos.get(2)), 2.0) * 0.5, 0)));
			}
			tri.generateFaceNormal();
		}
		
		//Add more noise!
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
		}
		
		//Logger.progress(-1, "Tris before [" + mesh.getTriangles().size() + "]");
		//mesh.tessellateMeshByTriangleLongestSideConstraint(0.05);
		//Logger.progress(-1, "Tris after [" + mesh.getTriangles().size() + "]");
		
		//Push the IDs
		mesh.pushSurfaceIDToChildren();

		
		//Accelerate the mesh
		CompositeSurface accelerated = AABVHSurface.makeAABVH(mesh.getTriangles(), 5, 8);
		
		
//		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
//				(new Color(0xff0068ff)).multiply3M(0.9), 
//				(new Color(0xf8f840ff)).multiply3M(0.94), 
//				0.5
//				);
		SphericalGradientTexture3D gradient = new SphericalGradientTexture3D(
				(new Color(0xff0068ff)), 
				Color.gray(0.9), 
				0.6
				);
		MatrixTransformTexture3D gradientTrans = new MatrixTransformTexture3D(gradient);
		gradientTrans.getTransform().scale(1.0);
		
		
		
		Instance instance = new Instance();
		instance.addChild(accelerated);
		instance.setMaterial(new DiffusePTMaterial(gradientTrans));
		//instance.setMaterial(new ColorMaterial(new Color(0.9, 0.9, 0.9)));
		instance.getTransform().rotateY(Math.PI/4.0);
		instance.getTransform().scale(3.0);
		instance.bake(null);
		
		this.addChild(instance);
		
		
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector3(1,-1,-1));
		lightManager.addLight(directionalLight);
		
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(new Color(0xffffffff));
		ambientLight.setIntensity(1.0);
		//lightManager.addLight(ambientLight);
		
		
		
	}
	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		if(Configuration.isClock())
			camController.upate(data.getDt());
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		super.bake(data);
	}
}
