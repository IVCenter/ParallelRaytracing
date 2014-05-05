package network;

import java.io.Serializable;

import process.Environment;

public class Message implements Serializable {
	
	/*
	 * A message object for transmitting data
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static final long serialVersionUID = 1L;
	

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//Stores the data payload for a message
	protected final MessageData data = new MessageData();
	protected String type = Message.Type.None;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public Message()
	{
		//
	}
	

	/* *********************************************************************************************
	 * toString Override
	 * *********************************************************************************************/
	@Override
	public String toString()
	{
		//TODO
		return "Message: Type[" + type + "].";
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MessageData getData() {
		return data;
	}
	

	/* *********************************************************************************************
	 * Inner Classes and Enums
	 * *********************************************************************************************/


	/**
	 * Each sendable message can indicate a type.
	 * The type specified will be used by the receiving node to decide how to
	 * handle the message.
	 * 
	 * A message with the default type of None will be dropped silently
	 */
	public static class Type {
		public static final String None = "NONE";	//A message with no type data (silently dropped)
		public static final String Debug = "DEBUG";						
		public static final String RenderRequest = "RenderRequest";
		public static final String RenderResponse = "RenderResponse";
		public static final String IntermediateRenderResponse = "IntermediateRenderResponse";
		public static final String UpdateRequest = "UpdateRequest";
		public static final String UpdateResponse = "UpdateResponse";
		public static final String Registration = "Registration";
		public static final String Configure = "Configuration";
		public static final String Shutdown = "Shutdown";
	};
		
	/**
	 * Renames the class Environment to MessageData.
	 * An Environment is a representation of a set of variables and their values
	 * Since it is fairly light weight, and full-featured, we'll reuse it for
	 * sending data between nodes.
	 */
	@SuppressWarnings("serial")
	public static final class MessageData extends Environment implements Serializable {};
}
