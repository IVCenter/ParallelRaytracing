package network;

import process.Environment;

public class Message {

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	
	//Stores the data payload for a message
	public final MessageData data = new MessageData();
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public Message()
	{
		//
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
	public static enum Type {
		None,			//A message with no type data (silently dropped)
		};
		
	/**
	 * Renames the class Environment to MessageData.
	 * An Environment is a representation of a set of variables and their values
	 * Since it is fairly light weight, and full-featured, we'll reuse it for
	 * sending data between nodes.
	 */
	@SuppressWarnings("serial")
	public static final class MessageData extends Environment{};

}
