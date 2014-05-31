package network.handlers;

import raytrace.data.UpdateData;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;
import network.CommonMessageConstructor;
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
		//
		Double deltaTime = message.getData().get(Constants.Message.DELTA_TIME);
		
		//Setup a render data object
		UpdateData udata = new UpdateData();
		udata.setDt(deltaTime);
		udata.setScene(Configuration.getMasterScene());
		
		//Get rendering
		ApplicationDelegate.inst.getRenderer().update(udata);
		
		
		//Send a response message
		Message response = CommonMessageConstructor.createUpdateResponseMessage();
		
		String returnIP = message.getData().get(Constants.Message.NODE_IP);
		ApplicationDelegate.inst.getMessageSender().send(response, returnIP);
	}

}