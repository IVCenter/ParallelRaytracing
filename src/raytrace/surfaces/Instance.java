package raytrace.surfaces;

import raytrace.material.Material;

public class Instance extends MatrixTransformSurface {
	
	/*
	 * A convenient renaming ;)
	 */

	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public void setMaterial(Material material)
	{
		for(CompositeSurface cs : this)
			cs.setMaterial(material);
	}
}
