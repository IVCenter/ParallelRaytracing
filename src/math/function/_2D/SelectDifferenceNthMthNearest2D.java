package math.function._2D;

import java.util.Collection;

public class SelectDifferenceNthMthNearest2D implements Function2D<Collection<Double>, Double> {
	
	/*
	 * A function for calculating the euclidean distance between two points in 3D space.
	 */
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	protected int nthNearest;
	protected int mthNearest;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SelectDifferenceNthMthNearest2D()
	{
		nthNearest = 0;
		mthNearest = 1;
	}
	
	public SelectDifferenceNthMthNearest2D(int nthNearest, int mthNearest)
	{
		this.nthNearest = nthNearest;
		this.mthNearest = mthNearest;
	}
	
	
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	@Override
	public Double evaluate(Collection<Double> values)
	{
		Double last = null;
		Double nth = null;
		Double mth = null;
		int index = 0;
		for(Double value : values)
		{
			if(index == nthNearest)
				nth = value;
			if(index == mthNearest)
				mth = value;
			index++;
			last = value;
		}
		
		if(nth != null && mth != null)
			return nth - mth;
		
		return last;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int getNthNearest() {
		return nthNearest;
	}

	public void setNthNearest(int nthNearest) {
		this.nthNearest = nthNearest;
	}

	public int getMthNearest() {
		return mthNearest;
	}

	public void setMthNearest(int mthNearest) {
		this.mthNearest = mthNearest;
	}

}
