package tests;

import java.util.ArrayList;
import java.util.List;

import math.Spline;
import math.Vector3;
import math.ray.CircularRayStencil;
import process.Environment;
import process.logging.Logger;
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
import raytrace.light.SoftDirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.Material;
import raytrace.material.SkyGradientMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import system.ApplicationDelegate;
import system.Configuration;
import file.sslevel.Gate;
import file.sslevel.SslFileLoader;
import file.sslevel.SslLevelData;

public class SoftDirectionalLightTest extends Scene
{	
	/*
	 * A simple scene for cse165 project2 that generates diagrams of the affects of smoothing/de-noising (2015)
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
		camera.setSuperSamplingLevel(16);
		camera.setPosition(new Vector3(2.2,1.4,2.4));
		camera.setViewingDirection(new Vector3(-0.8,-0.3,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new CircularAperture(0.03, 0.5));
		camera.setFocalPlaneDistance(3.1);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{				
		Material skyMaterial = new SkyGradientMaterial(new GradientTexture3D(new Color(0xffffeeff), new Color(0x888888ff), 5.0));
		//skyMaterial = new ColorMaterial(Color.black());
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	@Override
	protected void configureWorld()
	{
		//Setup a soft directional light
		SoftDirectionalLight sdlight = new SoftDirectionalLight();
		sdlight.setDirection((new Vector3(1, -1, -1)).normalizeM());
		sdlight.setSoftness(0.05);
		sdlight.setColor(new Color(0xffffeeff));
		sdlight.setIntensity(0.9);
		lightManager.addLight(sdlight);
		
		
		//Set up a material
		DiffusePTMaterial cmat = new DiffusePTMaterial(new Color(0xffffffff));
		ArrayList<AbstractSurface> surfaces = new ArrayList<AbstractSurface>();
		
		
		//Setup the geomoetry
		Cube cube = new Cube(0.7, new Vector3(0.0, 0.8, 0.0));
		surfaces.addAll(cube.getTriangles());

		Grid grid = new Grid(10.0, 10.0);
		surfaces.addAll(grid.getTriangles());
		
		
		AbstractSurface accel = AABVHSurface.makeAABVH(surfaces);
		
		Instance inst = new Instance();
		inst.addChild(accel);
		inst.setMaterial(cmat);
		this.addChild(inst);
	}
	
	

	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		//
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
		super.bake(data);
	}
	
	/* *********************************************************************************************
	 * Main
	 * *********************************************************************************************/
	
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
		//Feel free to over write these with your own settings
		Configuration.setId("Debug Node");
		//Configuration.setScreenWidth(1368);
		//Configuration.setScreenHeight(752);
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);//The top most node must have clock set to true (this includes stand-alone nodes)
		Configuration.setLeaf(true);//true for local, false for networked
		Configuration.setController(true);
		Configuration.setWorkingDirectory("/Users/Asylodus/Desktop/NightSky/");
		Configuration.setMasterScene(new SoftDirectionalLightTest());
	}
}