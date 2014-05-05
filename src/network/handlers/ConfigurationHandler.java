package network.handlers;

import process.logging.Logger;
import raytrace.scene.SceneLoader;
import system.ApplicationDelegate;
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
		 * 			isController
		 * 		SceneKey (load scene if key changes)
		 * 		
		 */
		Logger.progress(-27, "ConfigurationHander: Configuring this node...");
		
		//
		boolean didChangeScreenSize = false;
		
		
		
		String id = message.getData().get(Constants.Message.NODE_ID);
		if(id != null)
		{
			Logger.progress(-27, "ConfigurationHander: Setting ID to [" + id + "].");
			Configuration.setId(id);
		}

		
		Integer screenWidth = message.getData().get(Constants.Message.SCREEN_WIDTH);
		if(screenWidth != null && Configuration.getScreenWidth() != screenWidth) 
		{
			Logger.progress(-27, "ConfigurationHander: Setting screen width to [" + screenWidth + "].");
			Configuration.setScreenWidth(screenWidth);
			didChangeScreenSize = true;
		}
		
		
		Integer screenHeight = message.getData().get(Constants.Message.SCREEN_HEIGHT);
		if(screenHeight != null && Configuration.getScreenHeight() != screenHeight)
		{
			Logger.progress(-27, "ConfigurationHander: Setting screen height to [" + screenHeight + "].");
			Configuration.setScreenHeight(screenHeight);
			didChangeScreenSize = true;
		}
		

		Boolean isLeaf = message.getData().get(Constants.Message.STATE_IS_LEAF);
		if(isLeaf != null && Configuration.isLeaf() != isLeaf)
		{
			Logger.progress(-27, "ConfigurationHander: Setting leaf state to [" + isLeaf + "].");
			
			Configuration.setLeaf(isLeaf);
			
			ApplicationDelegate.inst.configureAsLeaf(isLeaf);
		}
		
		
		Boolean isDrawingToScreen = message.getData().get(Constants.Message.STATE_IS_DRAWINGTOSCREEN);
		if(isDrawingToScreen != null && Configuration.isDrawingToScreen() != isDrawingToScreen)
		{
			Logger.progress(-27, "ConfigurationHander: Setting screen drawing state to [" + isDrawingToScreen + "].");
			
			Configuration.setDrawToScreen(isDrawingToScreen);

			ApplicationDelegate.inst.configureAsDrawingToScreen(isDrawingToScreen);
		}else if(didChangeScreenSize)
		{
			Logger.progress(-27, "ConfigurationHander: Updating pixel buffer size.");
			ApplicationDelegate.inst.configureAsDrawingToScreen(Configuration.isDrawingToScreen());
		}
		
		
		Boolean isClock = message.getData().get(Constants.Message.STATE_IS_CLOCK);
		if(isClock != null && Configuration.isClock() != isClock)
		{
			Logger.progress(-27, "ConfigurationHander: Setting clock state to [" + isClock + "].");
			
			Configuration.setClock(isClock);
			
			ApplicationDelegate.inst.configureAsClock(isClock);
		}
		
		
		Boolean isController = message.getData().get(Constants.Message.STATE_IS_CONTROLLER);
		if(isController != null && Configuration.isController() != isController)
		{
			Logger.progress(-27, "ConfigurationHander: Setting controller state to [" + isController + "].");
			
			Configuration.setController(isController);
			
			ApplicationDelegate.inst.configureAsController(isController);
		}
		
		
		
		String sceneKey = message.getData().get(Constants.Message.SCENE_KEY);
		if(sceneKey != null && 
				(Configuration.getMasterScene() == null || !Configuration.getMasterScene().getSceneKey().equals(sceneKey)))
		{
			Logger.progress(-27, "ConfigurationHander: Setting scene to [" + sceneKey + "].");
			Configuration.setMasterScene(SceneLoader.load(sceneKey));
		}
	}

}