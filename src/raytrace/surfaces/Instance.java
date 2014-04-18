package raytrace.surfaces;

import raytrace.data.IntersectionData;
import raytrace.data.RayData;
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
		this.material = material;
		//for(CompositeSurface cs : this)
		//	cs.setMaterial(material);
	}
	
	@Override
	public IntersectionData intersects(RayData data)
	{
		IntersectionData idata = super.intersects(data);
		if(idata != null && material != null) {
			idata.setMaterial(this.material);
		}
		return idata;
	}
}
