package performance;

public class SimpleFLOPSMeasurement extends PerformanceMeasurement {

	/*
	 * An simple implementation of FLOPS measurement
	 */
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected final String unitType = "FLOP/s";
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SimpleFLOPSMeasurement() { /**/ }
	

	/* *********************************************************************************************
	 * Override Methods
	 * *********************************************************************************************/
	@Override
	public double measure(double sampleTimeInMS)
	{
		//If the sample time is negative or 0, set it to 10 seconds
		if(sampleTimeInMS <= 0) {
			sampleTimeInMS = 10000;
		}

		double[] a = {Math.random(), Math.random(), Math.random(), Math.random(), Math.random(), Math.random()};
		
		double operations = 0;
		double accum = 0.0;
		double startTime = System.currentTimeMillis();
		
		while(System.currentTimeMillis() - startTime < sampleTimeInMS)
		{
			operations += 4.0;//For the above conditional and this add
			
			accum = accum + a[2] - a[1] / a[3] * a[5];
			
			operations += 6.0;
			
			accum = Math.exp(a[0]) + Math.sqrt(a[4]) / Math.log(a[3] + a[1] + a[2]);
			
			operations += 9.0;
		}
		
		double endTime = System.currentTimeMillis();
		
		return operations / ((endTime - startTime) / 1000.0);
	}

}
