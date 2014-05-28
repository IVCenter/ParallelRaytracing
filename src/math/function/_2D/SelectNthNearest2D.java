package math.function._2D;

import java.util.Collection;

public class SelectNthNearest2D implements Function2D<Collection<Double>, Double> {
	
	/*
	 * A function for calculating the euclidean distance between two points in 3D space.
	 */
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	protected int nthNearest;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SelectNthNearest2D()
	{
		nthNearest = 0;
	}
	
	public SelectNthNearest2D(int nthNearest)
	{
		this.nthNearest = nthNearest;
	}
	
	
	/* *********************************************************************************************
	 * Function Override
	 * *********************************************************************************************/
	@Override
	public Double evaluate(Collection<Double> values)
	{
		Double last = null;
		int index = 0;
		for(Double value : values)
		{
			if(index == nthNearest)
				return value;
			index++;
			last = value;
		}
		
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

}
