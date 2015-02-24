package network.handlers;

import process.utils.RenderingUtils;

import raytrace.camera.Camera;
import raytrace.data.RenderData;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;
import network.CommonMessageConstructor;
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
		
		//Store the existing active camera
		Camera sceneCamera = Configuration.getMasterScene().getActiveCamera();
		
		//Replace it with the decomposed camera
		Camera camera = message.getData().get(Constants.Message.NODE_CAMERA);
		Configuration.getMasterScene().setActiveCamera(camera);
		
		//Setup a render data object
		RenderData rdata = new RenderData();
		rdata.setPixelBuffer(ApplicationDelegate.inst.getPixelBuffer());
		rdata.setScene(Configuration.getMasterScene());
		rdata.setCamera(camera);
		
		//If not rendering in realtime
		Thread intermediateThread = null;
		if(!Configuration.isRealTime())
		{
			intermediateThread = startIntermediateResultsLoop(camera.duplicate(), (String)message.getData().get(Constants.Message.NODE_IP));
			intermediateThread.start();
		}
		
		//Get rendering
		ApplicationDelegate.inst.getRenderer().render(rdata);
		
		//If an intermediate thread was created, stop it here.
		if(intermediateThread != null)
		{
			intermediateThread.interrupt();
			synchronized(this){/**/}//Block until we have access
		}
		
		
		//Replace the scenes camera
		Configuration.getMasterScene().setActiveCamera(sceneCamera);
		
		
		//TODO: Compress pixels?
		//Send a response message with the camera and pixels
		Camera freshCamera = camera.duplicate();
		Message response = CommonMessageConstructor.createRenderResponseMessage();
		response.getData().set(Constants.Message.NODE_CAMERA, freshCamera);
		response.getData().set(Constants.Message.NODE_PIXELS, RenderingUtils.packPixels(
				ApplicationDelegate.inst.getPixelBuffer().getPixels(), freshCamera));
		
		String returnIP = message.getData().get(Constants.Message.NODE_IP);
		ApplicationDelegate.inst.getMessageSender().send(response, returnIP);
	}
	
	private Thread startIntermediateResultsLoop(final Camera camera, final String controllerIP)
	{
		Thread intermediateThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				synchronized(this)
				{
					for(;!Thread.currentThread().isInterrupted();)//While not interupted
					{
						//Sleep for some period of time
						try {
							Thread.sleep(Constants.Default.INTERMEDIATE_RESULT_LOOP_SLEEP_TIME);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
	
						//Send an intermedaite result message
						Message response = CommonMessageConstructor.createIntermediateRenderResponseMessage();
						response.getData().set(Constants.Message.NODE_CAMERA, camera);
						response.getData().set(Constants.Message.NODE_PIXELS, RenderingUtils.packPixels(
								ApplicationDelegate.inst.getPixelBuffer().getPixels(), camera));
						
						ApplicationDelegate.inst.getMessageSender().send(response, controllerIP);
					}
				}
			}
		});
		
		return intermediateThread;
	}

}