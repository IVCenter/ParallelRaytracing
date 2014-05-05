package network.handlers;

import java.util.ArrayList;

import process.logging.Logger;
import process.utils.StringUtils;

import raytrace.camera.Camera;
import raytrace.data.RenderData;
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;
import math.Ray;
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
		
		//Get rendering
		ApplicationDelegate.inst.getRenderer().render(rdata);
		
		//Replace the scenes camera
		Configuration.getMasterScene().setActiveCamera(sceneCamera);
		
		
		//Send a response message with the camera and pixels
		Message response = CommonMessageConstructor.createRenderResponseMessage();
		response.getData().set(Constants.Message.NODE_CAMERA, camera);
		response.getData().set(Constants.Message.NODE_PIXELS, packPixels(
				ApplicationDelegate.inst.getPixelBuffer().getPixels(), camera));
		
		String returnIP = message.getData().get(Constants.Message.NODE_IP);
		ApplicationDelegate.inst.getMessageSender().send(response, returnIP);
	}
	
	/**
	 * 
	 * @param buffer
	 * @param camera
	 * @return
	 */
	private int[] packPixels(int[] buffer, Camera camera)
	{
		ArrayList<Integer> pixels = new ArrayList<Integer>();
		
		try{
			
			for(Ray ray : camera)
			{
				pixels.add(buffer[(int)(ray.getPixelY() * camera.getPixelWidth() + ray.getPixelX())]);
			}
			
			//Pack the pixels into an array
			int[] arr = new int[pixels.size()];
			for(int i = 0; i < pixels.size(); ++i)
			{
				arr[i] = pixels.get(i);
			}
			
			return arr;
			
		} catch(Exception e)
		{
			Logger.error(-30, "RenderRequestHandler: Encountered an error while packing pixels.");
			Logger.error(-30, StringUtils.stackTraceToString(e));
		}
		
		return new int[0];
	}

}