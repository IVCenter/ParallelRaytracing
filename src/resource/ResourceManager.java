package resource;

import java.util.HashMap;

import file.obj.ObjFileLoader;
import file.obj.ObjModelData;
import file.xyz.XyzFileLoader;
import file.xyz.XyzPointCloudData;

import raytrace.geometry.meshes.CompositeObjectSurface;
import raytrace.geometry.meshes.Cube;
import raytrace.geometry.pointclouds.PointCloudSurface;
import raytrace.surfaces.AbstractSurface;
import raytrace.surfaces.Instance;
import system.Configuration;

public class ResourceManager {
	
	/*
	 * A resource manager for loading models, geometry, etc.
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static HashMap<String, AbstractSurface> surfaces;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		surfaces = new HashMap<String, AbstractSurface>();
		
		AbstractSurface cube = new Cube();
		surfaces.put("cube", cube);
		surfaces.put("Cube", cube);
		surfaces.put("CUBE", cube);
		surfaces.put("box", cube);
		surfaces.put("Box", cube);
		surfaces.put("BOX", cube);
	}
	

	/* *********************************************************************************************
	 * Static Access Methods
	 * *********************************************************************************************/
	public static Instance create(String keyOrFilePath)
	{
		Instance inst = getInstance(keyOrFilePath);
		
		if(inst != null)
			return inst;
		
		//If we can't create an instance, try to load the key/filePath
		load(keyOrFilePath);
		
		//And now try again
		inst = getInstance(keyOrFilePath);
		
		if(inst != null)
			return inst;

		//If we still failed to create an instance, return null
		return null;
	}
	

	/* *********************************************************************************************
	 * Private Static Helper Methods
	 * *********************************************************************************************/
	private static Instance getInstance(String keyOrFilePath)
	{
		AbstractSurface cs = surfaces.get(keyOrFilePath);
		
		//Create a new wrapping instance for the current surface
		if(cs != null)
		{
			Instance inst = new Instance();
			inst.addChild(cs);
			return inst;
		}
		
		return null;
	}
	
	private static void load(String keyOrFilePath)
	{
		//If the key/filePath has an .obj extension, then parse it as an object file
		if(keyOrFilePath.endsWith(".obj"))
		{
			//TODO: Load object file and create a Mesh
			ObjModelData data = ObjFileLoader.load(
					Configuration.getWorkingDirectory() + Configuration.getModelsSubDirectory() + keyOrFilePath);
			
			if(data == null)
				return;
			
			AbstractSurface model = new CompositeObjectSurface(data);
			
			surfaces.put(keyOrFilePath, model);
			
			return;
		}
		else if(keyOrFilePath.endsWith(".xyz"))
		{
			XyzPointCloudData data = XyzFileLoader.load(
					Configuration.getWorkingDirectory() + Configuration.getModelsSubDirectory() + keyOrFilePath);
			
			if(data == null)
				return;
			
			AbstractSurface model = new PointCloudSurface(data);
			
			surfaces.put(keyOrFilePath, model);
			
			return;
		}
		
		//TODO: Add more loaders
	}
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/

}
