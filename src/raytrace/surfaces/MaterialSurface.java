package raytrace.surfaces;

import raytrace.material.Material;

public interface MaterialSurface {
	
	/*
	 * An interface for surfaces that are physical and based on a material
	 */
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Material getMaterial();

	public void setMaterial(Material material);

}
