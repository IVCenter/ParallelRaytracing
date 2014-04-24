package math.function._2D;

public class PassThrough2D<INDEPENDENT_X> implements Function2D<INDEPENDENT_X, INDEPENDENT_X> {
	
	/*
	 * A 2D function that pushes the input to the output
	 */

	@Override
	public INDEPENDENT_X eval(INDEPENDENT_X x)
	{
		return x;
	}

}
