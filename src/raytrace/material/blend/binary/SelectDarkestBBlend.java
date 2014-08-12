package raytrace.material.blend.binary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;

public class SelectDarkestBBlend extends BinaryBlend {

	/*
	 * A binary blend for selecting the material shade with the lowest intensity
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SelectDarkestBBlend(Material firstMaterial, Material secondMaterial)
	{
		super(firstMaterial, secondMaterial);
	}
	
	
	/* *********************************************************************************************
	 * Blend Override
	 * *********************************************************************************************/
	@Override
	public Color blend(Color firstShade, Color secondShade, ShadingData data)
	{
		return firstShade.intensity3() < secondShade.intensity3() ? firstShade : secondShade;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//
}