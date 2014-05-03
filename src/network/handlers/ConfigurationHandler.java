package network.handlers;

import process.logging.Logger;
import raytrace.scene.SceneLoader;
import system.Configuration;
import system.Constants;
import network.Message;

public class ConfigurationHandler extends MessageHandler {
	
	/*
	 * 
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ConfigurationHandler()
	{
		super();
		messageType = Message.Type.Configure;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		/*
		 * Values to check data for:
		 * 
		 * 		Id
		 * 		Screen sizes
		 * 		Boolean flags:
		 * 			drawing to screen
		 * 			isLeaf
		 * 			isClock
		 * 		SceneKey (load scene if key changes)
		 * 		
		 */
		Logger.progress(-27, "ConfigurationHander: Configuring this node...");
		
		String id = message.getData().get(Constants.Message.NODE_ID);
		if(id != null) {
			Logger.progress(-27, "ConfigurationHander: Setting ID to [" + id + "].");
			Configuration.setId(id);
		}

		
		Integer screenWidth = message.getData().get(Constants.Message.SCREEN_WIDTH);
		if(screenWidth != null) {
			Logger.progress(-27, "ConfigurationHander: Setting screen width to [" + screenWidth + "].");
			Configuration.setScreenWidth(screenWidth);
		}
		
		Integer screenHeight = message.getData().get(Constants.Message.SCREEN_HEIGHT);
		if(screenHeight != null) {
			Logger.progress(-27, "ConfigurationHander: Setting screen height to [" + screenHeight + "].");
			Configuration.setScreenHeight(screenHeight);
		}
		

		Boolean isLeaf = message.getData().get(Constants.Message.STATE_IS_LEAF);
		if(isLeaf != null && Configuration.isLeaf() != isLeaf)
		{
			Logger.progress(-27, "ConfigurationHander: Setting leaf state to [" + isLeaf + "].");
			
			Configuration.setLeaf(isLeaf);
			
			//TODO: if true, set renderer to configurable+parallel
			//TOOD: if false, set to network
		}
		
		Boolean isDrawingToScreen = message.getData().get(Constants.Message.STATE_IS_DRAWINGTOSCREEN);
		if(isDrawingToScreen != null && Configuration.isDrawingToScreen() != isDrawingToScreen)
		{
			Logger.progress(-27, "ConfigurationHander: Setting screen drawing state to [" + isDrawingToScreen + "].");
			
			Configuration.setDrawToScreen(isDrawingToScreen);

			//TODO: If true, make screen drawer, set pixle buffer
			//TODO: If false, dispose screen drawer, make new pixel buffer
		}
		
		/*
		Boolean isClock = message.getData().get(Constants.Message.STATE_IS_CLOCK);
		if(isClock != null) {
			Logger.progress(-27, "ConfigurationHander: Setting clock state to [" + isClock + "].");
			Configuration.setClock(isClock);
		}
		*/
		
		
		String sceneKey = message.getData().get(Constants.Message.SCENE_KEY);
		if(sceneKey != null && !Configuration.getMasterScene().getSceneKey().equals(sceneKey))
		{
			Logger.progress(-27, "ConfigurationHander: Setting scene to [" + sceneKey + "].");
			Configuration.setMasterScene(SceneLoader.load(sceneKey));
		}
	}

}