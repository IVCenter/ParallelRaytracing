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
		//TODO: Pass off control to the Delegate
		Configuration.setScreenWidth(1024);
		Configuration.setScreenHeight(640);
		Configuration.setDrawToScreen(true);
		Configuration.setClock(true);
		Configuration.setLeaf(true);
		
		
		//Load the scene (TODO: Parse args for scene choice)
		String sceneKey = Constants.SceneKeys.TEST1;
		SceneLoader sceneLoader = new SceneLoader();
		Configuration.setMasterScene(sceneLoader.load(sceneKey));
		
		Logger.progress(-1, "Launching a Parallel Rendering Node with ID:[" + Configuration.getId() + "]...");
		
		//Pass off control to the ApplicationDelegate
		ApplicationDelegate app = new ApplicationDelegate();
		app.execute(new Environment());
	}

}
