package system;

import java.io.File;

import file.nsconfig.ConfigFileLoader;
import folder.DirectoryManager;

import process.Environment;
import process.logging.Logger;
import raytrace.scene.SceneLoader;

public class Launcher {

	/* *********************************************************************************************
	 * Main
	 * *********************************************************************************************/
	
	public static void main(String[] args)
	{
		/*
		 * Args:
		 * 
		 * 		-[0] = Configuration File 
		 */
		
		//Try to open the config file
		File configFile = null;
		try{
			
			configFile = new File(args[0]);
			
			//If we have a config file, 
			if(configFile.exists() && configFile.getName().endsWith(Constants.configurationFileExtension))
			{
				ConfigFileLoader.load(configFile);

				//Create the directory structure for the current config
				DirectoryManager.createFolderStructure();
			}else{
				Logger.error(-1, "Launcher: Unable to open the configuration file [" + args[0] + "].");
			}
			
		}catch(Exception e) {
			//Do nothing, this is acceptable
		}
		
		
		//Setup debug configuration
		if(args.length > 0 && args[0].equals("DEBUG"))
		{
			loadDebugConfiguration();
		}
		
		
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
		Configuration.setMasterScene(SceneLoader.load(Constants.SceneKeys.TEST6));

		//Create the directory structure for the debug config
		DirectoryManager.createFolderStructure();
	}
	
	/*
	 * Local TODO for NightSky:
	 * 
	 * 		#An entry that starts with (LP) is considered Low Priority
	 * 
	 * 		-Test distribution stability and performance on caves
	 * 		-(LP) Compress pixels before sending?
	 * 		-AddMaterial support for UV tangents (if uv coords are bad, use existing YAXIS methods)
	 * 		-(LP) Add Quad support to Triangle
	 * 		-Discrete texture class that loads bmps, pngs, etc.
	 * 		-A transform wrapper for texture
	 * 		-[BROKEN] Cylinder (cap)
	 * 		-[BROKEN] AABVH improperly handles cylinders
	 * 
	 * 		-Primitive Geometry Tesellation
	 * 			-Cube
	 * 			-Sphere
	 * 			-Cylinder
	 * 
	 * 		-Progressive Tracer
	 * 			-Sobol psuedo random instead of stratified?
	 * 			-Iterator memorization?
	 * 			-Ray index used for multiply and sum-in
	 * 			-Keyboard keys for moving the camera
	 * 			-Detecting a dirty camera
	 * 			-Progressive Tracer...
	 * 				-Cycles on camera until camera has a request for next frame
	 * 				-request can be caused by camera.update
	 * 				-or 
	 * 
	 * 		-More Materials
	 * 		-Brick pattern + coloring + blend + material
	 * 		
	 * 		-Normal maps from textures
	 * 			-gradient based?
	 * 
	 * 		-Spatial geometry maps
	 * 
	 * 		-Sobol pseudo noise generator
	 * 
	 * 		-Subsurface Scattering
	 * 
	 * 		-Texture blending API
	 * 		-Material Generation API
	 * 		-Loading/Saving Materials from/to disk
	 * 
	 * 		-Geometry Generation API
	 * 			-Ex: Rocks, mechanical shapes, abstract shapes
	 * 		-Geometry placement API
	 * 			-Automatically layout generated geometry onto a surface
	 * 				-Ex: Create a BSphere via BB, shoot rays at the surface, place objects at intersections
	 * 
	 * 		-Post Processing Pass
	 * 		-Tone Mapping
	 * 
	 * 		-Camera Lens API
	 * 
	 */

}
