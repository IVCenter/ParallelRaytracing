package process.workflows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import process.Environment;
import process.Job;

public class SequentialWorkflow extends Workflow {

	/*
	 * Allows for the processing of any number of jobs that must execute
	 * in a certain order.
	 */
	
	/* *********************************************************************************************
	 * Vars
	 * *********************************************************************************************/
	protected long totalJobs = 0;
	protected long completeJobs = 0;
	
	protected SortedSet<Long> sortedStageIDs = new TreeSet<Long>();
	protected HashMap<Long, HashSet<Job>> stages = new HashMap<Long, HashSet<Job>>();
	
	protected Environment returnEnv = new Environment();
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public SequentialWorkflow()
	{
		//
	}
	
	
	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public boolean addJob(long stage, Job job)
	{
		//No null jobs
		if(job == null) {
			warning("Null jobs can not be added to a workflow.");
			return false;
		}
		
		HashSet<Job> stageJobs = stages.get(stage);
		
		//If the stage is new, create it
		if(stageJobs == null) {
			stageJobs = new HashSet<Job>();
			sortedStageIDs.add(stage);
			stages.put(stage, stageJobs);
		}
		
		//If we already have this job on this stage then reject it
		if(stageJobs.contains(job)) {
			warning("Job #" + job.getID() + " already exists on stage #" + stage);
			return false;
		}
		
		
		
		return stageJobs.add(job);
	}
	
	public boolean addJobToEnd(Job job)
	{
		long lastStageID = sortedStageIDs.isEmpty() ? 0 : sortedStageIDs.last();
		
		return addJob(lastStageID+1, job);
	}

	
	public boolean setStage(long stageID, HashSet<Job> jobs)
	{
		if(jobs == null) {
			warning("Null Stages can not be added to a workflow.");
			return false;
		}
		
		HashSet<Job> oldStage = stages.put(stageID, jobs);
		
		if(oldStage != null) {
			warning("Replacing Stage #" + stageID);
		}
		
		if(!sortedStageIDs.contains(stageID))
			sortedStageIDs.add(stageID);
		
		return true;
	}

	public boolean addStageToEnd(HashSet<Job> jobs)
	{
		long lastStageID = sortedStageIDs.isEmpty() ? 0 : sortedStageIDs.last();
		
		return setStage(lastStageID+1, jobs);
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
		if(returnEnv != null)
			inject(returnEnv);
	}
	
	@Override
	protected void begin()
	{
		long totalPermutedJobs = getTotalPermutedJobs();
		long permutedJobsCompleted = 0;
		
		ArrayList<Environment> prevStageMaps = new ArrayList<Environment>();
		prevStageMaps.add(Environment.map(parameters));
		
		ArrayList<Environment> thisStageMaps = new ArrayList<Environment>();
		
		HashSet<Job> stage;
		for(long stageID : sortedStageIDs)
		{
			thisStageMaps.clear();
			
			stage = stages.get(stageID);
			
			//If the state is null, send a warning and continue
			if(stage == null) {
				warning("Registered Stage #" + stageID + " is null!");
				continue;
			}
			
			//If the stage is empty, jump to the next stage
			if(stage.isEmpty()) {
				warning("Stage #" + stageID + " is empty.");
				continue;
			}
			
			Environment tempPM;
			for(Environment pmap : prevStageMaps)
			{
				tempPM = Environment.map(pmap);
				for(Job job : stage)
				{
					thisStageMaps.add(tempPM.addMap(job.execute(tempPM)));
					
					++permutedJobsCompleted;
					this.percentComplete = (permutedJobsCompleted / (double) totalPermutedJobs);
				}
			}
			
			prevStageMaps.clear();
			prevStageMaps.addAll(thisStageMaps);
			
			completeJobs += stage.size();
		}
		
		//Push all of the "This stage" maps onto the return map
		for(Environment pmap : thisStageMaps)
		{
			returnEnv = returnEnv.addMap(pmap);
		}
	}
	
	private long getTotalPermutedJobs()
	{
		long count = 1;
		
		HashSet<Job> stage;
		for(long stageID : sortedStageIDs)
		{
			stage = stages.get(stageID);
			
			if(stage == null || stage.size() < 1)
				continue;
			
			count *= stage.size();
		}
		
		return count;
	}

}
