package raytrace.surfaces;

import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.UpdateData;
import raytrace.material.Material;

public abstract class GeometrySurface extends AbstractSurface implements MaterialSurface {

	/*
	 * Masks the composite aspects of the Composite Surface
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Material material;

	
	/* *********************************************************************************************
	 * Masking Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		//Mask
		return null;
	}
	
	@Override
	public void update(UpdateData data)
	{
		//Mask
	}
	
	@Override
	public void bake(BakeData data)
	{
		//Mask
	}
	
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
}
