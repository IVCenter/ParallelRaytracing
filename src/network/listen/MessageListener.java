package network.listen;

import java.util.HashMap;

import network.Message;
import network.handlers.DefaultHandler;
import network.handlers.MessageHandler;

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
		Logger.progress(-21, "MessageListener: Listening...");
		
		if(message == null)
			return;
		
		String type = message.getType();
		
		if(type == null || type.equals(Message.Type.None))
			return;
		
		MessageHandler handler = messageHandlers.get(type);
		if(handler == null) {
			handler = defaultMessageHandler;
		}
		
		try {
			
			handler.handle(message);
		
		} catch(Exception e) {
			Logger.error(-21, "MessageListener: A handler accepted when parsing the message:\n" + message.toString());
		}
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
