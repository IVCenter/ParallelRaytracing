package tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.Vector3;
import math.ray.CircularRayStencil;
import process.logging.Logger;
import raster.pixel.ColorInversionPT;
import raytrace.bounding.BoundingBox;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.camera.aperture.PinholeAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.Grid;
import raytrace.geometry.pointclouds.PointCloudSurface;
import raytrace.geometry.pointclouds.PointSurface;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.Material;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.MatrixTransformSurface;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.MedianTracer;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import resource.ResourceManager;
import system.Configuration;

public class VoxelTest1 extends Scene
{	
	/*
	 * A simple test scene for cse167 project2 inspirations (2014)
	 */

	/* *********************************************************************************************
	 * Local Variables
	 * *********************************************************************************************/
	double elapsed = 0.0;
	
	
	/* *********************************************************************************************
	 * Tracers
	 * *********************************************************************************************/
	@Override
	protected List<Tracer> configureTracers()
	{
		ArrayList<Tracer> tracers = new ArrayList<Tracer>(2);
		
		//Standard ray tracer
		tracers.add(new RayTracer());
		
		
		//Pixel Transform Tracer
		ProgrammablePixelTracer pixeler = new ProgrammablePixelTracer();
		pixeler.addTransform(new ColorInversionPT());
		//tracers.add(pixeler);
		
		
		//Median Tracer (does the average as a test)
		MedianTracer medianer = new MedianTracer(10, 10);
		//tracers.add(medianer);
		
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.0015, 1, 4));
		outliner.setCreaseTexture(new Color(0x111111ff));
		outliner.setOcclusionTexture(new Color(0x111111ff));
		outliner.setSilhouetteTexture(new Color(0x111111ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		//tracers.add(outliner);
		
		return tracers;
	}

	
	/* *********************************************************************************************
	 * Camera
	 * *********************************************************************************************/
	@Override
	protected Camera configureCamera()
	{
		//Configuration.setScreenWidth((int)Configuration.getScreenWidth() * 2);
		//Configuration.setScreenHeight(Configuration.getScreenHeight() * 2);
		//Configuration.setRenderWidth(Configuration.getScreenWidth() / 2);
		//Configuration.setRenderHeight(Configuration.getScreenHeight() / 2);
		
		ProgrammableCamera camera = new ProgrammableCamera();
		
		camera = new ProgrammableCamera();
		camera.setStratifiedSampling(false);
		camera.setSuperSamplingLevel(1);
		camera.setPosition(new Vector3(0,3,0));
		camera.setViewingDirection(new Vector3(0,-1,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.0);
		//camera.setFieldOfView(Math.PI/1.6);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new PinholeAperture());
		camera.setFocalPlaneDistance(7.2);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{
		Material skyMaterial = new RecursionMinimumCMaterial(
				//new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xfbfbfbff), new Color(0xeeeeeeff), 5.0)),
				//new ColorMaterial(Color.black()),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffffffff), new Color(0x999999ff), 5.0)),
				1
				);
		
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	@Override
	protected void configureWorld()
	{
		double voxelSize = 0.15;
		int gridWidth = 64;
		int gridHeight = 64;
		
		Vector3 posOffset = new Vector3((-1.0 * voxelSize * gridWidth) / 2.0, 0, 0);
		
		Cube vox = new Cube();
		vox.setMaterial(new ColorMaterial(Color.red()));
		
		MatrixTransformSurface inst;
		
		ArrayList<AbstractSurface> voxels = new ArrayList<AbstractSurface>();
		
		//Material voxMat = new DiffusePTMaterial(Color.gray(0.7 + Math.random() * 0.2));
		
		//Voxels
		for(int i = 0; i < gridWidth; i++)
		{
			for(int j = 0; j < gridHeight; j++)
			{
				vox = new Cube(voxelSize * 0.98, (new Vector3(i * voxelSize, 0, -j * voxelSize)).addM(posOffset));
				vox.setMaterial(new DiffusePTMaterial(Color.gray(0.8 + Math.random() * 0.1)));
				//vox.setMaterial(new ColorMaterial(Color.gray(0.8 + Math.random() * 0.1)));
				//voxels.addAll(vox.getTriangles());
				voxels.add(vox);
				
//				inst = new Instance();
//				inst.getTransform().scale(voxelSize * 0.98);
//				inst.getTransform().translate((new Vector3(i * voxelSize, 0, -j * voxelSize)).addM(posOffset));
//				inst.setMaterial(new DiffusePTMaterial(Color.gray(0.8 + Math.random() * 0.1)));
//				//inst.setMaterial(new ColorMaterial(Color.gray(0.8 + Math.random() * 0.1)));
//				inst.addChild(vox);
//				inst.bake(null);
//				voxels.add(inst);
			}
		}
		
		int randomVoxelCount = 8192;
		HashMap<Integer, Integer> stackHeight = new HashMap<Integer, Integer>();
		int x;
		int z;
		
		for(int i = 0; i < randomVoxelCount; i++)
		{
			x = (int)(((Math.random() - 0.5) * Math.pow(Math.random(), 0.5) + 0.5) * gridWidth);
			z = (int)(((Math.random() - 0.5) * Math.pow(Math.random(), 0.5) + 0.5) * gridHeight);
			
			Integer height = stackHeight.get(z * gridWidth + x);
			if(height == null)
				height = 0;
			height += 1;
			
			vox = new Cube(voxelSize * 0.98, (new Vector3(x * voxelSize, height * voxelSize, -z * voxelSize)).addM(posOffset));
			vox.setMaterial(new DiffusePTMaterial(Color.gray(0.8 + Math.random() * 0.1).add3M(new Color(0, - 0.1 * height,  - 0.1 * height))));
			//vox.setMaterial(new ColorMaterial(Color.gray(0.8 + Math.random() * 0.1).add3M(new Color(0, - 0.1 * height,  - 0.1 * height))));
			//voxels.addAll(vox.getTriangles());
			voxels.add(vox);
			
//			inst = new Instance();
//			inst.getTransform().scale(voxelSize * 0.98);
//			inst.getTransform().translate((new Vector3(x * voxelSize, height * voxelSize, -z * voxelSize)).addM(posOffset));
//			inst.setMaterial(new DiffusePTMaterial(Color.gray(0.8 + Math.random() * 0.1).add3M(new Color(0, - 0.1 * height,  - 0.1 * height))));
//			//inst.setMaterial(new ColorMaterial(Color.gray(0.8 + Math.random() * 0.1).add3M(new Color(0, - 0.1 * height,  - 0.1 * height))));
//			inst.addChild(vox);
//			inst.bake(null);
//			voxels.add(inst);
			
			stackHeight.put(z * gridWidth + x, height);
		}
		

		AABVHSurface accel = AABVHSurface.makeAABVH(voxels, 10, 1);//voxels
		//AABVHSurface accel = AABVHSurface.makeAABVH(voxels, 10, 8);//tris
		//accel.bake(null);
		this.addChild(accel);
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);//.7 for global illum
		directionalLight.setDirection(new Vector3(1,-1,-1));
		lightManager.addLight(directionalLight);
		
		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(Color.white());
		ambientLight.setIntensity(0.1);
		//lightManager.addLight(ambientLight);
		
		
		//Update bounding boxes
		//this.updateBoundingBox();
		
		
		//Add a plane to the scene
		Grid plane = new Grid(100, 100);
		plane.setMaterial(new DiffusePTMaterial(Color.gray(0.9)));
		//this.addChild(plane);
	}
	

	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		//his.updateBoundingBox();
		super.bake(data);
	}
}