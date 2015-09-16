package math.volume;

import math.Vector3;
import raytrace.color.Color;

public class IsotropicPhaseFunction extends PhaseFunction {

	/*
	 * An implementaiton of the Isotropic Phase Function
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IsotropicPhaseFunction()
	{
		//
	}
	
	
	/* *********************************************************************************************
	 * Evaluate Method
	 * *********************************************************************************************/
	public Color evaluate(Vector3 point, Vector3 exitDirection, Vector3 lightDirection)
	{
		Color result = Color.white();
		
		return result.multiply3M(1.0 / (4.0 * Math.PI));
	}

}
