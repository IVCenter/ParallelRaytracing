package raytrace.geometry.meshes;

import java.util.ArrayList;
import java.util.HashMap;

import math.Vector4;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.geometry.Triangle;
import raytrace.surfaces.TerminalSurface;

public class MeshSurface extends TerminalSurface {
	
	/*
	 * A simple mesh class
	 */
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static int nextMeshID = 0;
	private static HashMap<Integer, MeshSurface> meshes;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		meshes = new HashMap<Integer, MeshSurface>();
	}
	
	
	/* *********************************************************************************************
	 * Static Helper Methods
	 * *********************************************************************************************/
	private synchronized static int nextMeshID()
	{
		return nextMeshID++;
	}
	
	public static void removeMeshReference(int meshID)
	{
		meshes.put(meshID, null);
	}
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Triangle> triangles;
	protected int meshID = nextMeshID();
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public MeshSurface()
	{
		meshes.put(meshID, this);
		triangles = new ArrayList<Triangle>();
	}
	
	public MeshSurface(int triangleCount)
	{
		meshes.put(meshID, this);
		triangles = new ArrayList<Triangle>(triangleCount);
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
		
		if(closest != null)
			closest.setMeshID(meshID);
		
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

			boundingBox.min.minimize3M(min);
			boundingBox.max.maximize3M(max);
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

	public int getMeshID() {
		return meshID;
	}

	public void setMeshID(int meshID) {
		this.meshID = meshID;
	}
	
}