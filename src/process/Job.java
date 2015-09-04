package process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import process.logging.Logger;
import system.Constants;

public abstract class Job implements Runnable {
	
	/*
	 * A base class for Jobs
	 */
	

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static long nextJobID = 0;
	protected static String idPrefix = "J";
	
	protected static int activeThreadCount = 0;
	protected static int maxThreadCount = 128;
	protected static int millisecondsToSleepBetweenRequests = 50;
	protected static double permissivePercentage = 0.001;
	protected static HashMap<Long, ThreadCounter> threadRequestCounts;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		threadRequestCounts = new HashMap<Long, ThreadCounter>();
		ThreadCounter i = new ThreadCounter(1);
		threadRequestCounts.put(Thread.currentThread().getId(), i);
	}
	

	/* *********************************************************************************************
	 * Static Helper Methods
	 * *********************************************************************************************/
	private static synchronized long nextJobID() { return nextJobID++; }
	
	
	/* *********************************************************************************************
	 * Vars
	 * *********************************************************************************************/
	private long jobID = nextJobID();
	private long creationTime = (new Date()).getTime();
	private long startTime = -1;
	private long endTime = -1;
	protected double percentComplete = 0.0;
	
	private long warningCount = 0;
	private long errorCount = 0;
	
	protected Environment parameters;
	protected Environment resultMap;
	
	protected boolean DEBUG = false;
	
	protected Transform redirect = new Transform();
	protected String instanceKey = Integer.toString(this.hashCode());

	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/


	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	public Environment execute()
	{
		return execute(new Environment());
	}
	
	public Environment execute(Environment params) {
		//Setup the instance variables
		parameters = params; 
		
		this.percentComplete = 0.0;

		//Have subclasses prepare the instance variables for running
		try{
			message("Initializing Job #" + jobID + ".");
			initialize();
		}catch(Exception e) {
			error("Initialization of Job #" + jobID + " failed in an unrecoverable manner.");
			error(e.getMessage());
			error(e.getLocalizedMessage());
			error(stackTraceToString(e));
		}

		//Run the job
		run();

		//Setup any return values
		resultMap = new Environment();
		try{
			message("Finalizing Job #" + jobID + ".");
			finalize();
		}catch(Exception e) {
			error("Finalization of Job #" + jobID + " failed in an unrecoverable manner.  Following jobs may not execute correctly.");
			error(e.getMessage());
			error(e.getLocalizedMessage());
			error(stackTraceToString(e));
		}
		
		this.percentComplete = 1.0;

		return resultMap;
	}
	
	@Override
	public void run()
	{
		//If we have any errors even before starting, cancel the job.
		if(errorCount > 0) {
			error("Cancelling Job #" + jobID + ".  Encountered a total of " + errorCount + " errors, and " + warningCount + " warnings.");
			return;
		}
		
		//Grab the start time
		this.startTime = (new Date()).getTime();
		
		//Start the job
		try{
			this.begin();
		}catch(Exception e) {
			error("An exception occurred while running Job #" + jobID + ".");
			error(e.getMessage());
			error(e.getLocalizedMessage());
			error(stackTraceToString(e));
		}
		
		//If we've returned from job then we can now store the end time
		this.endTime = (new Date()).getTime();
		
		//If we have any errors after ending the job, alert the user (?).
		if(errorCount > 0) {
			//TODO: What happens when a job has failed to complete?
			error("Failed to successfully complete Job #" + jobID + ".  Encountered a total of " + errorCount + " errors, and " + warningCount + " warnings.");
			return;
		}
	}
	
	public long getID() { return jobID; }

	public long getCreationTime() { return creationTime; }

	public long getStartTime() { return startTime; }

	public long getEndTime() { return endTime; }
	
	public long getElapsedTime()
	{
		//If no start time, job has not yet been started
		if(startTime == -1)
			return 0;

		//If no end time, return time since start
		if(endTime == -1 || endTime < startTime)
			return (new Date()).getTime() - startTime;
		
		return endTime - startTime;
	}
	
	public double getPercentComplete() { return percentComplete; }
	
	public long getWarningCount() { return warningCount; }
	
	public long getErrorCount() { return errorCount; }
	
	public Environment getParameters()
	{
		return parameters;
	}
	

	public void addRedirect(String source, String destination) { redirect.add(source, destination); }
	public String removeRedirect(String source) { return redirect.remove(source); }
	public String getRedirect(String source) { return redirect.transform(source); }
	
	private static String stackTraceToString(Exception e)
	{
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] elements = e.getStackTrace();
		for(StackTraceElement ele : elements)
		{
			sb.append(ele.toString() + "\n");
		}
		return sb.toString();
	}
	

	/* *********************************************************************************************
	 * Logging Methods
	 * *********************************************************************************************/
	protected void debug(String message) {
		if(Constants.GLOBAL_DEBUG && DEBUG)
			Logger.debug(jobID, idPrefix, message); 
	}

	protected void message(String message) { Logger.message(jobID, idPrefix, message); }

	protected void warning(String message) {
		++warningCount;
		Logger.warning(jobID, idPrefix, message);
	}

	protected void error(String message) {
		++errorCount;
		Logger.error(jobID, idPrefix, message);
	}
	

	/* *********************************************************************************************
	 * Protected Utility Methods
	 * *********************************************************************************************/
	/**
	 * Convert Object to given type via generics
	 */
	@SuppressWarnings("unchecked")
	protected <T> T convert(Object o)
	{
		//If the incoming object is null, then return null
		if(o == null) {
			warning("Object o is null, unable to convert");
			return null;
		}
		
		//Create a fake instance of T
		T t = ((T[])(new Object[1]))[0];

		//Attempt to cast o into a t of type T
		try{
			t = (T)o;
		}catch(Exception cce) {
			error("Unable to cast Object o into type " + t.getClass().getName());
			error(stackTraceToString(cce));
			error(cce.getMessage());
			return null;
		}
		
		return t;
	}
	
	protected <T> T extract(Environment params, String key)
	{
		String trueKey = getRedirect(key);
		Object o = params.get(trueKey);
		
		if(o == null) {
			warning("Job.extract(): Unable to find value for key [" + key + "]->[" + trueKey + "].");
		}
		
		return convert(o);
		
	}
	
	protected <T> T extract(String key)
	{
		return extract(parameters, key);
		
	}
	
	/**
	 * Injects an object o into the Result Map at the given key's Redirect Target.
	 * Note: only guaranteed to work if called within finalize()
	 */
	protected <T> void inject(Environment params, String key, T t)
	{
		String trueKey = getRedirect(key);
		params.put(trueKey, t);
	}
	
	protected <T> void inject(String key, T t)
	{
		inject(resultMap, key, t);
	}
	
	/**
	 * Performs inject on all items stored in an Environment
	 * @param params
	 */
	protected void inject(Environment destinationMap, Environment toInjectMap)
	{
		for(Map.Entry<String, Object> entry : toInjectMap.entrySet())
		{
			inject(destinationMap, entry.getKey(), entry.getValue());
		}
	}
	
	protected void inject(Environment params)
	{
		inject(resultMap, params);
	}
	

	/* *********************************************************************************************
	 * Protected Utility Methods
	 * *********************************************************************************************/
	protected static synchronized boolean requestThreads(int threadCount)
	{
		//While threads are not available, sleep
		while(accessThreadCount(0) + threadCount > maxThreadCount)
		{
			//To prevent deadlock, roll a random number,
			double rand = Math.random();
			
			//and let some requests slip by
			if(rand <= (permissivePercentage / threadCount) )
				break;
			
			//else we'll sleep for a short period and wait for threads to become available
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				Logger.error(-99, "THREADING", "Received an Interrupt Exception while waiting for a thread request to complete.");
				Logger.error(-99, "THREADING", stackTraceToString(e));
				return false;
			}
		}
		
		//Get the current threads ID
		long threadID = Thread.currentThread().getId();
		
		//Get the counter associated with this thread (or make one if needed)
		ThreadCounter counter = threadRequestCounts.get(threadID);
		if(counter == null)
		{
			counter = new ThreadCounter();
			threadRequestCounts.put(threadID, counter);
		}
		
		//Update the counter (and ass a result the active thread count)
		counter.add(threadCount);
		
		return true;
	}
	
	protected static synchronized boolean releaseThreads(int threadCount)
	{
		//Get the current threads ID
		long threadID = Thread.currentThread().getId();

		//Get the counter associated with this thread
		ThreadCounter counter = threadRequestCounts.get(threadID);
		if(counter == null)
		{
			Logger.warning(-99, "THREADING", "Encountered a thread attempting to free threads before requesting them!");
			
			return false;
		}
		
		//Free the given amount of threads, up to the total threads allocated to this ID
		counter.sub(threadCount);
		
		return true;
	}
	
	
	private static synchronized int accessThreadCount(int modifier)
	{
		activeThreadCount = activeThreadCount + modifier;
		return activeThreadCount;
	}
	
	public static int getActiveThreadCount()
	{
		return accessThreadCount(0);
	}
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	protected abstract void initialize();
	protected abstract void begin();
	protected abstract void finalize();

	
	private static class ThreadCounter
	{
		private int value;
		
		public ThreadCounter(int v)
		{
			add(v);
		}
		
		public ThreadCounter()
		{
			this.value = 0;
		}
		
		public void add(int v) {
			v = Math.max(0, v);
			value += v;
			accessThreadCount(v);
		}
		
		public void sub(int v) {
			v = Math.max(0, v);
			v = Math.min(value, v);
			value -= v;
			accessThreadCount(-v);
		}
		
		@SuppressWarnings("unused")
		public int value() {
			return value;
		}
		
	}
	
}
