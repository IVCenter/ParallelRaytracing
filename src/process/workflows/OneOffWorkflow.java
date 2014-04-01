package process.workflows;

import process.Job;
import process.Environment;

public class OneOffWorkflow extends Workflow {
	
	
	/*
	 * Allows for the processing of any single Job
	 */

	/* *********************************************************************************************
	 * Vars
	 * *********************************************************************************************/
	protected Job job;
	
	protected Environment returnMap;

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public OneOffWorkflow()
	{
		//
	}


	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public boolean setJob(Job job)
	{
		//No null jobs
		if(job == null) {
			warning("Null jobs can not be added to a workflow.");
			return false;
		}
		
		this.job = job;

		return true;
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
		//Run the job
		try {
			
			returnMap = job.execute(Environment.map(parameters));
			
		} catch (Exception e) {
			error(e.getMessage());
			error("An unrecoverable error ocurred while executing the child job of a One-Off Workflow.");
		}
	}
}
