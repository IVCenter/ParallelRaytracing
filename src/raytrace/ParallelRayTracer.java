package raytrace;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import math.Ray;

import process.logging.Logger;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.camera.RayBuffer;
import raytrace.framework.Tracer;
import raytrace.scene.Scene;

public class ParallelRayTracer implements Tracer {
	
	/*
	 * A parallel ray tracers that spawns workers based on available resources
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
	protected ArrayList<RayBuffer> rayBuffers;
	
	protected boolean randomizeRays = true;//TODO: Need a getter/setter for this
	
	//Used to cache rays
	private Camera cameraUsedForDistro = null;
	private int cameraRaySetID = -1;
	
	
	
	//TODO: Find a better way to get this data to the tracers
	//NOTE: These change every call to trace
	private PixelBuffer activePixelBuffer;
	@SuppressWarnings("unused")
	private Camera activeCamera;
	private Scene activeScene;
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelRayTracer()
	{
		int cores = Runtime.getRuntime().availableProcessors();
		//cores = 1;
		threadCount = cores;
		workers = new ArrayList<SynchronizingWorker>(cores);
		threadPool = new ArrayList<Thread>(cores);
		rayBuffers = new ArrayList<RayBuffer>(cores);
		
		//Create as many workers as there are cores
		//TODO:If a worker crashes, create a new one
		SynchronizingWorker worker;
		for(int i = 0; i < cores; ++i)
		{
			worker = new SynchronizingWorker(i);
			workers.add(worker);
			threadPool.add(new Thread(worker));
			rayBuffers.add(new RayBuffer());
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
		distributeRays(camera, randomizeRays);
		
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
	
	@SuppressWarnings("unchecked")
	private void distributeRays(Camera camera, boolean randomize)
	{
		if(camera == cameraUsedForDistro && camera.getRaySetID() == cameraRaySetID)
			return;
		
		cameraUsedForDistro = camera;
		cameraRaySetID = camera.getRaySetID();
		
		int bufCount = rayBuffers.size();
		ArrayList<Ray>[] buffers = (ArrayList<Ray>[])new ArrayList[bufCount];
		
		//Get the buffers and clear them
		int i = 0;
		ArrayList<Ray> temp;
		for(RayBuffer buffer : rayBuffers) {
			temp = buffer.getRays();
			temp.clear();
			buffers[i++] = temp;
		}
		
		//Distribute the rays to each buffer
		i = 0;
		for(Ray ray : camera)
		{
			buffers[(i++)%bufCount].add(ray);
		}
		
		//If randomize is set to true, shuffle the rays around
		if(randomize)
		{
			//Randomize the rays
			Ray tempRay;
			int leftIndex, rightIndex;
			int leftRayIndex, rightRayIndex;
			for(i *= 4; i >= 0; --i)
			{
				//Calculate buffer indices
				leftIndex = (int)Math.floor((Math.random() * bufCount));
				rightIndex = leftIndex;
				if(bufCount != 1)
					while(rightIndex == leftIndex) {
						rightIndex = (int)Math.floor((Math.random() * bufCount));
					}
				
				//Swap random rays from the two buffers
				leftRayIndex = (int)Math.floor((Math.random() * buffers[leftIndex].size()));
				rightRayIndex = (int)Math.floor((Math.random() * buffers[rightIndex].size()));
				
				tempRay = buffers[leftIndex].get(leftRayIndex);
				buffers[leftIndex].set(leftRayIndex, buffers[rightIndex].get(rightRayIndex));
				buffers[rightIndex].set(rightRayIndex, tempRay);
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
		public final int id;
		private int currentCallID = 0;
		private RayTracer tracer;

		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public SynchronizingWorker(int id)
		{
			this.id = id;
			tracer = new RayTracer();
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
				//Logger.progress(-1, "Starting RayTracerWorker ID:[" + id + "]...");
				
				RayBuffer buffer = rayBuffers.get(id);
				tracer.trace(activePixelBuffer, buffer, activeScene);
				
				
				//Logger.progress(-1, "Ending RayTracerWorker ID:[" + id + "]...");
				
				//Increment the completed counter
				incrementCompletedCount();
			}
			
		}
		
	}
}
