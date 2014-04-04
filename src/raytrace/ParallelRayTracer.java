package raytrace;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import math.Ray;

import process.logging.Logger;
import raster.PixelBuffer;
import raytrace.camera.Camera;
import raytrace.camera.RayBuffer;
import raytrace.framework.Tracer;
import raytrace.surfaces.CompositeSurface;

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
	
	
	//Used to cache rays
	private Camera cameraUsedForDistro = null;
	private int cameraRaySetID = -1;
	
	
	
	//TODO: Find a better way to get this data to the tracers
	private PixelBuffer pixelBuffer;
	private Camera camera;
	private CompositeSurface surface;
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelRayTracer()
	{
		int cores = Runtime.getRuntime().availableProcessors();
		threadCount = cores;
		workers = new ArrayList<SynchronizingWorker>(cores);
		threadPool = new ArrayList<Thread>(cores);
		rayBuffers = new ArrayList<RayBuffer>(cores);
		
		//Create as many workers as there are cores
		SynchronizingWorker worker;
		for(int i = 0; i < cores; ++i)
		{
			worker = new SynchronizingWorker(i);
			workers.add(worker);
			threadPool.add(new Thread(worker));
			rayBuffers.add(new RayBuffer());
		}
		
		//Start the workers (they should imediately fall into a spin lock)
		for(Thread t: threadPool)
			t.start();
	}
	

	/* *********************************************************************************************
	 * Trace Overrides
	 * *********************************************************************************************/
	@Override
	public synchronized void trace(PixelBuffer pixelBuffer, Camera camera, CompositeSurface surface)
	{	
		//Store parameters
		this.pixelBuffer = pixelBuffer;
		this.camera = camera;
		this.surface = surface;
		
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//Re-enable the spin lock on the workers
		wasNotified = false;
		
		//Tracing done
		Logger.progress(-1, "Ending Tracing... (" + (System.currentTimeMillis()-startTime) + "ms).");
		
		/*
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
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
	private void distributeRays(Camera camera)
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
				tracer.trace(pixelBuffer, buffer, surface);
				
				/*
				double acc = 0.0;
				for(Ray ray : buffer)
				{
					acc += ray.getDirection().get(0);
					//acc += 1.1;
				}*/
				
				//Logger.progress(-1, "Ending RayTracerWorker ID:[" + id + "]...");
				
				//Increment the completed counter
				incrementCompletedCount();
			}
			
		}
		
	}
}
