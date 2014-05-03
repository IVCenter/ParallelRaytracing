package network.handlers;

import network.Message;

public class UpdateRequestHandler extends MessageHandler {
	
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
	public UpdateRequestHandler()
	{
		super();
		messageType = Message.Type.UpdateRequest;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		// TODO Auto-generated method stub
	}

}