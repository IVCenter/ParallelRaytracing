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
		if(!boundingBox.intersects(data))
			return null;
		
		return super.intersects(data);
	}

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
	public static AABVHSurface makeAABVH(Collection<CompositeSurface> surfaces)
	{
		int slices = 100;
		int maxSurfacesPerLeaf = 4;
		return makeAABVH(surfaces, slices, maxSurfacesPerLeaf);
	}
	
	public static AABVHSurface makeAABVH(Collection<CompositeSurface> surfaces, int slices, int maxSurfacesPerLeaf)
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

		Logger.progress(-1, "Surfaces Size[" + surfaces.size() + "].");
		
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
		ArrayList<CompositeSurface> negative = new ArrayList<CompositeSurface>();
		ArrayList<CompositeSurface> positive = new ArrayList<CompositeSurface>();
		
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
		
		//If we encounter a set of surfaces that the current SAH can not partition further
		/*if(negative.size() == surfaces.size() || positive.size() == surfaces.size())*/
		//TODO: Then what do we do?
		
		//If we encounter a set that is smaller than the per leaf max, add the surfaces and return;
		if(surfaces.size() <= maxSurfacesPerLeaf) {
			for(CompositeSurface cs : surfaces)
				rootSurface.addChild(cs);
			return rootSurface;
		}
		
		//Recurse on the negative and positive sets, adding their result to this
		rootSurface.addChild(AABVHSurface.makeAABVH(negative));
		rootSurface.addChild(AABVHSurface.makeAABVH(positive));
		
		return rootSurface;
	}
	
	private static void split(Collection<CompositeSurface> surfaces, 
			ArrayList<CompositeSurface> negative, ArrayList<CompositeSurface> positive,
			int axis, double axisValue)
	{
		BoundingBox surfacebb;
		
		//For all of the surfaces, place them in either the negative or positive side
		for(CompositeSurface cs : surfaces)
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
	
	private static BoundingBox makeBoundingBox(Collection<CompositeSurface> surfaces)
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
	
	private static Vector4 centerPoint(Collection<CompositeSurface> surfaces)
	{
		Vector4 center = new Vector4();
		double[] m = center.getM();

		double count = 0;
		
		BoundingBox bb;
		Vector4 mp;
		double[] mpm;
		for(CompositeSurface cs : surfaces)
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
	
	private static double calculateSAH(ArrayList<CompositeSurface> negative, ArrayList<CompositeSurface> positive)
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
