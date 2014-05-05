package raytrace.camera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import math.Ray;

public class RayBuffer extends Camera {
	
	/*
	 * A simple set of rays (used by parallel ray tracer to distribute a camera to workers)
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	private ArrayList<Ray> rays;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RayBuffer()
	{
		rays = new ArrayList<Ray>();
	}
	
	public ArrayList<Ray> getRays()
	{
		return rays;
	}

	/* *********************************************************************************************
	 * Override Methods
	 * *********************************************************************************************/

	@Override
	public Iterator<Ray> iterator()
	{
		return rays.iterator();
	}

	@Override
	protected void update()
	{
		//TODO: Oh?
	}
	
	@Override
	protected void wasModified()
	{
		//TODO: Oh?
	}

	@Override
	protected Ray getRay(double x, double y)
	{
		//Since this is a pre-defined buffer we have no idea how to generate a given ray
		//that would make any sense
		return null;
	}

	@Override
	public Collection<Camera> decompose(int count)
	{
		// TODO Mmmmm
		return null;
	}

}
