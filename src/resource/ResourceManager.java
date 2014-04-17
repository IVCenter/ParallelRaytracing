package resource;

import java.util.HashMap;

import file.obj.ObjFileLoader;
import file.obj.ObjModelData;

import raytrace.geometry.meshes.CompositeObjectSurface;
import raytrace.geometry.meshes.Cube;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.Instance;
import system.Configuration;

public class ResourceManager {
	
	/*
	 * A resource manager for loading models, geometry, etc.
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static HashMap<String, CompositeSurface> surfaces;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		surfaces = new HashMap<String, CompositeSurface>();
		
		CompositeSurface cube = new Cube();
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
		CompositeSurface cs = surfaces.get(keyOrFilePath);
		
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
			
			CompositeSurface model = new CompositeObjectSurface(data);
			
			surfaces.put(keyOrFilePath, model);
			
			return;
		}
		
		//TODO: Add more loaders
	}
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/

}
