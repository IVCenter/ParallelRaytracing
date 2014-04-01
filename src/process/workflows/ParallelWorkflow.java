package process.workflows;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import process.Job;
import process.Environment;

public class ParallelWorkflow extends Workflow {

	/*
	 * Allows for the processing of any number of jobs that can
	 * execute in any order.
	 */

	/* *********************************************************************************************
	 * Vars
	 * *********************************************************************************************/
	protected int maxThreads = Runtime.getRuntime().availableProcessors();
	
	protected long totalJobs = 0;
	protected long completeJobs = 0;

	protected HashSet<Job> jobs = new HashSet<Job>();
	
	protected Environment returnMap;

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public ParallelWorkflow()
	{
		//
	}

	/* *********************************************************************************************
	 * Accessor Methods
	 * *********************************************************************************************/
	public void setMaxThreads(int maxThreads)
	{
		//Reduce the incoming thread count to something that might actually run...
		this.maxThreads = Math.min(maxThreads, Job.maxThreadCount - 1);
	}

	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public boolean addJob(Job job)
	{
		//No null jobs
		if(job == null) {
			warning("Null jobs can not be added to a workflow.");
			return false;
		}

		return jobs.add(job);
	}


	/* *********************************************************************************************
	 * Execution Methods
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		//Extract parameters here
	}

	@Override
	protected void finalize()
	{
		//Inject parameters here
		if(returnMap != null)
		{
			inject(returnMap);
		}
	}

	@Override
	protected void begin()
	{
		HashSet<CallableJob> wrappedJobs = new HashSet<CallableJob>();
		
		//Get the count of the jobs we'll be running
		int jobCount = jobs.size();

		//If we have fewer jobs than threads, we'll use just enough threads to run the jobs
		int threadsToUse = Math.min(jobCount, maxThreads);
		
		//Request the threads we need
		Job.requestThreads(threadsToUse);
		
		//Prepare the Executor Service
		ExecutorService exec = Executors.newFixedThreadPool(threadsToUse);
		
		//For all Jobs, wrap them and add them to the executor service
		for(Job job : jobs)
		{
			//Create a new wrapped job with a copy of the current parameter map
			CallableJob r = new CallableJob(Environment.map(parameters), job);
			wrappedJobs.add(r);
			exec.submit(r);
		}

		//Start the shutdown sequence
		exec.shutdown();
		
		//And wait until all submitted tasks are completed
		try {
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			
			returnMap = new Environment();
			
			//Get the output param maps
			for(CallableJob cj : wrappedJobs)
			{
				returnMap = returnMap.addMap(cj.output);
			}
			
			
		} catch (InterruptedException e) {
			error(e.getMessage());
			error("Received an Interrupt Exception while awaiting Parallel Workflow termination.");
		}

		//Release the threads now that we are finished
		Job.releaseThreads(threadsToUse);
	}
	
	
	private static class CallableJob implements Runnable
	{
		public Job job;
		public Environment input;
		public Environment output;
		
		public CallableJob(Environment input, Job job)
		{
			this.input = input;
			this.job = job;
		}
		
		@Override
		public void run()
		{
			output = job.execute(input);
		}
		
	}


}

