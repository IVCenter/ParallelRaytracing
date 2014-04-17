package raytrace.surfaces.acceleration;

import java.util.ArrayList;
import java.util.Collection;

import process.logging.Logger;

import math.Vector4;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.surfaces.CompositeSurface;

public class AABVHSurface extends CompositeSurface {
	
	/*
	 * An axis aligned bounding volume hierarchy
	 */
	/* *********************************************************************************************
	 * Instnace Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	private AABVHSurface()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		if(children == null)
			return null;
		
		//TODO: Check against BB, if miss, return null
		double bbIntersection = boundingBox.intersects(data);
		if(bbIntersection == Double.MAX_VALUE)
			return null;
		
		/*
		int childCount = getChildren().size();
		
		if(childCount > 2)
			return super.intersects(data);
		else if(childCount == 1)
			return this.getChildren().get(0).intersects(data);
		
		
		//Get the bounding boxes and intersection times
		CompositeSurface[] surfaces = new CompositeSurface[childCount];
		double[] intersections = new double[surfaces.length];

		surfaces[0] = this.getChildren().get(0);
		surfaces[1] = this.getChildren().get(1);
		intersections[0] = surfaces[0].getBoundingBox().intersects(data);
		intersections[1] = surfaces[1].getBoundingBox().intersects(data);
		
		//Sort
		//insertionSortSideBySide(intersections, surfaces);

		double swapTemp;
		CompositeSurface swapTempSurface;
		if(intersections[1] < intersections[0])
		{
			swapTemp = intersections[0];
			swapTempSurface = surfaces[0];
			intersections[0] = intersections[1];
			surfaces[0] = surfaces[1];
			intersections[1] = swapTemp;
			surfaces[1] = swapTempSurface;
		}
		
		
		
		IntersectionData idata;
		IntersectionData closest = null;
		CompositeSurface cs;
		double time;
		for(int i = 0; i < 2; ++i)
		{
			cs = surfaces[i];
			time = intersections[i];
			if(time == Double.MAX_VALUE) {
				continue;
			}
			
			idata = cs.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getTime() < closest.getTime()))
			{
				closest = idata;
				if(i < surfaces.length-1 && closest.getTime() <= intersections[i])
				{
					//System.out.println("Returning early.  Closest[" + closest.getTime() + "] NextBox[" + intersections[i] + "]");
					return closest;
				}
			}
		}
		
		return closest;
		*/
		
