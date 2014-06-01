package network;

import raytrace.camera.Camera;
import raytrace.data.UpdateData;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;

public class CommonMessageConstructor {
	
	/*
	 * A utility class for constructing messages that are commonly used.
	 */

	/* *********************************************************************************************
	 * Static Helper Methods
	 * *********************************************************************************************/
	/**
	 * 
	 * @return
	 */
	public static Message createEmptyMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.None);
		return message;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Message createRegistrationMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.Registration);

		message.getData().set(Constants.Message.NODE_ID, Configuration.getId());
		
		int totalCores = 0;
		if(ApplicationDelegate.inst.getNodeManager().getNodeCount() > 0)
		{
			for(Node node : ApplicationDelegate.inst.getNodeManager())
			{
				totalCores += node.getNumberOfCores();
			}
		}else{
			totalCores = Runtime.getRuntime().availableProcessors();
		}

		message.getData().set(Constants.Message.NODE_CORES, totalCores);
		
		return message;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Message createShutdownMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.Shutdown);
		
		return message;
	}

	/**
	 * 
	 * @param id
	 * @param screenWidth
	 * @param screenHeight
	 * @param isLeaf
	 * @param isDrawingToScreen
	 * @param isClock
	 * @param isController
	 * @param sceneKey
	 * @return
	 */
	public static Message createConfigurationMessage(String id, Integer screenWidth, Integer screenHeight,
			Boolean isLeaf, Boolean isDrawingToScreen, Boolean isClock, Boolean isController, String sceneKey)
	{
		Message message = new Message();
		message.setType(Message.Type.Configure);

		message.getData().set(Constants.Message.NODE_ID, id);
		message.getData().set(Constants.Message.SCREEN_WIDTH, screenWidth);
		message.getData().set(Constants.Message.SCREEN_HEIGHT, screenHeight);
		message.getData().set(Constants.Message.STATE_IS_LEAF, isLeaf);
		message.getData().set(Constants.Message.STATE_IS_DRAWINGTOSCREEN, isDrawingToScreen);
		message.getData().set(Constants.Message.STATE_IS_CLOCK, isClock);
		message.getData().set(Constants.Message.STATE_IS_CONTROLLER, isController);
		message.getData().set(Constants.Message.SCENE_KEY, sceneKey);
		
		return message;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static Message createUpdateRequestMessage(UpdateData data)
	{
		Message message = new Message();
		message.setType(Message.Type.UpdateRequest);
		
		message.getData().set(Constants.Message.DELTA_TIME, data.getDt());
		
		return message;
	}

	/**
	 * 
	 * @return
	 */
	public static Message createUpdateResponseMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.UpdateResponse);
		
		return message;
	}

	/**
	 * 
	 * @param camera
	 * @return
	 */
	public static Message createRenderRequestMessage(Camera camera)
	{
		Message message = new Message();
		message.setType(Message.Type.RenderRequest);
		
		message.getData().set(Constants.Message.NODE_CAMERA, camera);
		
		return message;
	}

	/**
	 * 
	 * @return
	 */
	public static Message createRenderResponseMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.RenderResponse);
		
		return message;
	}

	/**
	 * 
	 * @return
	 */
	public static Message createIntermediateRenderResponseMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.IntermediateRenderResponse);
		
		return message;
	}
}
