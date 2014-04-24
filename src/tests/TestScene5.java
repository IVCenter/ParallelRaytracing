package tests;

import java.util.ArrayList;

import math.Vector4;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Plane;
import raytrace.geometry.meshes.Cube;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene5 extends Scene
{	

	/*
	 * A simple test scene for debugging
	 */
	double elapsed = 0.0;
	
	/* *********************************************************************************************
	 * Initialize
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{

		skyMaterial = new ColorMaterial(new Color(0xddeeffff));
		
		//super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		activeCamera = new PinholeCamera();
		((PinholeCamera)activeCamera).setStratifiedSampling(true);
		((PinholeCamera)activeCamera).setSuperSamplingLevel(1);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).forceUpdate();
	
		
		
		Cube cube = new Cube(1.0, 1.0, 1.0);
		
		
		int blockCount = 100;
		ArrayList<CompositeSurface> blocks = new ArrayList<CompositeSurface>(blockCount + 1);
		
		for(int x = 0; x < blockCount; ++x)
		{
			Instance inst = new Instance();
			inst.addChild(cube);
			inst.getTransform().translation(randInRange(-4, 4), randInRange(0, 4), randInRange(-6, 2));
			inst.getTransform().scale(randInRange(0.1, 0.9));
			inst.setMaterial(new DiffusePTMaterial(Color.random(0.8), 0));
			inst.updateBoundingBox();
			inst.bake(null);
			//inst.setDynamic(false);
			blocks.add(inst);
		}
		
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(blocks);
		//Add all spheres at once
		this.addChild(aabvh);
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		

		//Make a plane
		Plane plane = new Plane();
		//plane.setMaterial(new ReflectiveMaterial(new Color(0xffeeddff), 0.35));
		//plane.setMaterial(new DielectricMaterial(new Color(0xffffffff), 1.35));
		plane.setMaterial(new DiffusePTMaterial(Color.grey(0.9), 64));
		this.addChild(plane);
		
	}
	
	private double randInRange(double min, double max)
	{
		return min + Math.random() * (max-min);
	}
	
	@Override
	public void update(UpdateData data)
	{
		elapsed = Math.PI/4.0;
		
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