		//TODO: check children BBs
		//Sort by hit time
		//Dive in order
		//if hit returns, and time less than next BB time, return hit
		return super.intersects(data);
	}
	
	/*
	private void insertionSortSideBySide(double[] data, CompositeSurface[] css)
	{	
		if(data.length < 2 || data.length != css.length)
			return;

		double swapTemp;
		CompositeSurface swapTempSurface;
		
		//Special case for 2 items
		if(data.length == 2)
		{
			if(data[1] < data[0])
			{
				swapTemp = data[0];
				swapTempSurface = css[0];
				data[0] = data[1];
				css[0] = css[1];
				data[1] = swapTemp;
				css[1] = swapTempSurface;
			}
			return;
		}
		
		//General case
		double insertTemp;
		CompositeSurface insertTempSurface;
		
		for(int i = 1; i < data.length; ++i)
		{
			insertTemp = data[i];
			insertTempSurface = css[i];
			for(int j = i-1; j >= 0; --j)
			{
				swapTemp = data[j];
				swapTempSurface = css[j];
				if(swapTemp > insertTemp)
				{
					data[j+1] = swapTemp;
					css[j+1] = swapTempSurface;
				}else {
					data[j+1] = insertTemp;
					css[j+1] = insertTempSurface;
					break;
				}
				
				if(j == 0) {
					data[0] = insertTemp;
					css[0] = insertTempSurface;
				}
			}
		}
		
	}
	
	private void printArray(double[] data)
	{
		StringBuilder sb = new StringBuilder("[");
		for(double d : data)
			sb.append(d + ", ");
		sb.append("]");
		System.out.println(sb.toString());
	}
	*/

	@Override
	public void bake(BakeData data)
	{
		super.bake(data);
	}

	@Override
	public void updateBoundingBox()
	{
		super.updateBoundingBox();
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Static Creation Methods
	 * *********************************************************************************************/
	public static <SURFACE extends CompositeSurface> AABVHSurface makeAABVH(Collection<SURFACE> surfaces)
	{
		int slices = 1;
		int maxSurfacesPerLeaf = 4;
		return makeAABVH(surfaces, slices, maxSurfacesPerLeaf);
	}
	
	public static <SURFACE extends CompositeSurface> AABVHSurface makeAABVH(Collection<SURFACE> surfaces, int slices, int maxSurfacesPerLeaf)
	{
		/*
		 * Steps:
		 * 
		 * 		-Build a AABB for the set of surfaces
		 * 		-For each axis, and for each of a set of planes orthogonal to that axis
		 * 			-Split the set of surface into -/+ sides of the AAP
		 * 			-Calculate SAH*N
		 * 		-Use the split with the minimum SAH*N
		 * 		-Recurse for both sets
		 */

		//Logger.progress(-1, "Surfaces Size[" + surfaces.size() + "].");
		
		//Get bounding box
		BoundingBox topBB = makeBoundingBox(surfaces);
		
		//Get spatial axis data
		double[] axisWidths = {topBB.max.get(0) - topBB.min.get(0), 
							   topBB.max.get(1) - topBB.min.get(1), 
							   topBB.max.get(2) - topBB.min.get(2)};
		
		//Center point might be better...
		Vector4 center = centerPoint(surfaces);
		
		//Get axis data
		double[] axisValues = center.getM();
		
		//Make surface sets
		ArrayList<SURFACE> negative = new ArrayList<SURFACE>();
		ArrayList<SURFACE> positive = new ArrayList<SURFACE>();
		
		//Best axis data
		double lowestSAH = Double.MAX_VALUE;
		double currentSAH = 0.0;
		double currentAxisValue = 0.0;
		int lowestAxis = -1;
		double lowestAxisValue = Integer.MAX_VALUE;
		
		//For each exis, split the sets
		for(int i = 0; i < 3; ++i)
		{
			for(int slice = -slices/2; slice <= slices/2; ++slice)
			{
				//Get the plane position for the current axis/slice
				currentAxisValue = axisValues[i] + (slice * (axisWidths[i])/(double)slices);
				
				//Split across current axis
				split(surfaces, negative, positive, i, currentAxisValue);
				
				//Compute the SAH for the current sets
				currentSAH = calculateSAH(negative, positive);

				//If the current SAH is lower than the current lowest
				if(currentSAH < lowestSAH)
				{
					lowestSAH = currentSAH;
					lowestAxis = i;
					lowestAxisValue = currentAxisValue;
				}
				
				//Clear
				negative.clear();
				positive.clear();
			}
		}

		//Split on the lowest axis
		split(surfaces, negative, positive, lowestAxis, lowestAxisValue);
		
		//Make a root surface
		AABVHSurface rootSurface = new AABVHSurface();
		
		//If we encounter a set that is smaller than the per leaf max, add the surfaces and return;
		if(surfaces.size() <= maxSurfacesPerLeaf) {
			for(CompositeSurface cs : surfaces)
				rootSurface.addChild(cs);
			return rootSurface;
		}
		
		//If we encounter a set of surfaces that the current SAH can not partition further
		int loopCount = 0;
		while(negative.size() == surfaces.size() || positive.size() == surfaces.size())
		{
			negative.clear();
			positive.clear();
			int index = (int)(Math.random() * 3);
			split(surfaces, negative, positive, index, axisValues[index]);
			
			//If we've tried all of the axes, just add them to the rootSurface
			//There is a good chance that all objects are nearly ontop of each other
			if(++loopCount >= 3)
				for(CompositeSurface cs : surfaces)
					rootSurface.addChild(cs);
				return rootSurface;
		}
		
		//Recurse on the negative and positive sets, adding their result to this
		rootSurface.addChild(AABVHSurface.makeAABVH(negative, slices, maxSurfacesPerLeaf));
		rootSurface.addChild(AABVHSurface.makeAABVH(positive, slices, maxSurfacesPerLeaf));
		
		return rootSurface;
	}
	
	private static <SURFACE extends CompositeSurface> void split(Collection<SURFACE> surfaces, 
			ArrayList<SURFACE> negative, ArrayList<SURFACE> positive,
			int axis, double axisValue)
	{
		BoundingBox surfacebb;
		
		//For all of the surfaces, place them in either the negative or positive side
		for(SURFACE cs : surfaces)
		{
			surfacebb = cs.getBoundingBox();
			
			//If on the negative side, add to negative
			if(surfacebb.min.get(axis) < axisValue && surfacebb.max.get(axis) < axisValue)
			{
				negative.add(cs);
				
			//If on the positive side, add to positive
			}else if(surfacebb.min.get(axis) > axisValue && surfacebb.max.get(axis) > axisValue)
			{
				positive.add(cs);
				
			//If on both sides, add to negative
			}else{
				negative.add(cs);
				//positive.add(cs);
			}
		}
	}
	
	private static <SURFACE extends CompositeSurface> BoundingBox makeBoundingBox(Collection<SURFACE> surfaces)
	{
		//Clear the current bounding box
		BoundingBox boundingBox = new BoundingBox();
		boundingBox.clear();
		
		//Temp Storage
		Vector4 min;
		Vector4 max;
		BoundingBox bb;
		
		//Loop through all children bounding boxes and set this to bound them
		for(CompositeSurface cs : surfaces)
		{
			cs.updateBoundingBox();
			bb = cs.getBoundingBox();
			
			min = bb.min;
			max = bb.max;

			boundingBox.min.minimize3(min);
			boundingBox.max.maximize3(max);
		}
		
		return boundingBox;
	}
	
	private static <SURFACE extends CompositeSurface> Vector4 centerPoint(Collection<SURFACE> surfaces)
	{
		Vector4 center = new Vector4();
		double[] m = center.getM();

		double count = 0;
		
		BoundingBox bb;
		Vector4 mp;
		double[] mpm;
		for(SURFACE cs : surfaces)
		{
			bb = cs.getBoundingBox();
			mp = bb.getMidpoint();
			mpm = mp.getM();
			m[0] += mpm[0];
			m[1] += mpm[1];
			m[2] += mpm[2];
			count += 1.0;
		}
		
		center.set(m[0]/count, m[1]/count, m[2]/count, 0);
		
		return center;
	}
	
	private static <SURFACE extends CompositeSurface> double calculateSAH(
			ArrayList<SURFACE> negative, ArrayList<SURFACE> positive)
	{
		BoundingBox negativeBB = makeBoundingBox(negative);
		BoundingBox positiveBB = makeBoundingBox(positive);
		
		double sah = 0.0;
		
		if(negative.size() > 0)
			sah += negativeBB.getSurfaceArea() * (double)(negative.size()/* * negative.size()*/);
		if(positive.size() > 0)
			sah += positiveBB.getSurfaceArea() * (double)(positive.size()/* * positive.size()*/);
			
		/*
		if(negative.size() > 0)
			sah += negativeBB.getSurfaceArea() * (double)negative.size();
		if(positive.size() > 0)
			sah += positiveBB.getSurfaceArea() * (double)positive.size();
			*/
		
		return sah;
	}
	
}
