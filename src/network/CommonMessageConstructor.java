package network;

import system.Configuration;
import system.Constants;

public class CommonMessageConstructor {
	
	/*
	 * A utility class for contructing mesages thare are commonly used.
	 */

	/* *********************************************************************************************
	 * Static Helper Methods
	 * *********************************************************************************************/
	public static Message createEmptyMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.None);
		return message;
	}
	
	public static Message createRegistrationMessage()
	{
		Message message = new Message();
		message.setType(Message.Type.Registration);
		
		message.getData().set(Constants.Message.NODE_ID, Configuration.getId());
		
		return message;
	}

	public static Message createConfigurationMessage(String id, Integer screenWidth, Integer screenHeight,
			Boolean isLeaf, Boolean isDrawingToScreen, Boolean isClock, String sceneKey)
	{
		Message message = new Message();
		message.setType(Message.Type.Configure);

		message.getData().set(Constants.Message.NODE_ID, id);
		message.getData().set(Constants.Message.SCREEN_WIDTH, screenWidth);
		message.getData().set(Constants.Message.SCREEN_HEIGHT, screenHeight);
		message.getData().set(Constants.Message.STATE_IS_LEAF, isLeaf);
		message.getData().set(Constants.Message.STATE_IS_DRAWINGTOSCREEN, isDrawingToScreen);
		message.getData().set(Constants.Message.STATE_IS_CLOCK, isClock);
		message.getData().set(Constants.Message.SCENE_KEY, sceneKey);
		
		return message;
	}
}
