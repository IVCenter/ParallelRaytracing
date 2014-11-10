package raytrace.geometry.pointclouds;

import java.util.ArrayList;

import process.logging.Logger;
import raytrace.geometry.Vertex;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.acceleration.AABVHSurface;
import file.xyz.XyzPointCloudData;

public class PointCloudSurface extends CompositeSurface {
	
	/*
	 * A composite collection of points as loaded from an .xyz file
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	//protected static final int minSurfacesRequiredForAABVH = 5;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<PointSurface> pointSurfaces;
	protected AABVHSurface aabvh;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PointCloudSurface(XyzPointCloudData data)
	{
		Logger.progress(-1, "Starting creating a Point Cloud Surface...");
		
		pointSurfaces = new ArrayList<PointSurface>(data.getPoints().size());
		
		//For each object in the model, create an object surface
		for(Vertex vertex : data.getPoints())
		{
			//TODO: Make new point surface
			pointSurfaces.add(new PointSurface(vertex, 0.0006));
		}
		
		//Accelerate
		aabvh = AABVHSurface.makeAABVH(pointSurfaces);
		this.addChild(aabvh);
		
		//this.updateBoundingBox();
		
		Logger.progress(-1, "Ending creating a Point Cloud Surface.");
	}

	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public ArrayList<PointSurface> getPointSurfaces() {
		return pointSurfaces;
	}

	public AABVHSurface getAABVH() {
		return aabvh;
	}


	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	/*
	@Override
	public void setMaterial(Material material)
	{
		for(CompositeSurface cs : pointSurfaces)
			cs.setMaterial(material);
	}
	*/

}
