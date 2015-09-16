package math.volume;

import math.Vector3;
import raytrace.color.Color;
import raytrace.map.texture._3D.Texture3D;

public class SchlickPhaseFunction extends PhaseFunction {

	/*
	 * An implementaiton of the Schlick Phase Function
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D k;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SchlickPhaseFunction()
	{
		//
	}
	
	public SchlickPhaseFunction(Texture3D k)
	{
		this.k = k;
	}
	
	
	/* *********************************************************************************************
	 * Evaluate Method
	 * *********************************************************************************************/
	public Color evaluate(Vector3 point, Vector3 exitDirection, Vector3 lightDirection)
	{
		//TODO: Convert this to a more robust per channel operation?
		double[] cArr = k.evaluate(point).getChannels();
		int length = cArr.length;
		Color result = new Color();//Set this color to be sized to the same as the map
		double[] resultArr = result.getChannels();

		double ki;
		double kiSqrd; 
		double onePlusKCos;
		double cosTheta = exitDirection.normalize().dot(lightDirection.normalize());
		
		//For all channels, evaluate
		for(int i = 0; i < length; ++i)
		{
			ki = cArr[i];
			kiSqrd = ki * ki;
			onePlusKCos = (1 + ki * cosTheta);
			
			resultArr[i] = (1 - kiSqrd) / (4 * Math.PI * onePlusKCos * onePlusKCos );
		}
		
		return result;
	}

	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getK() {
		return k;
	}

	public void setK(Texture3D k) {
		this.k = k;
	}
}
