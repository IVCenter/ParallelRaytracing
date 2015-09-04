package network.handlers;

import process.logging.Logger;
import raytrace.scene.SceneLoader;
import system.RenderingEngine;
import system.Configuration;
import system.Constants;
import network.Message;
import network.Node;
import network.NodeManager;
import network.send.MessageSender;

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
		Logger.message(-27, "ConfigurationHander: Configuring this node...");
		
		//
		boolean didChangeScreenSize = false;
		
		
		
		String id = message.getData().get(Constants.Message.NODE_ID);
		if(id != null)
		{
			Logger.message(-27, "ConfigurationHander: Setting ID to [" + id + "].");
			Configuration.setId(id);
		}

		
		Integer screenWidth = message.getData().get(Constants.Message.SCREEN_WIDTH);
		if(screenWidth != null && Configuration.getScreenWidth() != screenWidth) 
		{
			Logger.message(-27, "ConfigurationHander: Setting screen width to [" + screenWidth + "].");
			Configuration.setScreenWidth(screenWidth);
			didChangeScreenSize = true;
		}
		
		
		Integer screenHeight = message.getData().get(Constants.Message.SCREEN_HEIGHT);
		if(screenHeight != null && Configuration.getScreenHeight() != screenHeight)
		{
			Logger.message(-27, "ConfigurationHander: Setting screen height to [" + screenHeight + "].");
			Configuration.setScreenHeight(screenHeight);
			didChangeScreenSize = true;
		}
		

		//TODO: How to we properly handle setting of configuration states?
		
		
		Boolean isLeaf = message.getData().get(Constants.Message.STATE_IS_LEAF);
		if(isLeaf != null && Configuration.isLeaf() != isLeaf)
		{
			Logger.message(-27, "ConfigurationHander: Setting leaf state to [" + isLeaf + "].");
			
			Configuration.setLeaf(isLeaf);
			
			RenderingEngine.inst.configureAsLeaf(isLeaf);
		}
		
		
		Boolean isDrawingToScreen = message.getData().get(Constants.Message.STATE_IS_DRAWINGTOSCREEN);
		if(isDrawingToScreen != null && Configuration.isDrawingToScreen() != isDrawingToScreen)
		{
			Logger.message(-27, "ConfigurationHander: Setting screen drawing state to [" + isDrawingToScreen + "].");
			
			Configuration.setDrawToScreen(isDrawingToScreen);

			RenderingEngine.inst.configureAsDrawingToScreen(isDrawingToScreen);
		}else if(didChangeScreenSize)
		{
			Logger.message(-27, "ConfigurationHander: Updating pixel buffer size.");
			RenderingEngine.inst.configureAsDrawingToScreen(Configuration.isDrawingToScreen());
		}
		
		
		Boolean isClock = message.getData().get(Constants.Message.STATE_IS_CLOCK);
		if(isClock != null && Configuration.isClock() != isClock)
		{
			Logger.message(-27, "ConfigurationHander: Setting clock state to [" + isClock + "].");
			
			Configuration.setClock(isClock);
			
			RenderingEngine.inst.configureAsClock(isClock);
		}
		
		
		Boolean isController = message.getData().get(Constants.Message.STATE_IS_CONTROLLER);
		if(isController != null && Configuration.isController() != isController)
		{
			Logger.message(-27, "ConfigurationHander: Setting controller state to [" + isController + "].");
			
			Configuration.setController(isController);
			
			RenderingEngine.inst.configureAsController(isController);
		}
		

		Boolean isRealtime = message.getData().get(Constants.Message.STATE_IS_REALTIME);
		if(isRealtime != null && Configuration.isRealTime() != isRealtime)
		{
			Logger.message(-27, "ConfigurationHander: Setting real-time state to [" + isRealtime + "].");
			
			Configuration.setRealTime(isRealtime);
			
			RenderingEngine.inst.configureAsRealTime(isRealtime);
		}
		
		
		String sceneKey = message.getData().get(Constants.Message.SCENE_KEY);
		if(sceneKey != null/* && 
				(Configuration.getMasterScene() == null || !Configuration.getMasterScene().getSceneKey().equals(sceneKey))*/)
		{
			Logger.message(-27, "ConfigurationHander: Setting scene to [" + sceneKey + "].");

			//Make sure we clean up the previous scene
			Configuration.setMasterScene(null);
			System.gc();
			
			//Load the new scene
			Configuration.setMasterScene(SceneLoader.load(sceneKey));
		}
		
		
		//If this node has children nodes, send the message along
		MessageSender sender = RenderingEngine.inst.getMessageSender();

		NodeManager nodes = RenderingEngine.inst.getNodeManager();
		for(Node node : nodes)
		{
			sender.send(message, node.getIp());
		}
	}

}