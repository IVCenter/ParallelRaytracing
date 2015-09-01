package tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.meshes.Grid;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.geometry.pointclouds.PointCloudSurface;
import raytrace.geometry.pointclouds.PointSurface;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.light.SoftDirectionalLight;
import raytrace.map.texture._3D.CircularGradientTexture3D;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.map.texture._3D.MatrixTransformTexture3D;
import raytrace.map.texture._3D.SimplexNoiseTexture3D;
import raytrace.map.texture._3D.SineWaveTexture3D;
import raytrace.map.texture._3D.SphericalGradientTexture3D;
import raytrace.map.texture._3D.Texture3D;
import raytrace.map.texture._3D.WorleyNoiseTexture3D;
import raytrace.map.texture._3D.blend.AdditiveT3DBlend;
import raytrace.map.texture._3D.blend.MaskT3DBlend;
import raytrace.map.texture._3D.blend.MultiplicativeT3DBlend;
import raytrace.map.texture._3D.blend.OneMinusT3DBlend;
import raytrace.map.texture._3D.blend.SubtractiveT3DBlend;
import raytrace.material.AshikhminPTMaterial;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelDiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.Material;
import raytrace.material.NormalAsColorMaterial;
import raytrace.material.ReflectiveMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.SubSurfaceDiffusePTTestMaterial;
import raytrace.material.blend.binary.PosterMaskBBlend;
import raytrace.material.blend.binary.SelectDarkestBBlend;
import raytrace.material.blend.unary.MultiplicativeUBlend;
import raytrace.material.blend.unary.TwoToneNPRUBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.MatrixTransformSurface;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import raytrace.trace.postprocess.BloomPostProcess;
import resource.ResourceManager;
import system.ApplicationDelegate;
import system.Configuration;

