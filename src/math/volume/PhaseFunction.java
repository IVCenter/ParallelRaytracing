package math.volume;

import raytrace.color.Color;
import math.Vector3;
import math.function._4D.Function4D;

public abstract class PhaseFunction implements Function4D<Vector3, Vector3, Vector3, Color> {
	
	/*
	 * A base class for medium phase function
	 */
	
	/* *********************************************************************************************
	 * Abstract Medium Methods
	 * *********************************************************************************************/
	/**
	 * Assumes that exitDirection points away from point, and lightDirection points towards point
	 */
	public abstract Color evaluate(Vector3 point, Vector3 exitDirection, Vector3 lightDirection);

}
