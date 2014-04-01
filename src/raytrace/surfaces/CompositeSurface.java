package raytrace.surfaces;

import java.util.ArrayList;
import java.util.Iterator;

import math.Ray;

import raytrace.data.IntersectionData;
import raytrace.data.TraceData;
import raytrace.framework.Composite;
import raytrace.framework.Node;
import raytrace.framework.Surface;

public abstract class CompositeSurface implements Node, Composite<CompositeSurface>, Surface
{
	/*
	 * A base class for nodes of a surface graph
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<CompositeSurface> children;
	

	/* *********************************************************************************************
	 * Surface-related Methods
	 * *********************************************************************************************/
	public TraceData trace(Ray ray)
	{
		return trace(ray, 0, Double.POSITIVE_INFINITY);
	}

	public IntersectionData intersects(Ray ray)
	{
		return intersects(ray, 0, Double.POSITIVE_INFINITY);
	}

	/* *********************************************************************************************
	 * Composite-related Methods
	 * *********************************************************************************************/
	@Override
	public Iterator<CompositeSurface> iterator()
	{
		if(children == null)
			return null;
		return children.iterator();
	}
	
	public void addChild(CompositeSurface cs)
	{
		//By default children is not initialized so that leaf nodes don't incur the memory penalty of empty structures
		if(children == null)
			children = new ArrayList<CompositeSurface>();
		
		//Having the same surface added twice uses unnecessary resources.
		//This contains method is slow
		if(!children.contains(cs))
			children.add(cs);
	}
	
	public CompositeSurface removeChild(CompositeSurface cs)
	{
		//By default children is not initialized so that leaf nodes don't incur the memory penalty of empty structures
		if(children == null) {
			children = new ArrayList<CompositeSurface>();
			return null;
		}
		
		//Return the item removed only if it actually existed, else return null
		if(children.remove(cs))
			return cs;
		return null;
	}
	
	public ArrayList<CompositeSurface> getChildren()
	{
		return children;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
}
