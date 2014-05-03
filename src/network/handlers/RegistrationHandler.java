package network.handlers;

import process.logging.Logger;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;
import network.Node;

import network.Message;

public class RegistrationHandler extends MessageHandler {
	
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
	public RegistrationHandler()
	{
		super();
		messageType = Message.Type.Registration;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		// TODO Auto-generated method stub
		/*
		 * Check ID against existing nodes id table
		 * if exists, ignore
		 * if unassigned, make one
		 * create a node object, add to node manager (and node id table internally)
		 * 
		 * Get node IP (store in node object)
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		Logger.progress(-26, "RegistrationHandler: Registering new node...");
		
		/*
		 * Create a new node object
		 */
		
		Node node = new Node();
		
		
		String id = message.getData().get(Constants.Message.NODE_ID);
		
		if(id == null || id.isEmpty() || id.equals(Constants.Default.NODE_ID))
		{
			id = Configuration.getNodeIdPrefix() + node.getNodeNumber();
		}
		
		node.setId(id);
		
		
		String ip = message.getData().get(Constants.Message.NODE_IP);
		
		if(ip == null)
		{
			ip = "Unknown IP Address";
		}
		
		node.setIp(ip);
		
		node.setRegistrationTime(System.currentTimeMillis());
		node.setLastMessageTime(System.currentTimeMillis());
		
		
		ApplicationDelegate.inst.getNodeManager().addNode(node);
		
		
		/*
		 * Send a configuration message
		 */
		//Message message = CommonMessageConstructor.createConfigurationMessage();
		
	}

}