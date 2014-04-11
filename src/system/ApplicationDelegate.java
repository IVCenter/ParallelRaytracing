package system;

import input.DefaultKeyboard;
import input.Keyboard;
import process.Job;
import process.logging.Logger;
import raster.PixelBuffer;
import raster.ScreenDrawer;
import raytrace.ConfigurableRayTracer;
import raytrace.ParallelRayTracer;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;

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
	 * 
	 * Handles the main loop, including synchronizing between children nodes
	 * 		-Update, Render, Keyboard callbacks, etc.
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ScreenDrawer screenDrawer;
	protected PixelBuffer pixelBuffer;
	protected Keyboard keyboard;
	
	protected Renderer renderer;
	
	
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
		Logger.progress(-1, "Initializing Application Delegate...");
		
		if(Configuration.isDrawingToScreen())
		{
			screenDrawer = new ScreenDrawer(Configuration.getScreenWidth(), Configuration.getScreenHeight());
			screenDrawer.setVerticalSynchronize(false);
			screenDrawer.setMsPerFrame(1000/12);
			
			pixelBuffer = screenDrawer.getPixelBuffer();
			
			keyboard = Configuration.getKeyboard();
			if(keyboard == null) {
				keyboard = new DefaultKeyboard(pixelBuffer);
			}
			screenDrawer.addKeyListener(keyboard);
			
		}else{
			pixelBuffer = new PixelBuffer(Configuration.getScreenWidth(), Configuration.getScreenHeight());
		}
		
		//TODO: Set renderer
		//	If leaf its a ray tracer
		//	If has children its a network distribution renderer
		if(Configuration.isLeaf())
		{
			renderer = new ConfigurableRayTracer(new ParallelRayTracer(), Configuration.getMasterScene());
		}
	}

	@Override
	protected void begin()
	{
		//Initialize the renderer
		//This may establish network connections, or block until one is established
		renderer.initialize();
		
		//If this node is the clock, start the main loop, else, sleep this thread
		//The sleep prevents this Job subclass from finalizing, and instead of using
		//	a loop for timing, this relies on a parent node to send timing signals
		if(Configuration.isClock())
		{
			startMainLoop();
		}else{
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize()
	{
		//
	}

	
	/* *********************************************************************************************
	 * Main Loop Methods
	 * *********************************************************************************************/
	protected void startMainLoop()
	{
		Logger.progress(-1, "Starting Main Loop...");
		
		//Configure the initial update data
		UpdateData udata = initUpdateData();
		
		//Configure the initial render data
		RenderData rdata = initRenderData();
		
		//Main Loop
		for(;;)//While spider face holds true
		{
			renderer.update(udata);
			
			renderer.render(rdata);
			
		}
	}
	
	private UpdateData initUpdateData()
	{
		//Configure the initial update data
		UpdateData udata = new UpdateData();
		
		return udata;
	}
	
	private RenderData initRenderData()
	{
		//Configure the initial render data
		RenderData rdata = new RenderData();
		rdata.setPixelBuffer(pixelBuffer);
		
		return rdata;
	}
}
