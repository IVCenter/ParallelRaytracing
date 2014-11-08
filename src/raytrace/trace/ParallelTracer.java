package raytrace.trace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import process.logging.Logger;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.framework.Tracer;
import raytrace.scene.Scene;

public abstract class ParallelTracer implements Tracer {
	
	/*
	 * A parallel tracer that spawns workers based on available resources
	 */
	/* *********************************************************************************************
	 * Local Constants
	 * *********************************************************************************************/
	//protected static final String k = "";
	//private static AtomicInteger idPool = new AtomicInteger(0);
	
	
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
	protected ArrayList<Camera> rayBuffers;
	
	//protected boolean randomizeRays = true;//TODO: Need a getter/setter for this
	
	//Used to cache rays
	//private Camera cameraUsedForDistro = null;
	
	
	
	//TODO: Find a better way to get this data to the tracers
	//NOTE: These change every call to trace
	protected PixelBuffer activePixelBuffer;
	//@SuppressWarnings("unused")
	protected Camera activeCamera;
	protected Scene activeScene;
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelTracer()
	{
		int cores = Runtime.getRuntime().availableProcessors();
		//cores = 1;
		threadCount = cores;
		workers = new ArrayList<SynchronizingWorker>(cores);
		threadPool = new ArrayList<Thread>(cores);
		rayBuffers = new ArrayList<Camera>(cores);
		
		//Create as many workers as there are cores
		//TODO:If a worker crashes, create a new one
		SynchronizingWorker worker;
		for(int i = 0; i < cores; ++i)
		{
			worker = createWorker(i);
			workers.add(worker);
			threadPool.add(new Thread(worker));
			//rayBuffers.add(new RayBuffer());
		}
		
		//Start the workers (they should immediately fall into a spin lock)
		for(Thread t: threadPool)
			t.start();
	}
	

	/* *********************************************************************************************
	 * Trace Overrides
	 * *********************************************************************************************/
	@Override
	public synchronized void trace(PixelBuffer pixelBuffer, Camera camera, Scene scene)
	{	
		//Store parameters
		this.activePixelBuffer = pixelBuffer;
		this.activeCamera = camera;
		this.activeScene = scene;
		
		//Start tracing
		Logger.progress(-1, "Starting Tracing...(" + threadCount + " threads).");
		long startTime = System.currentTimeMillis();
		
		//Distribute camera rays (if necessary)
		distributeRays(camera);
		
		//Update the call id
		callID++;
		
		//Clear thread control flags/counts
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
					e.printStackTrace();
				}
			}
		}
		
		//Re-enable the spin lock on the workers
		wasNotified = false;
		
		//Tracing done
		Logger.progress(-1, "Ending Tracing... (" + (System.currentTimeMillis()-startTime) + "ms).");
		
	}
	
	protected void incrementCompletedCount()
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
	
	//@SuppressWarnings("unchecked")
	protected void distributeRays(Camera camera)
	{
		rayBuffers.clear();
		
		Collection<Camera> cams = camera.decompose(threadCount);
		
		for(Camera cam : cams)
			rayBuffers.add(cam);
	}
	
	protected abstract SynchronizingWorker createWorker(int i);
	

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	protected abstract class SynchronizingWorker implements Runnable {
		
		/*
		 * A synchronizing worker
		 */
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		public final int id;
		private int currentCallID = 0;
		
		
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public SynchronizingWorker(int id)
		{
			this.id = id;
		}

		
		/* *********************************************************************************************
		 * Run Methods
		 * *********************************************************************************************/
		@Override
		public void run()
		{
			for(;;)//While the spider face holds true
			{
				try
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
	
					//Perform a unit operation
					work();
					
					//Increment the completed counter
					incrementCompletedCount();
				}
				catch(Throwable t)
				{
					//Print a stack trace
					t.printStackTrace();
					
					//Boot a new worker to replace the crashed one
					//SynchronizingWorker worker = createWorker(id);
					//workers.set(id, worker);
					//threadPool.set(id, new Thread(worker));
					
					//Increment the completed counter
					incrementCompletedCount();
				}
			}
			
		}

		
		/* *********************************************************************************************
		 * Abstract Methods
		 * *********************************************************************************************/
		protected abstract void work();
		
	}
	
}