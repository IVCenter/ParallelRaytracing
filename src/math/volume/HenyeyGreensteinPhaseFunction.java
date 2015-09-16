package math.volume;

import math.Vector3;
import raytrace.color.Color;
import raytrace.map.texture._3D.Texture3D;

public class HenyeyGreensteinPhaseFunction extends PhaseFunction {

	/*
	 * An implementaiton of the Henyey-Greenstein Phase Function
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D g;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public HenyeyGreensteinPhaseFunction()
	{
		//
	}
	
	public HenyeyGreensteinPhaseFunction(Texture3D g)
	{
		this.g = g;
	}
	
	
	/* *********************************************************************************************
	 * Evaluate Method
	 * *********************************************************************************************/
	public Color evaluate(Vector3 point, Vector3 exitDirection, Vector3 lightDirection)
	{
		//TODO: Convert this to a more robust per channel operation?
		double[] cArr = g.evaluate(point).getChannels();
		int length = cArr.length;
		Color result = new Color();//Set this color to be sized to the same as the map
		double[] resultArr = result.getChannels();

		double gi;
		double giSqrd; 
		double cosTheta = exitDirection.normalize().dot(lightDirection.normalize());
		
		//For all channels, evaluate
		for(int i = 0; i < length; ++i)
		{
			gi = cArr[i];
			giSqrd = gi * gi;
			
			resultArr[i] = (1 - giSqrd) / (4 * Math.PI * Math.pow( (1 + giSqrd - (2 * gi * cosTheta)), 1.5) );
		}
		
		return result;
	}

	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getG() {
		return g;
	}

	public void setG(Texture3D g) {
		this.g = g;
	}

}
