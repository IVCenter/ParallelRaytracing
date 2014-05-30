package raytrace.material.composite;

import raytrace.material.Material;

public abstract class CompositeMaterial extends Material {
	
	/*
	 * A base class for composite materials
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Material material;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public CompositeMaterial(Material material)
	{
		this.material = material;
	}


	/* *********************************************************************************************
	 * Setters/Getters
	 * *********************************************************************************************/
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
	
}
