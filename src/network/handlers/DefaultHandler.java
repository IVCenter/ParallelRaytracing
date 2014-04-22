package network.handlers;

import process.logging.Logger;
import network.Message;
import network.MessageHandler;

public class DefaultHandler extends MessageHandler {

	/*
	 * An default handler for messages
	 * Prints the contents of the given message to log
	 */
	/* *********************************************************************************************
	 * Override Methods
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		Logger.progress(-19, message.toString());
	}

}
