package raytrace.trace;

import raytrace.camera.Camera;
import raytrace.data.RenderData;
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
			
			//Setup the rdata
			RenderData localRData = new RenderData();
			localRData.setCamera(buffer);
			localRData.setPixelBuffer(rdata.getPixelBuffer());
			localRData.setRenderBuffer(rdata.getRenderBuffer());
			localRData.setScene(rdata.getScene());

			//Iterate the tracers for this scene
			for(Tracer tracer : rdata.getScene().getTracers())
			{
				tracer.trace(localRData);
			}
		}
		
	}
}
