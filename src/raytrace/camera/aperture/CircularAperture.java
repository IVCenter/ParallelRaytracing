package raytrace.camera.aperture;

import math.Vector3;

public class CircularAperture extends Aperture {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * A simple circle aperture
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double distributionExponent;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CircularAperture()
	{
		radius = 0.0;
		distributionExponent = 0.5;
	}
	
	public CircularAperture(double radius, double distributionExponent)
	{
		super();
		this.radius = radius;
		this.distributionExponent = distributionExponent;
	}

	/* *********************************************************************************************
	 * Interface Overrides
	 * *********************************************************************************************/
	@Override
	public Vector3 sample()
	{
		return sample(new Vector3());
	}
	
	@Override
	public Vector3 sample(Vector3 sample)
	{
		double theta = Math.random() * Math.PI * 2.0;
		double distance = Math.pow(Math.random(), distributionExponent) * radius;
		sample.set(distance * Math.cos(theta), distance * Math.sin(theta), 0);
		return sample;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/

}
