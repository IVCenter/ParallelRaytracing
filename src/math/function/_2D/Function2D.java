package math.function._2D;

public interface Function2D<INDEPENDENT_X, DEPENDENT_Y> {
	
	/*
	 * And interface for 2D functions
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public DEPENDENT_Y eval(INDEPENDENT_X x);
}
