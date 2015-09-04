package network.handlers;

import process.logging.Logger;
import network.Message;

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
		Logger.message(-19, message.toString());
	}

}
