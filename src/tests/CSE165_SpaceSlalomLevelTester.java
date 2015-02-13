
package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import file.nsconfig.ConfigFileLoader;
import file.sslevel.Gate;
import file.sslevel.SslFileLoader;
import file.sslevel.SslLevelData;
import folder.DirectoryManager;

import math.Matrix4;
import math.Spline;
import math.Vector3;
import math.function._2D.SelectDifferenceNthMthNearest2D;
import math.function._2D.SelectNthNearest2D;
import math.function._3D.TchebyshevDistance3D;
import math.ray.CircularRayStencil;
import process.Environment;
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
import raytrace.geometry.Sphere;
import raytrace.material.ColorMaterial;
import raytrace.material.Material;
import raytrace.scene.Scene;
import raytrace.scene.SceneLoader;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;

public class CSE165_SpaceSlalomLevelTester extends Scene
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
		
		
		//Pixel Transform Tracer
		ProgrammablePixelTracer pixeler = new ProgrammablePixelTracer();
		//tracers.add(pixeler);
		
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.001, 1, 4));
		outliner.setCreaseTexture(new Color(0x888888ff));
		outliner.setOcclusionTexture(new Color(0x888888ff));
		outliner.setSilhouetteTexture(new Color(0x888888ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		tracers.add(outliner);
		
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
		camera.setStratifiedSampling(false);
		camera.setSuperSamplingLevel(1);
		camera.setPosition(new Vector3(0,0,6000));
		camera.setViewingDirection(new Vector3(0,0,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.3);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new PinholeAperture());
		camera.setFocalPlaneDistance(3.0);
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{				
		Material skyMaterial = new ColorMaterial(Color.white());
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	ArrayList<Gate> gates;
	@Override
	protected void configureWorld()
	{
		//Generate data points
		//Animate the data stream and algorithm
		
		ColorMaterial cmat = new ColorMaterial(new Color(0x333333ff));
		ArrayList<AbstractSurface> surfaces = new ArrayList<AbstractSurface>();
		
		
		//Load level
		String filename = "competition_7.txt";
		//String filename = "test1.txt";
		
		SslLevelData data = SslFileLoader.load(
				Configuration.getWorkingDirectory() + Configuration.getModelsSubDirectory() + filename);
		gates = data.getGates();
		
		Spline s;
		ArrayList<Vector3> points;
		int ignoreCount = 0;
		for(Gate g : gates)
		{
			//Fix for overlapping gates
			if(ignoreCount > 0)
			{
				--ignoreCount;
				continue;
			}
			
			points = makePoints(g);
			for(int i = 0; i < points.size(); ++i)
			{
				s = new Spline();
				s.add(points.get(i));
				s.add(points.get((i+1)%points.size()));
				surfaces.addAll(s.tessellate(1, 4, 1, 1));
			}
		}
		
		
		AbstractSurface accel = AABVHSurface.makeAABVH(surfaces);
		
		Instance inst = new Instance();
		inst.addChild(accel);
		inst.setMaterial(cmat);
		
		this.addChild(inst);
	}
	
	protected ArrayList<Vector3> makePoints(Gate g)
	{
		ArrayList<Vector3> points = new ArrayList<Vector3>();

		points.add(g.getCenter().subtract(g.getUp()).subtractM(g.getRight()));
		points.add(g.getCenter().subtract(g.getUp()).addM(g.getRight()));
		points.add(g.getCenter().add(g.getUp()).addM(g.getRight()));
		points.add(g.getCenter().add(g.getUp()).subtractM(g.getRight()));
		
		return points;
	}
	

	int currentGate = 100;
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		Gate g0 = gates.get(currentGate%gates.size());
		Gate g1 = gates.get((currentGate+1)%gates.size());

		//activeCamera.setPosition(g0.getCenter().add(g0.getUp().multiply(10.0)));
		//activeCamera.setViewingDirection(g1.getCenter().subtract(g0.getCenter().add(g0.getUp().multiply(10.0))).normalize());
		//activeCamera.setPosition(g0.getCenter());
		//activeCamera.setViewingDirection(g1.getCenter().subtract(g0.getCenter()).normalize());
		//activeCamera.setUp(g0.getRight().normalize());
		
		currentGate++;
		
		
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
		Configuration.setMasterScene(new CSE165_SpaceSlalomLevelTester());
	}
}