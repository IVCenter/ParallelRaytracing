package raytrace.surfaces;

import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.material.Material;

public class Instance extends MatrixTransformSurface implements MaterialSurface {
	
	/*
	 * A convenient renaming ;)
	 */
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	protected Material material;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public Instance()
	{
		super();
	}
	

	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public Material getMaterial()
	{
		return material;
	}
	
	@Override
	public void setMaterial(Material material)
	{
		this.material = material;
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
