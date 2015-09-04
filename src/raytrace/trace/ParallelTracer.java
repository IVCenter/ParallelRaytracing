package raytrace.trace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import process.logging.Logger;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;
import system.Configuration;

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
	//protected PixelBuffer activePixelBuffer;
	//@SuppressWarnings("unused")
	//protected Camera activeCamera;
	//protected Scene activeScene;
	protected RenderData rdata;
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelTracer()
	{
		//Set the number of core to the minimum of the avialble cores, or the configuration maximum
		int cores = Math.min(Configuration.getMaxAllowableRenderingCores(), Runtime.getRuntime().availableProcessors());
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
		}
		
		//Start the workers (they should immediately fall into a spin lock)
		for(Thread t: threadPool)
			t.start();
	}
	

	/* *********************************************************************************************
	 * Trace Overrides
	 * *********************************************************************************************/
	@Override
	public synchronized void trace(RenderData data)
	{	
		//Store parameters
		this.rdata = data;
		
		//Start tracing
		Logger.message(-1, "Starting Tracing...(" + threadCount + " threads).");
		long startTime = System.currentTimeMillis();
		
		//Distribute camera rays (if necessary)
		distributeRays(rdata.getScene().getActiveCamera());
		
		//Reset the workers
		for(SynchronizingWorker worker : workers)
			worker.reset();
		
		//For all work across all workers
		while(thereIsWorkToBeDone())
		{
			//Update the call id
			callID++;
			
			//Flip the input and output buffers
			//TODO: Don't just flip them, copy the contents across!
			RenderBuffer oldInput = rdata.getInputRenderBuffer();
			rdata.setInputRenderBuffer(rdata.getOutputRenderBuffer());
			rdata.setOutputRenderBuffer(oldInput);
			
			//Copy the input contents into the output
			rdata.getOutputRenderBuffer().copy(rdata.getInputRenderBuffer());
			
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
		}
		
		
		//Tracing done
		Logger.message(-1, "Ending Tracing... (" + (System.currentTimeMillis()-startTime) + "ms).");
		
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
	
	protected boolean thereIsWorkToBeDone()
	{
		for(SynchronizingWorker worker : workers)
			if(!worker.isDone())
				return true;
		return false;
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
		protected boolean isDone = false;
		
		
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
					isDone = work();
					
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
		protected abstract boolean work();

		public abstract void reset();


		/* *********************************************************************************************
		 * Getters/Setters
		 * *********************************************************************************************/
		public boolean isDone() {
			return isDone;
		}
		
	}
	
}