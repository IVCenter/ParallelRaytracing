package raytrace.geometry.meshes;

import java.util.ArrayList;

import math.Vector4;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.geometry.Triangle;
import raytrace.surfaces.TerminalSurface;

public class MeshSurface extends TerminalSurface {
	
	/*
	 * A simple sphere class
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Triangle> triangles;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public MeshSurface()
	{
		triangles = new ArrayList<Triangle>();
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data)
	{
		IntersectionData idata;
		IntersectionData closest = null;
		
		for(Triangle tri : triangles)
		{
			idata = tri.intersects(data);
			//If idata isn't null, and either closest is null, or idata is closer than closest
			if(idata != null && (closest == null || idata.getDistance() < closest.getDistance()))
			{
				closest = idata;
			}
		}
		
		return closest;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
	}
	
	@Override
	/**
	 * 
	 */
	public void updateBoundingBox()
	{
		//Clear the current bounding box
		boundingBox.clear();
		
		//Temp Storage
		Vector4 min;
		Vector4 max;
		BoundingBox bb;
		
		//Loop through all children bounding boxes and set this to bound them
		for(Triangle tri : triangles)
		{
			tri.updateBoundingBox();
			bb = tri.getBoundingBox();
			
			min = bb.min;
			max = bb.max;

			boundingBox.min.minimize3(min);
			boundingBox.max.maximize3(max);
		}
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}

	public void setTriangles(ArrayList<Triangle> triangles) {
		this.triangles = triangles;
	}
	
}