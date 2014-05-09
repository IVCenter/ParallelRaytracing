package raytrace.camera.aperture;

import java.io.Serializable;

import math.Vector4;

public interface Aperture extends Serializable {
	
	/*
	 * An interface for camera apertures
	 */
	
	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public Vector4 sample();
	public Vector4 sample(Vector4 sample);

}
