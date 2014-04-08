package raytrace.surfaces.acceleration;

import java.util.ArrayList;
import java.util.Collection;

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
	public AABVHSurface()
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
		/*
		 * Steps:
		 * 
		 * 		-Build a AABB for the set of surfaces
		 * 		-Calculate the midpoint
		 * 		-For each component of the midpoint, create an AAP
		 * 			-Split the set of surface into -/+ sides of the AAP
		 * 			-Calculate SAH*N
		 * 		-Use the split with the minimum SAH*N
		 * 		-Recurse for both sets
		 */

		System.out.println("Surfaces Size[" + surfaces.size() + "].");
		
		//Get bounding box and mid point
		//BoundingBox topBB = makeBoundingBox(surfaces);
		//Vector4 midPoint = topBB.min.add3(topBB.max).multiply3(0.5);
		
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
		int lowestAxis = -1;
		
		//For each exis, split the sets
		for(int i = 0; i < 3; ++i)
		{
			//Split across current axis
			split(surfaces, negative, positive, i, axisValues);
			//System.out.println("Negative Size[" + negative.size() + "]\tPositive Size[" + positive.size() + "]\t");
			
			//Compute the SAH for the current sets
			currentSAH = calculateSAH(negative, positive);
			//System.out.println("CurrentSAH: " + currentSAH);
			if(currentSAH < lowestSAH)
			{
				lowestSAH = currentSAH;
				lowestAxis = i;
			}
			
			//Clear
			negative.clear();
			positive.clear();
		}

		//Split on the lowest axis
		split(surfaces, negative, positive, lowestAxis, axisValues);
		//System.out.println("Splitting on Axis[" + lowestAxis + "]");
		//System.out.println("Negative Size[" + negative.size() + "]\tPositive Size[" + positive.size() + "]\t");
		
		//Build subtrees
		AABVHSurface root = new AABVHSurface();
		
		//If we encounter a set that can not be partitioned, add the surfaces and return;
		if(negative.size() == surfaces.size() || positive.size() == surfaces.size() || surfaces.size() <= 8) {
			for(CompositeSurface cs : surfaces)
				root.addChild(cs);
			return root;
		}
		
		root.addChild(AABVHSurface.makeAABVH(negative));
		root.addChild(AABVHSurface.makeAABVH(positive));
		
		return  root;
	}
	
	private static void split(Collection<CompositeSurface> surfaces, 
			ArrayList<CompositeSurface> negative, ArrayList<CompositeSurface> positive,
			int axis, double[] axisValues)
	{
		BoundingBox surfacebb;
		
		for(CompositeSurface cs : surfaces)
		{
			surfacebb = cs.getBoundingBox();
			
			//If on the negative side, add to negative
			if(surfacebb.min.get(axis) < axisValues[axis] && surfacebb.max.get(axis) < axisValues[axis])
			{
				negative.add(cs);
				
			//If on the positive side, add to positive
			}else if(surfacebb.min.get(axis) > axisValues[axis] && surfacebb.max.get(axis) > axisValues[axis])
			{
				positive.add(cs);
				
			//If on both sides, add to both
			}else{
				negative.add(cs);
				positive.add(cs);
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
			sah += negativeBB.getSurfaceArea() / (double)negative.size();
		if(positive.size() > 0)
			sah += positiveBB.getSurfaceArea() / (double)positive.size();
		
		return sah;
	}
	
}
