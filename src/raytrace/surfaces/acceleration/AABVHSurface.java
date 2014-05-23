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
		
		
		int childCount = getChildren().size();
		
		//If we have too many, or too few, children to heuristically perform an intersection test, 
		//use the default test.
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
		
		
		//If there are no intersections then bail out.
		if(intersections[1] == Double.MAX_VALUE && intersections[0] == Double.MAX_VALUE)
		{
			return null;
		}
		
		//If the intersections are sorted in reverse, flip them
		int first = 0;
		int second = 1;
		if(intersections[1] < intersections[0])
		{
			first = 1;
			second = 0;
		}
		

		IntersectionData idata = null;
		IntersectionData closest = null;
		
		//Test the closest bounding box first
		if(intersections[first] != Double.MAX_VALUE)
		{
			idata = surfaces[first].intersects(data);
			if(idata != null)
			{
				closest = idata;
				//If idata isn't null, idata is closer than second bounding box
				if(closest.getTime() < intersections[second])
				{
					return closest;
				}
			}
		}
		
		//If necessary, test the second bounding box
		if(intersections[second] != Double.MAX_VALUE)
		{
			idata = surfaces[second].intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getTime() < closest.getTime()))
			{
				closest = idata;
			}
		}
		
		return closest;
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
	public static <SURFACE extends CompositeSurface> AABVHSurface makeAABVH(Collection<SURFACE> surfaces)
	{
		int slices = 1;
		int maxSurfacesPerLeaf = 8;
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
		double[] axisValues = center.getArray();
		
		//Make surface sets
		ArrayList<SURFACE> negative = new ArrayList<SURFACE>();
		ArrayList<SURFACE> positive = new ArrayList<SURFACE>();
		
		//Best axis data
		double lowestSAH = Double.MAX_VALUE;
		double currentSAH = 0.0;
		double currentAxisValue = 0.0;
		int lowestAxis = 0;
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
			if(++loopCount >= 3) {
				for(CompositeSurface cs : surfaces)
					rootSurface.addChild(cs);
				return rootSurface;
			}
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
		double[] m = center.getArray();

		double count = 0;
		
		BoundingBox bb;
		Vector4 mp;
		double[] mpm;
		for(SURFACE cs : surfaces)
		{
			bb = cs.getBoundingBox();
			mp = bb.getMidpoint();
			mpm = mp.getArray();
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
