package raytrace.surfaces;

import java.util.ArrayList;
import java.util.Collection;

import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.UpdateData;

public abstract class TerminalSurface extends CompositeSurface {

	/*
	 * Masks the composite aspects of the Composite Surface
	 */

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
	public void updateBoundingBox()
	{
		//Mask
	}

	/* *********************************************************************************************
	 * Masking Composite Methods
	 * *********************************************************************************************/
	
	public void addChild(CompositeSurface cs)
	{
		//Mask
	}
	
	public void addChildren(Collection<CompositeSurface> set)
	{	
		//Mask
	}
	
	public void addChildrenUnsafe(Collection<CompositeSurface> set)
	{	
		//Mask
	}
	
	public CompositeSurface removeChild(CompositeSurface cs)
	{
		//Mask
		return null;
	}
	
	public ArrayList<CompositeSurface> getChildren()
	{
		//Mask
		return null;
	}

}
