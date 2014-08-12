package tests;

import java.util.ArrayList;
import java.util.List;

import process.logging.Logger;
import math.Vector3;
import math.ray.CircularRayStencil;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.CircularAperture;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.Plane;
import raytrace.geometry.Sphere;
import raytrace.light.AmbientLight;
import raytrace.light.DirectionalLight;
import raytrace.light.PointLight;
import raytrace.map.texture._3D.GradientTexture3D;
import raytrace.material.ColorMaterial;
import raytrace.material.DielectricMaterial;
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
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import raytrace.trace.OutlineTracer;
import raytrace.trace.RayTracer;
import resource.ResourceManager;
import system.Configuration;

public class TestScene4 extends Scene
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
		
		//Outline Tracer
		OutlineTracer outliner = new OutlineTracer();
		outliner.setStencil(new CircularRayStencil(0.002, 3, 24));
		//outliner.setCreaseTexture(new Color(0xdd2222ff));//0x111111ff
		//outliner.setOcclusionTexture(new Color(0x22dd22ff));
		//outliner.setSilhouetteTexture(new Color(0x2222ddff));
		outliner.setCreaseTexture(new Color(0x111111ff));
		outliner.setOcclusionTexture(new Color(0x111111ff));
		outliner.setSilhouetteTexture(new Color(0x111111ff));
		outliner.setDepthThreshold(0.005);
		outliner.setNormalThreshold(0.1);
		tracers.add(outliner);
		
		return tracers;
	}
	
	@Override
	protected void initialize()
	{

		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);
		

		//skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		skyMaterial = new RecursionMinimumCMaterial(
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0xddeeffff))),
				//new SkyGradientMaterial(new GradientTexture3D(new Color(0xddeeffff), new Color(0x8c96eeff), 5.0)),
				new SkyGradientMaterial(new GradientTexture3D(new Color(0xffffffff), new Color(0x999999ff), 5.0)),
				1
				);
		
		activeCamera = new ProgrammableCamera();
		((ProgrammableCamera)activeCamera).setStratifiedSampling(true);
		((ProgrammableCamera)activeCamera).setSuperSamplingLevel(20);
		//activeCamera.setPosition(new Vector3(0,2,5));
		activeCamera.setPosition(new Vector3(0,2.85,3));
		activeCamera.setViewingDirection(new Vector3(0,-0.1,-1));
		activeCamera.setUp(new Vector3(0,1,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((ProgrammableCamera)activeCamera).setAperture(new CircularAperture(0.005 * 0, 0.5));
		((ProgrammableCamera)activeCamera).setFocalPlaneDistance(4.5);
	
		

		ArrayList<CompositeSurface> spheres = new ArrayList<CompositeSurface>(64);
		for(int i = 0; i < 64; i++)
		{
			Sphere sphere = new Sphere();
			sphere.setMaterial(new DielectricMaterial(Color.random(0.7 + (Math.random()/16.0)), randInRange(1.01, 2.0)));
			sphere.setPosition(new Vector3(4 * Math.random() - 2.0, 3.2 * Math.random() - 0.4, 4 * Math.random() - 2.0));
			sphere.setRadius(Math.pow(Math.random() * 0.1, 1.15));
			spheres.add(sphere);
		}
		
		AABVHSurface sphereSurface = AABVHSurface.makeAABVH(spheres);
		//this.addChild(sphereSurface);
		
		
		
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
			Material softShadows = 
					new TwoToneNPRUBlend(
							//new DiffuseMaterial(new Color(0xffffffff)), 
							new DiffusePTMaterial(new Color(0xffffffff)), 
							new Color(0x222222ff), 
							new Color(0xffffffff), 
							0.5
							) ;
			Material softMids = 
					new TwoToneNPRUBlend(
							//new DiffuseMaterial(new Color(0xffffffff)), 
							new DiffusePTMaterial(new Color(0xffffffff)), 
							new Color(0xccccccff), 
							new Color(0xffffffff), 
							0.9
							) ;
			Material hardShadows = 
					new TwoToneNPRUBlend(
							new DiffuseMaterial(new Color(0xffffffff)), 
							new Color(0x555555ff), 
							new Color(0xffffffff), 
							0.01
							);
			Material shadowSelect = new SelectDarkestBBlend(softShadows, hardShadows);
			Material midOrShadowSelect = new SelectDarkestBBlend(softMids, shadowSelect);
			model.setMaterial(shadowSelect);
			//model.setMaterial(new DielectricMaterial(new Color(1.9, 1.9, 1.2), 1.45));
			this.addChild(model);
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
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		
		//Add a plane to the scene
		Plane plane = new Plane();
		//plane.setMaterial(new ReflectiveMaterial(new Color(0x303030ff), 0.30));
		plane.setMaterial(new DiffusePTMaterial(Color.gray(0.7)));
		//this.addChild(plane);
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
		this.updateBoundingBox();
		super.bake(data);
	}
}