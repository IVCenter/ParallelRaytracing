package raytrace.camera.aperture;

import math.Vector4;

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
	public Vector4 sample()
	{
		return sample(new Vector4());
	}
	
	@Override
	public Vector4 sample(Vector4 sample)
	{
		double theta = Math.random() * Math.PI * 2.0;
		double distance = Math.pow(Math.random(), distributionExponent) * radius;
		sample.set(distance * Math.cos(theta), distance * Math.sin(theta), 0, 0);
		return sample;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/

}
