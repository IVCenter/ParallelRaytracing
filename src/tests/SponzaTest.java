package tests;

import java.util.ArrayList;
import java.util.List;

import math.Vector3;
import math.ray.CircularRayStencil;
import process.Environment;
import process.logging.Logger;
import raster.pixel.ColorInversionPT;
import raytrace.bounding.BoundingBox;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.Grid;
import raytrace.light.SoftDirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.Material;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import raytrace.trace.postprocess.BloomPostProcess;
import resource.ResourceManager;
import system.ApplicationDelegate;
import system.Configuration;

public class SponzaTest extends Scene
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
		
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.002, 3, 24));
		outliner.setCreaseTexture(new Color(0xffffffff));
		outliner.setOcclusionTexture(new Color(0xffffffff));
		outliner.setSilhouetteTexture(new Color(0xffffffff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		//tracers.add(outliner);
		
		BloomPostProcess bloom = new BloomPostProcess();
		tracers.add(bloom);
		
		return tracers;
	}

	
	/* *********************************************************************************************
	 * Camera
	 * *********************************************************************************************/
	@Override
	protected Camera configureCamera()
	{
		ProgrammableCamera camera = new ProgrammableCamera();
		
		camera = new ProgrammableCamera();
		camera.setStratifiedSampling(true);
		camera.setSuperSamplingLevel(1);
		camera.setPosition(new Vector3(0,30.85,5));
		camera.setViewingDirection(new Vector3(0,-1.000,-0.00001));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new CircularAperture(0.001, 1.0));
		camera.setFocalPlaneDistance(6.2);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{				
		Material skyMaterial = new ColorMaterial((new Color(0xddeeffff)));
		//Material skyMaterial = new ColorMaterial(Color.black());
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	Instance model;
	@Override
	protected void configureWorld()
	{
		
		//Directional Light
		SoftDirectionalLight directionalLight = new SoftDirectionalLight();
		directionalLight.setColor(new Color(0xeeeeeaff));
		directionalLight.setIntensity(2.80);
		directionalLight.setDirection(new Vector3(1,-1.2,-0.2));
		directionalLight.setSoftness(0.02);
		lightManager.addLight(directionalLight);
		
	
		//Object list
		ArrayList<AbstractSurface> surfaces = new ArrayList<AbstractSurface>();
		
		
		//Dragon
		Instance dragon = ResourceManager.create("sponza.obj");
		//Instance dragon = ResourceManager.create("teapot_smooth.obj");
		//Instance dragon = ResourceManager.create("bear.obj");
		//Instance dragon = ResourceManager.create("bunny_n.obj");
		//Instance dragon = ResourceManager.create("ia.obj");
		
		model = dragon;
		
		if(dragon != null) {
			dragon.getTransform().scale(1.0/*2.4*/);//Dragon
			//dragon.getTransform().scale(0.2);//Bear
			//dragon.getTransform().scale(1.7);//Bunny
			//dragon.getTransform().scale(0.4);//Ia
			dragon.getTransform().translate(0, 0, -1.2);
			//dragon.getTransform().rotateY(1.65);
			dragon.bake(null);

			//Place the cloud on the ground
			BoundingBox bb = dragon.getBoundingBox();
			dragon.getTransform().translate(-1.0 * bb.getMidpoint().get(0), -1.0 * bb.min.get(1), 0);
			dragon.bake(null);
			

			dragon.setMaterial(new DiffusePTMaterial(Color.gray(0.85).multiply3(Color.white())));
			//dragon.setMaterial(new AshikhminPTMaterial(new Color(0xC7F464ff), new Color(0xffffffff), 0.7, 0.3, 16.0, 4.0));
			//dragon.setMaterial(new NormalAsColorMaterial(1.0));
			//dragon.setMaterial(new RecursionMinimumCMaterial(new NormalAsColorMaterial(5.0), new NormalAsColorMaterial(1.0), 1));
			
			surfaces.add(dragon);
		}else{
			Logger.error(-13, "CSE167_2014_Project2: Dragon was null!");
		}
		
		

		//Global AABVH
		this.addChild(dragon);
		
	}
	
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		//model.getTransform().rotateY(data.getDt());
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		super.bake(data);
	}
	
	
	
	public static void main(String[] args)
	{
		loadDebugConfiguration();
		
		Logger.progress(-1, "Launching a Night Sky Node with ID:[" + Configuration.getId() + "]...");
		
		//Pass off control to the ApplicationDelegate
		ApplicationDelegate app = new ApplicationDelegate();
		app.execute(new Environment());
	}
	
	private static void loadDebugConfiguration()
	{
		int width = 1280;
		int height = 720;
		//Feel free to over write these with your own settings
		Configuration.setId("Debug Node");
		//Configuration.setScreenWidth(1368);
		//Configuration.setScreenHeight(752);
		Configuration.setScreenWidth(width);
		Configuration.setScreenHeight(height);
		Configuration.setRenderWidth(width);
		Configuration.setRenderHeight(height);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);//The top most node must have clock set to true (this includes stand-alone nodes)
		Configuration.setLeaf(true);//true for local, false for networked
		Configuration.setController(true);
		Configuration.setWorkingDirectory("/Users/Asylodus/Desktop/NightSky/");
		Configuration.setMasterScene(new SponzaTest());
		
		Configuration.setFrameRate(1.0);
		//Configuration.setRealTime(true);
	}
}