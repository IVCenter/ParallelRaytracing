package network.handlers;

import system.ApplicationDelegate;
import network.Message;

public class ShutdownHandler extends MessageHandler {
	
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
	public ShutdownHandler()
	{
		super();
		messageType = Message.Type.Shutdown;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		//Request shutdown via Application Delegate
		//This will guarantee that all children also receive a shutdown request
		ApplicationDelegate.inst.shutdown();
	}

}