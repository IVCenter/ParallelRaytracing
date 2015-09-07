package system;

import java.io.File;

import file.nsconfig.ConfigFileLoader;
import process.logging.Logger;
import raytrace.scene.EmptyScene;

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
			}else{
				Logger.error(-1, "Launcher: Unable to open the configuration file [" + args[0] + "].");
			}
			
		}catch(Exception e) {
			//Do nothing, this is acceptable
		}
		
		
		//Setup debug configuration
		if(args.length == 0 || (args.length > 0 && args[0].equals("DEBUG")))
		{
			loadDebugConfiguration();
		}
		
		
		Logger.message(-1, "Launching a Night Sky Node with ID:[" + Configuration.getId() + "]...");
		
		//Pass off control to the ApplicationDelegate
		RenderingEngine engine = new RenderingEngine();
		engine.execute();
	}
	
	private static void loadDebugConfiguration()
	{
		//Feel free to over write these with your own settings
		Configuration.setId("Debug Node");
		Configuration.setScreenWidth(1280);
		Configuration.setScreenHeight(720);
		Configuration.setRenderWidth(1280);
		Configuration.setRenderHeight(720);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);//The top most node must have clock set to true (this includes stand-alone nodes)
		Configuration.setLeaf(true);//true for stand-alone and leaves, false for non-leaf nodes when networked
		Configuration.setController(true);
		Configuration.setWorkingDirectory("");
		Configuration.setMasterScene(new EmptyScene());
	}

}
