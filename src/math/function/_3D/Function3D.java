package math.function._3D;

public interface Function3D<INDEPENDENT_X, INDEPENDENT_Y, DEPENDENT_Z> {
	
	/*
	 * And interface for 3D functions (like texture maps, height fields, normal maps, etc.)
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public DEPENDENT_Z eval(INDEPENDENT_X x, INDEPENDENT_Y y);
}
