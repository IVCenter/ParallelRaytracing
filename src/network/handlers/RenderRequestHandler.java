package network.handlers;

import network.Message;

public class RenderRequestHandler extends MessageHandler {
	
	/*
	 * A message handler for render requests
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RenderRequestHandler()
	{
		super();
		messageType = Message.Type.RenderRequest;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		/*
		 * Ops:
		 * 
		 * 		-Get and Set active camera for current scene
		 * 		-Render
		 * 		-Iterate camera to extract pixels into adjacent array
		 * 		-Send back render response with:
		 * 			-pixels
		 * 			-camera used (so that the controller can remap pixel array to pixel buffer)
		 * 			-profiling info
		 * 			-
		 * 
		 * 		-
		 */
	}

}