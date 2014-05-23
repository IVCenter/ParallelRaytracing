package math.function._4D;

public interface Function4D<INDEPENDENT_X, INDEPENDENT_Y, INDEPENDENT_Z, DEPENDENT_W> {
	
	/*
	 * An interface for 4D functions (like volume textures, height volumes, normal volumes, etc.)
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public DEPENDENT_W evaluate(INDEPENDENT_X x, INDEPENDENT_Y y, INDEPENDENT_Z z);
}