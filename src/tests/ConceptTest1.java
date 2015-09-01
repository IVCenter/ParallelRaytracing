package tests;

import java.util.ArrayList;
import java.util.List;

import process.Environment;
import process.logging.Logger;
import math.Vector3;
import math.ray.CircularRayStencil;
import raster.pixel.ColorInversionPT;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.geometry.meshes.Cube;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.light.SoftDirectionalLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricPTMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.FresnelMetalMaterial;
import raytrace.material.Material;
import raytrace.material.ReflectiveMaterial;
import raytrace.material.SkyGradientMaterial;
import raytrace.material.blend.binary.SelectDarkestBBlend;
import raytrace.material.blend.unary.TwoToneNPRUBlend;
import raytrace.material.composite.RecursionMinimumCMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.ProgrammablePixelTracer;
import raytrace.trace.RayTracer;
import resource.ResourceManager;
import system.ApplicationDelegate;
import system.Configuration;

public class ConceptTest1 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
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
		outliner.setCreaseTexture(new Color(0x111111ff));
		outliner.setOcclusionTexture(new Color(0x111111ff));
		outliner.setSilhouetteTexture(new Color(0x111111ff));
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
		ProgrammableCamera activeCamera = new ProgrammableCamera();
		activeCamera.setStratifiedSampling(true);
		activeCamera.setSuperSamplingLevel(4);
		activeCamera.setPosition(new Vector3(0,2.85,3));
		activeCamera.setViewingDirection(new Vector3(0,-0.1,-1));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		activeCamera.setAperture(new CircularAperture(0.005 * 0, 0.5));
		activeCamera.setFocalPlaneDistance(4.5);
		
		return activeCamera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{	
		Material skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				//new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0x8c96eeff), 5.0)),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffffffff), new Color(0x777777ff), 5.0)),
				1
				);
		
		return skyMaterial;
	}
	
	@Override
	protected void configureWorld()
	{
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);
		
		
		Material softShadows = 
				new TwoToneNPRUBlend(
						//new DiffuseMaterial(new Color(0xffffffff)), 
						new DiffusePTMaterial(new Color(0xffffffff)), 
						new Color(0x010101ff), 
						new Color(0xffffffff), 
						0.5
						) ;
//		Material softMids = 
//				new TwoToneNPRUBlend(
//						//new DiffuseMaterial(new Color(0xffffffff)), 
//						new DiffusePTMaterial(new Color(0xffffffff)), 
//						new Color(0xccccccff), 
//						new Color(0xffffffff), 
//						0.9
//						) ;
		Material hardShadows = 
				new TwoToneNPRUBlend(
						new DiffuseMaterial(new Color(0xffffffff)), 
						new Color(0x444444ff), 
						new Color(0xffffffff), 
						0.01
						);
		//Material shadowSelect = new SelectDarkestBBlend(softShadows, hardShadows);
		Material shadowSelect = new SelectDarkestBBlend(softShadows, hardShadows);
		//Material midOrShadowSelect = new SelectDarkestBBlend(softMids, shadowSelect);
		
		
	
		

		ArrayList<AbstractSurface> spheres = new ArrayList<AbstractSurface>(64);
		for(int i = 0; i < 64; i++)
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(shadowSelect);
			//sphere.setMaterial(new ColorMaterial(new Color(0xfefefeff)));
			sphere.setPosition(new Vector3(4 * Math.random() - 2.0, 3.2 * Math.random() - 0.4, 4 * Math.random() - 2.0));
			sphere.setRadius(Math.pow(Math.random() * 0.3, 1.15));
			spheres.add(sphere);
		}
		
		for(int i = 0; i < 64; i++)
		{
			Cube cube = new Cube(Math.pow(Math.random() * 1.0, 1.15), new Vector3(4 * Math.random() - 2.0, 3.2 * Math.random() - 0.4, 4 * Math.random() - 2.0));
			cube.setMaterial(shadowSelect);
			spheres.add(cube);
		}
		
		
		
		AABVHSurface sphereSurface = AABVHSurface.makeAABVH(spheres);
		this.addChild(sphereSurface);
		
		
		
		Instance model = ResourceManager.create("ia.obj");
		
		if(model != null) {
			model.getTransform().scale(0.18);//ia
			//model.getTransform().translate(-1.2, 0, 1.2);//IA
			model.getTransform().translate(0, 0, 1.2);
			model.getTransform().rotateY(0.15);
			model.bake(null);
			//model.setMaterial(new ReflectiveMaterial(new Color(0xff0068ff), .70));
			//model.setMaterial(new DiffuseMaterial(new Color(0xffffffff)));
			//model.setMaterial(new DiffusePTMaterial(new Color(0xffffffff)));
			//model.setMaterial(new ColorMaterial(new Color(0xffffffff)));
			model.setMaterial(shadowSelect);
			//model.setMaterial(new DielectricMaterial(new Color(1.9, 1.9, 1.2), 1.45));
			//this.addChild(model);
		}else{
			Logger.error(-13, "TestScene4: Model was null!");
		}
		
		
		
		Instance model2 = ResourceManager.create("ia.obj");
		
		if(model2 != null) {
			model2.getTransform().scale(0.20);//ia
			model2.getTransform().translate(1, 0, 0);//IA
			model2.bake(null);
			model2.setMaterial(new FresnelMetalMaterial(new Color(1.0, 0.8, 0.6, 1.0), 0.55, 5.20));
			//this.addChild(model2);
		}else{
			Logger.error(-13, "TestScene4: Model2 was null!");
		}
		
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.70);
		directionalLight.setDirection(new Vector3(1,-1,-1));
		//directionalLight.setSoftness(0.02);
		lightManager.addLight(directionalLight);
		

		PointLight pointLight = new PointLight();
		pointLight.setColor(Color.white());
		pointLight.setIntensity(0.70);
		pointLight.setQuadraticAttenuation(0.8);
		pointLight.setPosition(new Vector3(0.0, 3.0, 1.6));
		//lightManager.addLight(pointLight);
		

		AmbientLight ambientLight = new AmbientLight();
		ambientLight.setColor(new Color(0xddeeffff));
		ambientLight.setIntensity(0.9);
		//lightManager.addLight(ambientLight);
		
		
		
		
		//Add a plane to the scene
		Plane plane = new Plane();
		//plane.setMaterial(new ReflectiveMaterial(new Color(0x303030ff), 0.30));
		plane.setMaterial(shadowSelect);
		this.addChild(plane);
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	@Override
	public void update(UpdateData data)
	{
		elapsed += data.getDt();
		
		//Vector3 position = activeCamera.getPosition();
		//position.set(Math.cos(elapsed * 1 + Math.PI/2) * 1.8, 2.85, Math.sin(elapsed * 1 + Math.PI/2) * 1.8);
		//activeCamera.setPosition(position);
		
		//activeCamera.setViewingDirection(position.multiply(-1.0));
		//activeCamera.getViewingDirection().set(1, -0.1);
		
		//activeCamera.getPosition().set(2, activeCamera.getPosition().get(2) + 1.2);
		
		//Update the children
		super.update(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		//TODO: This may be costly
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
		//Feel free to over write these with your own settings
		Configuration.setId("Debug Node");
		//Configuration.setScreenWidth(1368);
		//Configuration.setScreenHeight(752);
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);//The top most node must have clock set to true (this includes stand-alone nodes)
		Configuration.setLeaf(true);//true for local, false for networked
		Configuration.setController(true);
		Configuration.setWorkingDirectory("/Users/Asylodus/Desktop/NightSky/");
		Configuration.setMasterScene(new ConceptTest1());
	}
}




