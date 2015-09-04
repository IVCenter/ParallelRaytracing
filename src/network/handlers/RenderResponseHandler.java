package network.handlers;

import process.utils.RenderingUtils;

import raytrace.AnimationRenderer;
import raytrace.camera.Camera;
import raytrace.framework.Renderer;
import system.RenderingEngine;
import system.Constants;
import network.Message;
import network.NetworkRenderer;

public class RenderResponseHandler extends MessageHandler {
	
	/*
	 * A handler for messages that are the response of a render request
	 * 
	 * Ops:
	 * 
	 * 		Get reauestID and nodeID
	 * 		Check against the given node, and that node's known request id
	 * 		If match,
	 * 
	 * 			Pass all of this to network renderer
	 * 
	 * 			Scratch that, do it here
	 * 
	 * 			Get pixel data from message (width, height, int array, etc.)
	 * 			Compare to known quest params in node
	 * 			if match,
	 * 				write pixel data to pixel buffer
	 * 				update node to reflect job completion (remove quest id, etc.)
	 * 				store any extra data like timings, performance data, etc.
	 * 				if all render jobs are complete, 
	 * 					send a render response with the appropriate data
	 * 			
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RenderResponseHandler()
	{
		super();
		messageType = Message.Type.RenderResponse;
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
		
		
		
		//If the renderer is a network renderer, let it know we completed a request
		//TODO: There has to be a better way to detect if there is a network renderer in the render chain
		//		besides unwrapping it.
		//TODO: UpdateResponse has this same problem
		Renderer renderer = RenderingEngine.inst.getRenderer();
		if(renderer instanceof NetworkRenderer)
		{
			((NetworkRenderer)renderer).completedARequest();
		}else if(renderer instanceof AnimationRenderer && ((AnimationRenderer)renderer).getRenderer() instanceof NetworkRenderer)
		{
			((NetworkRenderer)((AnimationRenderer)renderer).getRenderer()).completedARequest();
		}
	}

}
