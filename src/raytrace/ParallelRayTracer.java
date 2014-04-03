package raytrace;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import process.logging.Logger;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.framework.Tracer;
import raytrace.surfaces.CompositeSurface;

public class ParallelRayTracer implements Tracer {
	
	/*
	 * A parallel ray tracers that spawns workers based on available resources
	 */
	/* *********************************************************************************************
	 * Local Constants
	 * *********************************************************************************************/
	protected static final String k = "";
	private static AtomicInteger idPool = new AtomicInteger(0);
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected boolean wasNotified = false;
	protected boolean isComplete = false;
	protected Object traceLock = new Object();
	protected Object mainLock = new Object();
	protected AtomicInteger completedCount = new AtomicInteger(0);
	protected int threadCount = 1;
	
	protected int callID = 0;

	protected ArrayList<SynchronizingWorker> workers;
	protected ArrayList<Thread> threadPool;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelRayTracer()
	{
		int cores = Runtime.getRuntime().availableProcessors();
		threadCount = cores;
		workers = new ArrayList<SynchronizingWorker>(cores);
		threadPool = new ArrayList<Thread>(cores);
		
		//Create as many workers as there are cores
		SynchronizingWorker worker;
		for(int i = 0; i < cores; ++i)
		{
			worker = new SynchronizingWorker();
			workers.add(worker);
			threadPool.add(new Thread(worker));
		}
		
		//Start the workers (they should imediately fall into a spin lock)
		for(Thread t: threadPool)
			t.start();
	}
	

	/* *********************************************************************************************
	 * Trace Overrides
	 * *********************************************************************************************/
	@Override
	public void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface)
	{
		Logger.progress(-1, "Starting Tracing...");
		
		callID++;
		
		completedCount.set(0);
		
		isComplete = false;
		wasNotified = true;
		
		//Wake the workers up
		synchronized(traceLock) {
			traceLock.notifyAll();
		}
		
		//Wait until tracing is complete via Spin Lock
		synchronized(mainLock) {
			while(!isComplete) {
				try {
					
					mainLock.wait();
						
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//Re-enable the spin lock on the workers
		//TODO: This may be insufficient.  If a worker finishes much quicker than another,
		//	the finished worker has a small chance of spuriously waking up until all other
		//	workers finish
		wasNotified = false;
		

		Logger.progress(-1, "Ending Tracing...");
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void incrementCompletedCount()
	{
		int cc = completedCount.incrementAndGet();
		
		//If all workers are done, release the main thread
		if(cc == threadCount) {
			isComplete = true;
			wasNotified = false;
			synchronized(mainLock) {
				mainLock.notifyAll();
			}
		}
	}


	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class SynchronizingWorker implements Runnable {
		
		/*
		 * A ray tracing worker
		 */
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		public final int id = idPool.getAndIncrement();
		private int currentCallID = 0;

		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public SynchronizingWorker()
		{
			
		}
		

		/* *********************************************************************************************
		 * Run Methods
		 * *********************************************************************************************/
		@Override
		public void run()
		{
			for(;;)//While the spider face holds true
			{
				//Spin Lock
				synchronized(traceLock)
				{
					while(!wasNotified || callID == currentCallID)
					{
						try {
							
							traceLock.wait();
								
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//Update the call ID
				currentCallID = callID;

				
				//Do tracing here
				Logger.progress(-1, "Running RayTracerWorker ID:[" + id + "]...");
				
				
				//Increment the completed counter
				incrementCompletedCount();
			}
			
		}
		
	}
}
