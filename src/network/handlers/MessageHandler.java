package network.handlers;

import network.Message;
import network.Message.Type;

public abstract class MessageHandler {
	
	/*
	 * An interface for message handlers
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected String messageType = Message.Type.None;
	

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public abstract void handle(Message message);
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public String getMessageType() {
		return messageType;
	}
}
