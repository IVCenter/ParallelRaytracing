package raytrace.material.composite;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.ColorMaterial;
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
	protected Material belowMinimumMaterial;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RecursionMinimumCMaterial(Material material)
	{
		super(material);
		recursionMinimum = 0;
		belowMinimumMaterial = new ColorMaterial(Color.black());
	}
	
	public RecursionMinimumCMaterial(Material material, int recursionMinimum)
	{
		super(material);
		this.recursionMinimum = recursionMinimum;
		this.belowMinimumMaterial = new ColorMaterial(Color.black());
	}
	
	public RecursionMinimumCMaterial(Material material, Material belowMinimumMaterial, int recursionMinimum)
	{
		super(material);
		this.recursionMinimum = recursionMinimum;
		this.belowMinimumMaterial = belowMinimumMaterial;
	}

	
	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		if(data.getRecursionDepth() < recursionMinimum)
			return belowMinimumMaterial.shade(data);
		
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
