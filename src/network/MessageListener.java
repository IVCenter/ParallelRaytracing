package network;

import java.util.HashMap;

import network.handlers.DefaultHandler;

import process.logging.Logger;

public abstract class MessageListener {
	
	/*
	 * A simple message listener base class
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected HashMap<String, MessageHandler> messageHandlers;
	protected MessageHandler defaultMessageHandler;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MessageListener()
	{
		messageHandlers = new HashMap<String, MessageHandler>();
		defaultMessageHandler = new DefaultHandler();
	}
	
	
	/* *********************************************************************************************
	 * Listening Methods
	 * *********************************************************************************************/
	public void listen(Message message)
	{
		String type = message.getType();
		
		if(type == null || type.equals(Message.Type.None))
			return;
		
		MessageHandler handler = messageHandlers.get(type);
		if(handler == null) {
			handler = defaultMessageHandler;
		}
		
		handler.handle(message);
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public void addMessageHandler(MessageHandler handler)
	{
		if(handler.getMessageType().equals(Message.Type.None)) {
			Logger.warning(-18, "MessageListener: A Message Handler of type None can not be added to a Message Listener.");
			return;
		}
		
		MessageHandler existingHandler = messageHandlers.get(handler.getMessageType());
		
		if(existingHandler != null) {
			Logger.warning(-18, "MessageListener: Exisitng Message Handler for key [" + existingHandler.getMessageType() + 
								"] will be overwritten.");
		}
		
		messageHandlers.put(handler.getMessageType(), handler);
	}
	
	public HashMap<String, MessageHandler> getMessageHandlers()
	{
		return messageHandlers;
	}
	
	public MessageHandler removeMessageHandler(String messageType)
	{
		return messageHandlers.remove(messageType);
	}

	public MessageHandler getDefaultMessageHandler() {
		return defaultMessageHandler;
	}

	public void setDefaultMessageHandler(MessageHandler defaultMessageHandler) {
		if(defaultMessageHandler == null) {
			Logger.warning(-18, "MessageListener: A null message handler can not be used as a default handler.");
			return;
		}
		this.defaultMessageHandler = defaultMessageHandler;
	}
}
