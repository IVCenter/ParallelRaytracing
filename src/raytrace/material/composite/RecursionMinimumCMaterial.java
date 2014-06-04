package raytrace.material.composite;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;

public class RecursionMinimumCMaterial extends CompositeMaterial {

	/*
	 * A composite material that returns black if the recursion level is below the given minimum
	 * 
	 * Useful for having global lighting without showing the sky box
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected int recursionMinimum;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RecursionMinimumCMaterial(Material material)
	{
		super(material);
		recursionMinimum = 0;
	}
	
	public RecursionMinimumCMaterial(Material material, int recursionMinimum)
	{
		super(material);
		this.recursionMinimum = recursionMinimum;
	}

	
	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		if(data.getRecursionDepth() < recursionMinimum)
			return Color.black();
		
		//Shade
		return material.shade(data);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int getRecursionMinimum() {
		return recursionMinimum;
	}

	public void setRecursionMinimum(int recursionMinimum) {
		this.recursionMinimum = recursionMinimum;
	}

}
