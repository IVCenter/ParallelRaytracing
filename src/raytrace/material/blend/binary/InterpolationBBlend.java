package raytrace.material.blend.binary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;

public class InterpolationBBlend extends BinaryBlend {

	/*
	 * A binary blend for interpolating between two materials
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double t;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public InterpolationBBlend(Material firstMaterial, Material secondMaterial, double t)
	{
		super(firstMaterial, secondMaterial);
		this.t = t;
	}
	
	
	/* *********************************************************************************************
	 * Blend Override
	 * *********************************************************************************************/
	@Override
	public Color blend(Color firstShade, Color secondShade, ShadingData data)
	{
		return firstShade.interpolate(firstShade, secondShade, t);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}
}
