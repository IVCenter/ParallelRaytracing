package raytrace.trace;

import raytrace.camera.Camera;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;

public class MultiPassParallelTracer extends ParallelTracer {
	
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
		protected int workIndex;
		
		
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public TracingWorker(int id)
		{
			super(id);
			workIndex = 0;
		}
		

		/* *********************************************************************************************
		 * Run Methods
		 * *********************************************************************************************/
		@Override
		protected boolean work()
		{
			//Get the ray buffer
			Camera buffer = rayBuffers.get(id);
			
			//Setup the rdata
			RenderData localRData = new RenderData();
			localRData.setCamera(buffer);
			localRData.setPixelBuffer(rdata.getPixelBuffer());
			localRData.setInputRenderBuffer(rdata.getInputRenderBuffer());
			localRData.setOutputRenderBuffer(rdata.getOutputRenderBuffer());
			localRData.setScene(rdata.getScene());
			
			//Make sure the current index isn't out of range
			//If it is, there is no work to do
			int totalWork = rdata.getScene().getTracers().size();
			if(workIndex < 0 || workIndex >= totalWork)
				return true;

			//Iterate the tracers for this scene
			Tracer tracer = rdata.getScene().getTracers().get(workIndex);
			tracer.trace(localRData);
			
			//Increment the work index
			++workIndex;
			
			return workIndex >= totalWork;
		}

		@Override
		public void reset()
		{
			workIndex = 0;
			isDone = false;
		}
		
	}
}
