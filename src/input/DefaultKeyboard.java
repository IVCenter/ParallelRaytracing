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
import system.ApplicationDelegate;
import system.Configuration;
import system.Constants;

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
		Logger.progress(-7, "Revceived a keyReleased() call for Key[" + e.getKeyChar() + "].");
		
		//If P is pressed, take a screen shot of the current pixel buffer
		if(e.getKeyCode() == KeyEvent.VK_P)
		{
			pixelBuffer.writeToFile(Configuration.getWorkingDirectory() + Configuration.getScreenshotSubDirectory()
					+ "NightSky_Frame_" + TimeStamp.makeForFileName());
		}
		
		//If Ctrl is down, and X is released, kill the program
		if(keyIsDown(KeyEvent.VK_CONTROL) && e.getKeyCode() == KeyEvent.VK_X)
		{
			//Logger is threaded so there is a good chance it will never get around to processing this message
			//Logger.progress(-7, "Shuting down...");

			System.out.println("Shuting down...");
			System.exit(1);
		}
		
		//If N is released, print the current nodes under the node manager
		if(e.getKeyCode() == KeyEvent.VK_N)
		{
			Logger.progress(-23, "Node Information:");
			
			NodeManager nodes = ApplicationDelegate.inst.getNodeManager();
			
			for(Node node : nodes)
			{
				Logger.progress(-32, node.toString());
			}
		}
		
		//If S is released, start/stop the renderer
		if(e.getKeyCode() == KeyEvent.VK_S)
		{
			if(ApplicationDelegate.inst.isStarted())
			{
				Logger.progress(-24, "Stopping Rendering...");
				ApplicationDelegate.inst.stop();
			}else{
				Logger.progress(-24, "Starting Rendering...");
				ApplicationDelegate.inst.start();	
			}
		}
		
		//If C, configure all known nodes to slave mode
		if(e.getKeyCode() == KeyEvent.VK_C)
		{
			Message message;
			
			MessageSender sender = ApplicationDelegate.inst.getMessageSender();

			NodeManager nodes = ApplicationDelegate.inst.getNodeManager();
			for(Node node : nodes)
			{
				message = CommonMessageConstructor.createConfigurationMessage(
						node.getId(), 
						Configuration.getScreenWidth(),
						Configuration.getScreenHeight(), 
						true,								//Leaf is true
						false, 								//Drawing to screen is false
						false, 								//Clock is false
						Configuration.getMasterScene().getSceneKey());
				sender.send(message, node.getIp());
			}
		}
		
	}

}
