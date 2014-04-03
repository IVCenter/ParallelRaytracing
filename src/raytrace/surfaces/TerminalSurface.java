package raytrace.surfaces;

import java.util.ArrayList;
import java.util.Collection;

public abstract class TerminalSurface extends CompositeSurface {

	/*
	 * Masks the composite aspects of the Composite Surface
	 */

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
