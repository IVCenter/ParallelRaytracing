package performance;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import process.logging.Logger;

public class PerformanceMonitor {

	/*
	 * A simple performance monitor
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/

	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PerformanceMonitor()
	{
		//TODO: Launch thread to monitor and update performance data
		ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
		

		//If the current jvm supports cpu time
		if(tmxb.isThreadCpuTimeSupported())
		{
			//TODO: Wrap the below and do soemthing
		}
		
		long cpuTime;
		ThreadInfo threadInfo;
		long[] threadIDs = tmxb.getAllThreadIds();
		for(long threadID : threadIDs)
		{
			threadInfo = tmxb.getThreadInfo(threadID);
			
			if(threadInfo == null)
				continue;
			
			cpuTime = tmxb.getThreadCpuTime(threadID);
		}
		
		
		//Memory calc
		double memoryInUse = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		
		
		
		
		
		//Performance test
		final SimpleFLOPSMeasurement flopper = new SimpleFLOPSMeasurement();
		double acc = 0.0;
		for(int i = 0; i < Runtime.getRuntime().availableProcessors(); ++i) {
			new Thread() {
				public void run() {
					Logger.progress(-17, "FLOPs: [" + flopper.measure(5000) + "].");
				}
			}.start();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
