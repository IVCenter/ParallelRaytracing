package raytrace.surfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
//import java.util.LinkedList;
import java.util.List;

import math.Vector3;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.UpdateData;
import raytrace.framework.Composite;

public abstract class CompositeSurface extends AbstractSurface implements Composite<AbstractSurface>
{
	/*
	 * A base class for composite nodes of a surface graph
	 */
	

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<AbstractSurface> children;
	

	/* *********************************************************************************************
	 * Surface-related Methods
	 * *********************************************************************************************/
	//No longer supported
	/*
	public TraceData trace(RayData data)
	{
		return trace(ray, 0, Double.POSITIVE_INFINITY);
	}
	*/

	@Override
	public IntersectionData intersects(RayData data)
	{
		//If no children, then return
		if(children == null)
			return null;
		
		IntersectionData idata;
		IntersectionData closest = null;
		
		for(AbstractSurface cs : this)
		{
			idata = cs.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getTime() < closest.getTime()))
			{
				closest = idata;
			}
		}
		
		return closest;
	}
	
	@Override
	public void update(UpdateData data)
	{
		//If no children, then return
		if(children == null)
			return;
		
		//Update all children
		for(AbstractSurface cs : this)
		{
			cs.update(data);
		}
	}
	
	@Override
	public void bake(BakeData data)
	{
		//If no children, then return
		if(children == null)
			return;
		
		//Update all children
		for(AbstractSurface cs : this)
		{
			cs.bake(data);
		}
	}
	
	/*
	public void updateBoundingBox()
	{
		
	}
	*/
	
	public BoundingBox getBoundingBox()
	{
		BoundingBox boundingBox = new BoundingBox();
		
		//If no children or this is a static surface, then return
		if(children == null)
			return null;
		
		//Temp Storage
		Vector3 min;
		Vector3 max;
		BoundingBox bb;
		
		//Loop through all children bounding boxes and set this to bound them
		for(AbstractSurface cs : this)
		{
			bb = cs.getBoundingBox();
			
			min = bb.min;
			max = bb.max;

			boundingBox.min.minimizeM(min);
			boundingBox.max.maximizeM(max);
		}
		
		return boundingBox;
	}

	/* *********************************************************************************************
	 * Composite-related Methods
	 * *********************************************************************************************/
	@Override
	public Iterator<AbstractSurface> iterator()
	{
		if(children == null)
			return null;
		return children.iterator();
	}
	
	public void addChild(AbstractSurface cs)
	{
		//By default children is not initialized so that leaf nodes don't incur the memory penalty of empty structures
		if(children == null)
			children = new ArrayList<AbstractSurface>(2);
		
		//Having the same surface added twice uses unnecessary resources.
		//This contains method is slow
		if(cs != null && !children.contains(cs))
			children.add(cs);
	}
	
	public void addChildren(Collection<AbstractSurface> set)
	{	
		//Add each child, one by one
		for(AbstractSurface cs : set)
			addChild(cs);
	}
	
	public void addChildrenUnsafe(Collection<AbstractSurface> set)
	{	
		//Add all children at once
		//This does not check for null or duplicate surfaces
		if(children == null)
			children = new ArrayList<AbstractSurface>(set.size());
		children.addAll(set);
	}
	
	public AbstractSurface removeChild(AbstractSurface cs)
	{
		//By default children is not initialized so that leaf nodes don't incur the memory penalty of empty structures
		if(children == null) {
			children = new ArrayList<AbstractSurface>(2);
			return null;
		}
		
		//Return the item removed only if it actually existed, else return null
		if(children.remove(cs))
			return cs;
		return null;
	}
	
	public List<AbstractSurface> getChildren()
	{
		return children;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/

}
