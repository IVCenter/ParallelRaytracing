package raytrace.trace;

import raytrace.camera.Camera;
import raytrace.framework.Tracer;

public class ParallelRayTracer extends ParallelTracer {
	
	/*
	 * A parallel ray tracer that make use of the parallel tracer interface to manage threading and sync
	 */

	/* *********************************************************************************************
	 * Abstract Overrides
	 * *********************************************************************************************/
	@Override
	protected SynchronizingWorker createWorker(int i)
	{
		return new TracingWorker(i);
	}
	

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class TracingWorker extends SynchronizingWorker {
		
		/*
		 * A ray tracing worker
		 */
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public TracingWorker(int id)
		{
			super(id);
		}
		

		/* *********************************************************************************************
		 * Run Methods
		 * *********************************************************************************************/
		@Override
		protected void work()
		{
			//Get the ray buffer
			Camera buffer = rayBuffers.get(id);

			//Iterate the tracers for this scene
			for(Tracer tracer : activeScene.getTracers())
			{
				tracer.trace(activePixelBuffer, buffer, activeScene);
			}
		}
		
	}
}
