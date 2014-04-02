package system;

import process.Job;
import raster.ScreenDrawer;

public class ApplicationDelegate extends Job{
	
	/*
	 * If controller specified:
	 * 		attempt to register (loop until success, or timeout reached)
	 * 
	 * If controller:
	 * 		branch to controller code
	 * 
	 * If display == true:
	 * 		draw pixel data to screen
	 * 
	 * If has children nodes:
	 * 		distribute workload and calls to children
	 * 
	 * If leaf node:
	 * 		perform rendering on given workload
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ScreenDrawer screenDrawer;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ApplicationDelegate()
	{
		
	}
	

	/* *********************************************************************************************
	 * Initialization
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		screenDrawer = new ScreenDrawer(Configuration.getScreenWidth(), Configuration.getScreenHeight());
	}

	@Override
	protected void begin()
	{
		//
	}

	@Override
	protected void finalize()
	{
		//
	}
}
