package network.handlers;

import network.Message;

public class IntermediateRenderResponseHandler extends MessageHandler {
	
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
	public IntermediateRenderResponseHandler()
	{
		super();
		messageType = Message.Type.IntermediateRenderResponse;
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