public class DragonInABoxTest extends Scene
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
		outliner.setStencil(new CircularRayStencil(0.001, 3, 24));
		outliner.setCreaseTexture(new Color(0x111111ff));
		outliner.setOcclusionTexture(new Color(0x111111ff));
		outliner.setSilhouetteTexture(new Color(0x111111ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		tracers.add(outliner);
		
		BloomPostProcess bloom = new BloomPostProcess();
		//tracers.add(bloom);
		
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
		camera.setSuperSamplingLevel(10);
		camera.setPosition(new Vector3(0,2.85,5));
		camera.setViewingDirection(new Vector3(0,-0.1,-1));
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
		Material skyMaterial = new ColorMaterial((new Color(0xddeeffff)).multiply3(0.6));
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
		//Instance dragon = ResourceManager.create("dragon_smooth.obj");
		//Instance dragon = ResourceManager.create("ganesha1.obj");
		//Instance dragon = ResourceManager.create("teapot_smooth.obj");
		//Instance dragon = ResourceManager.create("bear.obj");
		//Instance dragon = ResourceManager.create("bunny_n.obj");
		Instance dragon = ResourceManager.create("ia.obj");
		
		model = dragon;
		
		if(dragon != null) {
			//dragon.getTransform().scale(2.0/*2.4*/);//Dragon
			//dragon.getTransform().scale(0.05/*2.4*/);//Dragon
			//dragon.getTransform().scale(0.2);//Bear
			//dragon.getTransform().scale(1.7);//Bunny
			dragon.getTransform().scale(0.2);//Ia
			//dragon.getTransform().translate(0, 0, -1.2);
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
			
			
			PointLight pl = new PointLight();
			pl.setPosition(new Vector3(0, bb.max.get(1) * 0.88, bb.max.get(2) + 0.0));
			pl.setColor(Color.white());
			pl.setConstantAttenuation(1.0);
			pl.setLinearAttenuation(0.0);
			pl.setQuadraticAttenuation(0.5);
			lightManager.addLight(pl);
			
			Sphere spl = new Sphere();
			spl.setRadius(0.1);
			spl.setPosition(pl.getPosition());
			spl.setMaterial(new ColorMaterial(Color.red()));
			//surfaces.add(spl);
			
			
			
			
		}else{
			Logger.error(-13, "CSE167_2014_Project2: Dragon was null!");
		}
		
		
		//Make the box
		float size = 10.0f;
		
		Grid grid = new Grid(1.0, 1.0);
		grid.setMaterial(new DiffusePTMaterial(new Color(0xbbbbbbff)));
		
		Instance bottom = new Instance();
		bottom.addChild(grid);
		bottom.getTransform().scale(size);
		bottom.getTransform().rotateX(0.0);
		bottom.getTransform().translate(0, 0, 0);
		bottom.bake(null);
		surfaces.add(bottom);
		

		
		Instance back = new Instance();
		back.addChild(grid);
		back.getTransform().scale(size);
		back.getTransform().rotateX(Math.PI/2.0);
		back.getTransform().translate(0, size/2.0, -size/2.0);
		back.bake(null);
		surfaces.add(back);
		

		
		Instance left = new Instance();
		left.addChild(grid);
		left.getTransform().scale(size);
		left.getTransform().rotateZ(-Math.PI/2.0);
		left.getTransform().translate(-size/2.0, size/2.0, 0);
		left.bake(null);
		surfaces.add(left);
		

		
		Instance right = new Instance();
		right.addChild(grid);
		right.getTransform().scale(size);
		right.getTransform().rotateZ(Math.PI/2.0);
		right.getTransform().translate(size/2.0, size/2.0, 0);
		right.bake(null);
		surfaces.add(right);
		
		
		
		//Generate an interesting back wall
		//List<AbstractSurface> backWall = makeWall(10, 10, 0.5, 6, 2);
		//surfaces.addAll(backWall);
		
		
		//Cube pedestal = new Cube(size*0.3, size * 0.05, size * 0.3, new Vector3(0, 0, -1.2));
		//pedestal.setMaterial(new AshikhminPTMaterial(Color.white(), new Color(1.0, 1.0, 1.0),  0.95,  0.05, 100000.0, 100000.0));
		//surfaces.add(pedestal);
		

		//Global AABVH
		AABVHSurface globalAABVH = AABVHSurface.makeAABVH(surfaces);
		this.addChild(globalAABVH);
		
	}
	
	List<AbstractSurface> makeWall(double width, double height, double depth, int slices, int rdepth)
	{
		List<AbstractSurface> surfaces = new ArrayList<AbstractSurface>();
		
		makeWallRecurse(surfaces, width, height, depth, -width/2.0, -height/2.0, slices, rdepth, 0.0);
		
		return surfaces;
	}
	
	Material mat = new DiffusePTMaterial(Color.white());
	void makeWallRecurse(List<AbstractSurface> surfaces, double width, double height, double depth, double offsetX, 
			double offsetY, int slices, int rdepth, double depthOffset)
	{
		if(rdepth > 0)
		{
			double newOffsetX = offsetX;
			double newOffsetY = offsetY;
			double newWidth = width / slices;
			double newHeight = height / slices;
			
			for(int x = 0; x < slices; ++x)
			{
				for(int y = 0; y < slices; ++y)
				{
					makeWallRecurse(surfaces, newWidth, newHeight, depth, newOffsetX + x * newWidth, newOffsetY + y * newHeight, 
							 slices, rdepth-1, depthOffset + rdepth/5.0 *  Math.pow(Math.random(), 1.0/rdepth));
				}
			}
			
			return;
		}
		
		Cube c;
		c = new Cube(width*0.95, height*0.95, depth, new Vector3(offsetX, offsetY+5.0, depthOffset-5.0));
		c.setMaterial(new DiffusePTMaterial(new Color(0xbbbbbbff)));
		surfaces.add(c);
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
		Configuration.setMasterScene(new DragonInABoxTest());
		
		Configuration.setFrameRate(1.0);
		//Configuration.setRealTime(true);
	}
}