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
	 * 		-Documentation
	 * 			-Code clean up
	 * 			-Javadoc comments
	 * 			-Wiki
	 * 
	 * 		-Network
	 * 			-Pixel Buffer compression
	 * 			-Clock edge prediction (pre-predict when the next clock edge (render call) will come 
	 * 				and begin rendering as soon as resources are available)
	 * 			-Open message streams (open them once and use a thread to keep them live)
	 * 			-
	 * 
	 * 		-(LP)User Interface
	 * 			-Web-based (Polymer?)
	 * 			-System control, Node statistics, scheudle rendering jobs, etc.
	 * 
	 * 		-Camera
	 * 			-Lens API
	 * 
	 * 		-Geometry
	 * 			-Support for Quads (two triangle? or direct coplanar? or interpolated?)
	 * 			-Cylinder does not render correctly (cap, and under transforms)
	 * 			-AABVH does not handle cylinders correctly (bounding box? transform issue?)
	 * 			-Cylinder tessellation
	 * 			-3D lowpass filter for smoothing vertices (gaussian?)
	 * 			-Geometry IDs for all pieces of geometry
	 * 				-Returned as part of intersection data
	 * 				-Composite surfaces like meshes, etc. overwrite the id returned by their children o intersect
	 * 		
	 * 		-Volume Rendering
	 * 			-Marching cubes
	 * 				-Mesh generation
	 * 				-Direct tracing
	 * 			-Ray Marching
	 * 
	 * 		-Material
	 * 			-Support for UV tangents (calculated from UV coords, fallback to primitive defaults, else 
	 * 				YAxis method if not available)
	 * 			-Brick pattern + coloring + blend + material
	 * 			-More Materials
	 * 			-Illustration materials
	 * 
	 * 		-Mapping
	 * 			-Discrete texture loading class (support for ppm, png, jpg, bmp, etc.)
	 * 				-Wrapping modes (clamp, mirror, repeat, single color, etc.  for both axes)
	 * 			-
	 * 
	 * 		-Math
	 * 			-Sobol random number generator
	 * 			-Vector4 -> Vector3
	 * 
	 * 		-File Formats
	 * 			-Import MMD
	 * 			-Material format (save/load, JSON?)
	 * 			-
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
	 * 
	 * 		-Per-pixel Post Processing Pass
	 * 			-For line/edge rendering, etc.
	 * 		-Image Post Processing Pass
	 * 			-Tone Mapping
	 * 			-Bloom
	 * 			-HDR
	 * 			-Exposure
	 * 			-Shading API
	 * 				-Painting style, line drawing, etc.
	 * 			-Etc.
	 * 
	 * 		-Camera Lens API
	 * 
	 */

}
