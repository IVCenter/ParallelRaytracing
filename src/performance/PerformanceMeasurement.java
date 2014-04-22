package performance;

public abstract class PerformanceMeasurement {
	
	/*
	 * An interface for performance measurement
	 */
	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	protected String unitType;
	

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/	
	public abstract double measure(double sampleTimeInMS);
	
	public String getUnitType()
	{
		return unitType;
	}
}
