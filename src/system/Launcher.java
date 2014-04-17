package system;

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
		 * 		-
		 */
		
		//TODO: Parse the arguments
		//TODO: Set configuration values
		//Configuration.setScreenWidth(1368);
		//Configuration.setScreenHeight(752);
		Configuration.setScreenWidth(800);
		Configuration.setScreenHeight(600);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);
		Configuration.setLeaf(true);
		
		//TODO: Need a reliable and relative folder to write to
		Configuration.setWorkingDirectory("/Users/Asylodus/Desktop/NightSky/");
		
		
		//Load the scene (TODO: Parse args for scene choice)
		String sceneKey = Constants.SceneKeys.TEST3;
		SceneLoader sceneLoader = new SceneLoader();
		Configuration.setMasterScene(sceneLoader.load(sceneKey));
		
		Logger.progress(-1, "Launching a Parallel Rendering Node with ID:[" + Configuration.getId() + "]...");
		
		//Pass off control to the ApplicationDelegate
		ApplicationDelegate app = new ApplicationDelegate();
		app.execute(new Environment());
	}

}
