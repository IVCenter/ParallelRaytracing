package raytrace.camera.aperture;

import math.Vector4;

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
	public Vector4 sample()
	{
		return sample(new Vector4());
	}
	
	@Override
	public Vector4 sample(Vector4 sample)
	{
		sample.set(0, 0, 0, 0);
		return sample;
	}

}
