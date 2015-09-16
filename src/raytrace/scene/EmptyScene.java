package raytrace.scene;

import java.util.ArrayList;
import java.util.List;

import math.Vector3;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.camera.aperture.PinholeAperture;
import raytrace.color.Color;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.geometry.meshes.Cube;
import raytrace.light.PointLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.Material;
import raytrace.surfaces.Instance;
import raytrace.trace.IntegrationTracer;
import raytrace.trace.integration.PathTracingIntegrator;
import system.Configuration;

public class EmptyScene extends Scene {
	
	/*
	 *	An empty scene
	 */

	/* *********************************************************************************************
	 * Local Variables
	 * *********************************************************************************************/
	protected Instance cubeParent;
	protected Cube cube;
	
	
	/* *********************************************************************************************
	 * Tracers
	 * *********************************************************************************************/
	@Override
	protected List<Tracer> configureTracers()
	{
		ArrayList<Tracer> tracers = new ArrayList<Tracer>(1);
		
		//Standard ray tracer
		tracers.add(new IntegrationTracer(new PathTracingIntegrator()));
		
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
		camera.setPosition(new Vector3(0,3,6));
		camera.setViewingDirection(new Vector3(0,0,-1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.0);
		camera.setPixelWidth(Configuration.getScreenWidth());
		camera.setPixelHeight(Configuration.getScreenHeight());
		camera.setAperture(new PinholeAperture());
		
		return camera;
	}

	
	/* *********************************************************************************************
	 * Sky Material
	 * *********************************************************************************************/
	@Override
	protected Material configureSkyMaterial()
	{
		Material skyMaterial = new ColorMaterial(Color.black());
		
		return skyMaterial;
	}
	

	/* *********************************************************************************************
	 * World
	 * *********************************************************************************************/
	@Override
	protected void configureWorld()
	{
		//Create a new cube and give it a dark green diffuse material
		cube = new Cube();
		cube.setMaterial(new DiffuseMaterial(Color.darkGreen()));
		
		//Create a new instaance wrapper for the cube
		cubeParent = new Instance();
		cubeParent.getTransform().scale(2.0);
		cubeParent.getTransform().translate(0.0, 3.0, 0.0);
		cubeParent.bake(null);
		cubeParent.addChild(cube);
		
		//Add the cube instance to the scene
		addChild(cubeParent);
		
		//Point Light
		PointLight pointLight = new PointLight();
		pointLight.setColor(Color.white());
		pointLight.setIntensity(200.00);
		pointLight.setConstantAttenuation(1.0);
		pointLight.setQuadraticAttenuation(1.0);
		pointLight.setPosition(new Vector3(0.0, 8.0, 4.0));
		lightManager.addLight(pointLight);
	}


	/* *********************************************************************************************
	 * Update
	 * *********************************************************************************************/
	@Override
	public void update(UpdateData data)
	{
		cubeParent.getTransform().rotateY(data.getDt());
		cubeParent.bake(null);
		
		super.update(data);
	}
	
}
