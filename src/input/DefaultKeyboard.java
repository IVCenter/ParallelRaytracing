package input;

import java.awt.event.KeyEvent;

import network.CommonMessageConstructor;
import network.Message;
import network.Node;
import network.NodeManager;
import network.send.MessageSender;

import process.logging.Logger;
import process.utils.TimeStamp;
import raster.PixelBuffer;
import raytrace.AnimationRenderer;
import raytrace.camera.ProgrammableCamera;
import raytrace.framework.Renderer;
import system.RenderingEngine;
import system.Configuration;

public class DefaultKeyboard extends Keyboard {
	
	/*
	 * A default implementation for Keyboard
	 */
	/* *********************************************************************************************
	 * Keyboard Event Listener Overrides
	 * *********************************************************************************************/
	protected PixelBuffer pixelBuffer;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DefaultKeyboard(PixelBuffer pixelBuffer)
	{
		super();
		this.pixelBuffer = pixelBuffer;
	}
	
	
	/* *********************************************************************************************
	 * Keyboard Event Listener Overrides
	 * *********************************************************************************************/
	@Override
	public void keyReleased(KeyEvent e)
	{
		Logger.message(-7, "Revceived a keyReleased() call for Key[" + e.getKeyChar() + "].");
		
		//If P is pressed, take a screen shot of the current pixel buffer
		if(e.getKeyCode() == KeyEvent.VK_P)
		{
			if(Configuration.canWriteToDisk())
			{
				pixelBuffer.writeToFile(Configuration.getWorkingDirectory() + Configuration.getScreenshotSubDirectory()
						+ Configuration.getFrameFileNamePrefix() + TimeStamp.makeForFileName());
			}
		}
		
		//If Ctrl is down, and X is released, kill the program
		if(keyIsDown(KeyEvent.VK_CONTROL) && e.getKeyCode() == KeyEvent.VK_X)
		{
			RenderingEngine.inst.shutdown();
		}
		
		//If N is released, print the current nodes under the node manager
		if(e.getKeyCode() == KeyEvent.VK_N)
		{
			Logger.message(-23, "Node Information:");
			
			NodeManager nodes = RenderingEngine.inst.getNodeManager();
			
			int totalCores = 0;
			for(Node node : nodes)
			{
				totalCores += node.getNumberOfCores();
				Logger.message(-32, node.toString());
			}
			
			Logger.message(-23, "Total Rendering Cores: " + totalCores);
		}
		
		//If S is released, start/stop the renderer
		if(e.getKeyCode() == KeyEvent.VK_S)
		{
			if(RenderingEngine.inst.isStarted())
			{
				Logger.message(-24, "Stopping Rendering...");
				RenderingEngine.inst.stop();
			}else{
				Logger.message(-24, "Starting Rendering...");
				RenderingEngine.inst.start();	
			}
		}
		
		//If C, configure all known nodes to slave mode
		if(e.getKeyCode() == KeyEvent.VK_C)
		{
			Message message;
			
			MessageSender sender = RenderingEngine.inst.getMessageSender();

			NodeManager nodes = RenderingEngine.inst.getNodeManager();
			for(Node node : nodes)
			{
				message = CommonMessageConstructor.createConfigurationMessage(
						node.getId(), 
						Configuration.getScreenWidth(),
						Configuration.getScreenHeight(), 
						true,								//Leaf is true
						false, 								//Drawing to screen is false
						false, 								//Clock is false
						false, 								//Controller is false
						Configuration.isRealTime(), 								//Controller is false
						Configuration.getMasterScene().getSceneKey());
				sender.send(message, node.getIp());
			}
		}
		
		//If R is pressed, start/stop the animation renderer
		if(e.getKeyCode() == KeyEvent.VK_R)
		{
			Renderer renderer = RenderingEngine.inst.getRenderer();
			if(renderer instanceof AnimationRenderer)
			{
				AnimationRenderer ar = (AnimationRenderer)renderer;
				if(ar.isRecording())
					ar.stopRecording();
				else
					ar.startRecording();
			}
		}
		

		
		//If 1 is pressed, decrease render resolution
		if(e.getKeyCode() == KeyEvent.VK_1)
		{
			Configuration.setRenderWidth((int)Math.max(Configuration.getRenderWidth() * 0.9, 2.0));
			Configuration.setRenderHeight((int)Math.max(Configuration.getRenderHeight() * 0.9, 2.0));
			Configuration.getMasterScene().getActiveCamera().setDirty(true);
		}
		
		//If 2 is pressed, increase render resolution
		if(e.getKeyCode() == KeyEvent.VK_2)
		{
			Configuration.setRenderWidth((int)Math.min(Configuration.getRenderWidth() / 0.9, Configuration.getScreenWidth()));
			Configuration.setRenderHeight((int)Math.min(Configuration.getRenderHeight() / 0.9, Configuration.getScreenHeight()));
			Configuration.getMasterScene().getActiveCamera().setDirty(true);
		}
		
		//If 3 is pressed, reset render resolution to screen resolution
		if(e.getKeyCode() == KeyEvent.VK_3)
		{
			Configuration.setRenderWidth(Configuration.getScreenWidth());
			Configuration.setRenderHeight(Configuration.getScreenHeight());
			Configuration.getMasterScene().getActiveCamera().setDirty(true);
		}
		
		//If 4 is pressed, decrease sample count
		if(e.getKeyCode() == KeyEvent.VK_4)
		{
			if(Configuration.getMasterScene().getActiveCamera() instanceof ProgrammableCamera)
			{
				ProgrammableCamera cam = (ProgrammableCamera)Configuration.getMasterScene().getActiveCamera();
				cam.setSuperSamplingLevel(Math.max(cam.getSuperSamplingLevel()-1, 1));
			}
		}
		
		//If 5 is pressed, decrease sample count
		if(e.getKeyCode() == KeyEvent.VK_5)
		{
			if(Configuration.getMasterScene().getActiveCamera() instanceof ProgrammableCamera)
			{
				ProgrammableCamera cam = (ProgrammableCamera)Configuration.getMasterScene().getActiveCamera();
				cam.setSuperSamplingLevel(cam.getSuperSamplingLevel()+1);
			}
		}
		
		
		
		
	}

}
