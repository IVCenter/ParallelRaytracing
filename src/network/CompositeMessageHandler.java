package network;

import java.util.Iterator;

public abstract class CompositeMessageHandler extends MessageHandler implements Iterable<MessageHandler> {
	
	/*
	 * A simple composite message handler
	 */
	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/

	@Override
	public Iterator<MessageHandler> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
