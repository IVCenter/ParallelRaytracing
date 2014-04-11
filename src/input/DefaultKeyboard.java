package input;

import java.awt.event.KeyEvent;

import process.logging.Logger;
import process.utils.TimeStamp;
import raster.PixelBuffer;
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
		Logger.progress(-7, "Revceived a keyReleased() call for Key[" + e.getKeyChar() + "].");
		
		//If P is pressed, take a screen shot of the current pixel buffer
		if(e.getKeyCode() == KeyEvent.VK_P)
		{
			pixelBuffer.writeToFile(Configuration.getWorkingDirectory() + "NightSky_Frame_" + TimeStamp.makeForFileName());
		}
		
		//If Ctrl is down, and X is released, kill the program
		if(keyIsDown(KeyEvent.VK_CONTROL) && e.getKeyCode() == KeyEvent.VK_X)
		{
			//Logger is threaded so there is a good chance it will never get around to processing this message
			//Logger.progress(-7, "Shuting down...");

			System.out.println("Shuting down...");
			System.exit(1);
		}
	}

}
