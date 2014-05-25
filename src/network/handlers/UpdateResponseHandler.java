package network.handlers;

import network.Message;
import network.NetworkRenderer;
import raytrace.AnimationRenderer;
import raytrace.framework.Renderer;
import system.ApplicationDelegate;

public class UpdateResponseHandler extends MessageHandler {
	
	/*
	 * A handler for messages that are the response of an update request
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public UpdateResponseHandler()
	{
		super();
		messageType = Message.Type.UpdateResponse;
	}
	

	/* *********************************************************************************************
	 * MessageHandler Override
	 * *********************************************************************************************/
	@Override
	public void handle(Message message)
	{
		//TODO: Anything else?
		
		//If the renderer is a network renderer, let it know we completed a request
		Renderer renderer = ApplicationDelegate.inst.getRenderer();
		if(renderer instanceof NetworkRenderer)
		{
			((NetworkRenderer)renderer).completedARequest();
		}else if(renderer instanceof AnimationRenderer && ((AnimationRenderer)renderer).getRenderer() instanceof NetworkRenderer)
		{
			((NetworkRenderer)((AnimationRenderer)renderer).getRenderer()).completedARequest();
		}
	}

}