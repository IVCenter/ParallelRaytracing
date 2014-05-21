package system;

import java.io.File;

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
		
		//Set up some defaults
		Configuration.setId(Constants.Default.NODE_ID);
		Configuration.setDrawToScreen(false);
		Configuration.setClock(false);
		Configuration.setLeaf(false);
		Configuration.setController(false);
		//Configuration.setWorkingDirectory("/home/linux/ieng6/oce/2d/rwest/NightSky/");
		
		
		//Setup debug configuration
		if(args.length > 0 && args[0].equals("DEBUG"))
		{
			Configuration.setId("Debug Node");
			Configuration.setScreenWidth(1368);
			Configuration.setScreenHeight(752);
			//Configuration.setScreenWidth(800);
			//Configuration.setScreenHeight(600);
			Configuration.setDrawToScreen(true);
			Configuration.setClock(true);
			Configuration.setLeaf(true);//true for local, false for networked
			Configuration.setController(true);
			//TODO: Need a reliable and relative folder to write to
			Configuration.setWorkingDirectory("/Users/Asylodus/Desktop/NightSky/");
			Configuration.setMasterScene(SceneLoader.load(Constants.SceneKeys.TEST5));
		}
		
		
		//Try to open the config file
		File configFile = null;
		try{
			
			configFile = new File(args[0]);
			
			//If we have a config file, 
			if(configFile.exists() && configFile.getName().endsWith(Constants.configurationFileExtension))
			{
				//TODO: Config file loader
				
				//TODO: Create folder structure if it doesn't exist under working directory
			}
			
		}catch(Exception e) {
			//Do nothing, this is acceptable
		}
		
		
		Logger.progress(-1, "Launching a Night Sky Node with ID:[" + Configuration.getId() + "]...");
		
		//Pass off control to the ApplicationDelegate
		ApplicationDelegate app = new ApplicationDelegate();
		app.execute(new Environment());
	}
	
	/*
	 * Local TOOD:
	 * 
	 * 		-Test distribution stability
	 * 		-Configuration loader
	 * 		-Intermediate results support
	 * 		-Registration Loop (controller compares on both ID and IP?)
	 * 		-Wrap network sender send calls in try catch
	 * 		-Compress pixels before sending?
	 */

}
