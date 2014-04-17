package tests;

import java.util.ArrayList;

import math.Vector4;
import process.logging.Logger;
import raytrace.camera.PinholeCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.geometry.Sphere;
import raytrace.light.DirectionalLight;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.scene.Scene;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import raytrace.surfaces.acceleration.AABVHSurface;
import system.Configuration;

public class TestScene3 extends Scene
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
		((PinholeCamera)activeCamera).setUseRayCaching(false);
		activeCamera.setPosition(new Vector4(0,2,5,0));
		activeCamera.setViewingDirection(new Vector4(0,-0.1,-1,0));
		activeCamera.setUp(new Vector4(0,1,0,0));
		activeCamera.setFieldOfView(Math.PI/2.0);
		activeCamera.setPixelWidth(Configuration.getScreenWidth());
		activeCamera.setPixelHeight(Configuration.getScreenHeight());
		((PinholeCamera)activeCamera).forceUpdate();
	
		
		
		
		//Color Spheres
		int size = 10;
		ArrayList<CompositeSurface> spheres = new ArrayList<CompositeSurface>(size * size * size + 1);
		
		for(int x = -size/2; x < size/2; ++x)
			for(int y = -size/2; y < size/2; ++y)
				for(int z = 0; z > -size; --z)
				{
					Sphere sphere = new Sphere();
					sphere.setMaterial(new DiffuseMaterial(Color.random()));
					sphere.setPosition(new Vector4(x, y, z, 0));
					sphere.setRadius(0.50);
					sphere.updateBoundingBox();
					sphere.setDynamic(false);
					spheres.add(sphere);
				}
		
		CompositeSurface sphereAABVH = AABVHSurface.makeAABVH(spheres);
		sphereAABVH.updateBoundingBox();
		sphereAABVH.setDynamic(false);
		
		
		int blockSize = 10;
		ArrayList<CompositeSurface> blocks = new ArrayList<CompositeSurface>(blockSize * blockSize * blockSize + 1);
		
		for(int x = -blockSize/2; x < blockSize/2; ++x)
			for(int y = -blockSize/2; y < blockSize/2; ++y)
				for(int z = 0; z > -blockSize; --z)
				{
					Instance inst = new Instance();
					inst.addChild(sphereAABVH);
					inst.getTransform().translation(x * size * (Math.random()+1), y * size * (Math.random()+1), z * size * (Math.random()+1));
					inst.updateBoundingBox();
					inst.bake(null);
					//inst.setDynamic(false);
					blocks.add(inst);
				}
		
		
		//Add all spheres at once
		this.addChildrenUnsafe(blocks);
		
		
		//Directional Light
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(Color.white());
		directionalLight.setIntensity(0.95);
		directionalLight.setDirection(new Vector4(1,-1,-1,0));
		lightManager.addLight(directionalLight);
		
		
		
		//Update bounding boxes
		this.updateBoundingBox();
		
		//BVH TESTS
		Logger.progress(-1, "Starting creating a BVH for root surface...");
		long startTime = System.currentTimeMillis();
		
		AABVHSurface aabvh = AABVHSurface.makeAABVH(this.getChildren(), 1, 4);
		this.getChildren().clear();
		this.addChild(aabvh);
		
		//Refresh
		this.updateBoundingBox();
		
		Logger.progress(-1, "Ending AABVH creation... (" + (System.currentTimeMillis() - startTime) + "ms).");
		
		
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
