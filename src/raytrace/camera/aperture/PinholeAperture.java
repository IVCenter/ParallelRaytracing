package raytrace.camera.aperture;

import math.Vector3;

public class PinholeAperture extends Aperture {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * A simple pinhole aperture
	 */
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PinholeAperture()
	{	
		/**/
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
		sample.set(0, 0, 0);
		return sample;
	}

}
