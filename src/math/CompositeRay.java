package math;

import java.util.ArrayList;
import java.util.Iterator;

public class CompositeRay extends Ray {

	/*
	 * A class that represents a set of Rays in 3D space
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Ray> rays;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	public CompositeRay()
	{
		rays = new ArrayList<Ray>();
	}
	
	public CompositeRay(int rayCount)
	{
		rays = new ArrayList<Ray>(rayCount);
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public void addRay(Ray ray)
	{
		if(ray != null)
			rays.add(ray);
	}
	
	
	/* *********************************************************************************************
	 * Iteration Overrides
	 * *********************************************************************************************/
	@Override
	public Iterator<Ray> iterator()
	{
		return rays.iterator();
	}

}
