package raytrace.geometry.meshes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import math.Vector3;

import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.material.Material;
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
	
	@Deprecated
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
		//TODO: This can cause leaks, disabled for now
		//meshes.put(meshID, this);
		triangles = new ArrayList<Triangle>();
	}
	
	public MeshSurface(int triangleCount)
	{
		//TODO: This can cause leaks, disabled for now
		//meshes.put(meshID, this);
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
	
	public void synchronizeVertices()
	{
		//Finds are vertices at the same location and converts them to the same instance
		//This is useful for procedural geometry where it is expensive to keep track of 
		//	vertices on shared edges
		
		HashMap<String, Vector3> seenVectors = new HashMap<String, Vector3>();
		
		String encoding;
		Vector3 existing;
		for(Triangle tri : triangles)
		{
			for(Vertex vert : tri.getVertices())
			{
				encoding = encodeVector(vert.getPosition());
				existing = seenVectors.get(encoding);
				
				if(existing == null) {
					existing = vert.getPosition();
					seenVectors.put(encoding, existing);
				}
				
				vert.setPosition(existing);
			}
		}
	}
	
	public void smoothNormals()
	{
		//Another encoding and hash based method for fast processing of triangle normals
		//Collect all Vertices that share a position
		//For each group, average the normals
		
		HashMap<String, LinkedList<Vertex>> groups = new HashMap<String, LinkedList<Vertex>>();
		
		String encoding;
		LinkedList<Vertex> group;
		for(Triangle tri : triangles)
		{
			for(Vertex vert : tri.getVertices())
			{
				encoding = encodeVector(vert.getPosition());
				group = groups.get(encoding);
				
				if(group == null) {
					group = new LinkedList<Vertex>();
					groups.put(encoding, group);
				}
				
				group.add(vert);
			}
		}
		
		//For each group generate an average and set them
		Vector3 averageNormal = new Vector3();
		Vector3 tempNormal;
		for(Map.Entry<String, LinkedList<Vertex>> entry : groups.entrySet())
		{
			group = entry.getValue();
			averageNormal.set(0, 0, 0);
			
			for(Vertex vert : group)
			{
				averageNormal.addM(vert.getNormal());
			}
			
			averageNormal.normalizeM();
			
			tempNormal = averageNormal.add(0);
			
			for(Vertex vert : group)
			{
				vert.setNormal(tempNormal);
			}
			
		}
	}
	
	public void tessellateMeshByTriangleAreaConstraint(double areaConstraint)
	{
		ArrayList<Triangle> newTriangles = new ArrayList<Triangle>();
		ArrayList<Triangle> tessellated;
		
		for(Triangle tri : triangles)
		{
			if(tri.getArea() > areaConstraint)
			{
				//Tessellate the triangle
				tessellated = tri.tessellateByAreaConstraint(areaConstraint);
				for(Triangle ttri : tessellated)
				{
					newTriangles.add(ttri);
				}
			}else{
				newTriangles.add(tri);
			}
		}
		
		triangles.clear();
		triangles = newTriangles;
	}
	
	public void tessellateMeshByTriangleLongestSideConstraint(double lengthConstraint)
	{
		ArrayList<Triangle> newTriangles = new ArrayList<Triangle>();
		ArrayList<Triangle> tessellated;
		
		for(Triangle tri : triangles)
		{
			if(tri.getLongestSide() > lengthConstraint)
			{
				//Tessellate the triangle
				tessellated = tri.tessellateByLongestSideConstraint(lengthConstraint);
				for(Triangle ttri : tessellated)
				{
					newTriangles.add(ttri);
				}
			}else{
				newTriangles.add(tri);
			}
		}
		
		triangles.clear();
		triangles = newTriangles;
	}
	
	private String encodeVector(Vector3 v)
	{
		StringBuilder sb = new StringBuilder();
		
		//The float cast here is a terrible idea
		sb.append("<");
		sb.append(Double.toString((float)v.get(0)));
		sb.append(", ");
		sb.append(Double.toString((float)v.get(1)));
		sb.append(", ");
		sb.append(Double.toString((float)v.get(2)));
		sb.append(", ");
		sb.append(Double.toString((float)v.get(3)));
		sb.append(">");
		
		return sb.toString();
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
		Vector3 min;
		Vector3 max;
		BoundingBox bb;
		
		//Loop through all children bounding boxes and set this to bound them
		for(Triangle tri : triangles)
		{
			tri.updateBoundingBox();
			bb = tri.getBoundingBox();
			
			min = bb.min;
			max = bb.max;

			boundingBox.min.minimizeM(min);
			boundingBox.max.maximizeM(max);
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

	public void setMaterial(Material material)
	{
		for(Triangle cs : triangles)
			cs.setMaterial(material);
	}
	
}