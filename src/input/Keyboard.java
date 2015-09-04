package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import process.logging.Logger;

public abstract class Keyboard implements KeyListener {
	
	/*
	 * A listener for keyboard events
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected HashMap<Integer, Boolean> keyDown;

	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public Keyboard()
	{
		keyDown = new HashMap<Integer, Boolean>();
	}


	/* *********************************************************************************************
	 * Keyboard Event Listener Overrides
	 * *********************************************************************************************/
	@Override
	public void keyTyped(KeyEvent e)
	{
		Logger.message(-7, "Revceived a keyTyped() call for Key[" + e.getKeyChar() + "].");
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		Logger.message(-7, "Revceived a keyPressed() call for Key[" + e.getKeyChar() + "].");
		
		keyDown.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		Logger.message(-7, "Revceived a keyReleased() call for Key[" + e.getKeyChar() + "].");

		keyDown.put(e.getKeyCode(), false);
	}
	

	/* *********************************************************************************************
	 * Key Query Methods
	 * *********************************************************************************************/
	public boolean keyIsDown(int keyCode)
	{
		Boolean down = keyDown.get(keyCode);
		if(down == null)
			return false;
		return down;
	}
	
	public boolean keyIsUp(int keyCode)
	{
		return !keyIsDown(keyCode);
	}
}
