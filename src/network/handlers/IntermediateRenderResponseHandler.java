package network.handlers;

import process.utils.RenderingUtils;
import raytrace.camera.Camera;
import system.RenderingEngine;
import system.Constants;
import network.Message;

public class IntermediateRenderResponseHandler extends MessageHandler {
	
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
	public IntermediateRenderResponseHandler()
	{
		super();
		messageType = Message.Type.IntermediateRenderResponse;
	}
	
	
	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		//Get camera
		Camera camera = message.getData().get(Constants.Message.NODE_CAMERA);
		
		//Get pixels
		int[] pixels = message.getData().get(Constants.Message.NODE_PIXELS);
		
		//Unpack the pixels into the buffer
		RenderingUtils.unpackPixels(RenderingEngine.inst.getPixelBuffer().getPixels(), pixels, camera);
	}

}
