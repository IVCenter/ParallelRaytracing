package raytrace.geometry.meshes;

import java.util.ArrayList;

import math.Vector4;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.material.Material;

public class Grid extends MeshSurface {
	
	/*
	 * A simple grid of triangle quads
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double w;
	protected double h;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Grid()
	{
		//Unit cube centered at the origin
		super();
		initialize(1,1);
	}
	
	public Grid(double width, double height)
	{
		super();
		initialize(width, height);
	}

	
	/* *********************************************************************************************
	 * Init Triangles
	 * *********************************************************************************************/
	public void initialize(double width, double height)
	{
		w = width * 0.5;
		h = height * 0.5;

		ArrayList<Triangle> tris = generateTriangles(w, h, 1);
		this.triangles = tris;

		updateBoundingBox();
		dynamic = false;
	}
	
	protected ArrayList<Triangle> generateTriangles(double width, double height, double resolution)
	{
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		//Vertices
		Vector4 p010 = new Vector4(-width, 0, -height, 0);
		Vector4 p011 = new Vector4(-width, 0, height, 0);
		Vector4 p110 = new Vector4(width, 0, -height, 0);
		Vector4 p111 = new Vector4(width, 0, height, 0);

		
		//Normals
		Vector4 YAxis = new Vector4(0,1,0,0);

		//Top
		{
			generateFaceTriangle(triangles, p011, p111, p110, p010, YAxis, resolution);
		}
				
		return triangles;
	}
	
	protected void generateFaceTriangle(ArrayList<Triangle> tris, 
			Vector4 v0, Vector4 v1, Vector4 v2, Vector4 v3, Vector4 normal,
			double resolution)
	{
		Vector4 xOffset;
		Vector4 yOffset;
		Vector4 xSide = v3.subtract3(v0);
		Vector4 ySide = v1.subtract3(v0);
		Vector4 xSideDelta = v3.subtract3(v0).multiply3M(1.0/(double)resolution);
		Vector4 ySideDelta = v1.subtract3(v0).multiply3M(1.0/(double)resolution);
		
		Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;
		
		//Interpolate the 4 corners
		for(int x = 0; x < resolution; x++)
		{
			xOffset = xSide.multiply3((double)x / (double)resolution);
			for(int y = 0; y < resolution; y++)
			{
				yOffset = ySide.multiply3((double)y / (double)resolution);
				
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add3(xOffset).add3M(yOffset), 
						normal, 
						new Vector4((double)(x + 0) / (double)resolution, (double)(y + 0) / (double)resolution, 0, 0));
				
				subV0_2 = new Vertex(
						v0.add3(xOffset).add3M(yOffset), 
						normal, 
						new Vector4((double)(x + 0) / (double)resolution, (double)(y + 0) / (double)resolution, 0, 0));
				
				subV1 = new Vertex(
						v0.add3(xOffset).add3M(yOffset).add3M(ySideDelta), 
						normal, 
						new Vector4((double)(x + 0) / (double)resolution, (double)(y + 1) / (double)resolution, 0, 0));
				
				subV2 = new Vertex(
						v0.add3(xOffset).add3M(xSideDelta).add3M(yOffset).add3M(ySideDelta), 
						normal, 
						new Vector4((double)(x + 1) / (double)resolution, (double)(y + 1) / (double)resolution, 0, 0));
				
				subV2_2 = new Vertex(
						v0.add3(xOffset).add3M(xSideDelta).add3M(yOffset).add3M(ySideDelta), 
						normal, 
						new Vector4((double)(x + 1) / (double)resolution, (double)(y + 1) / (double)resolution, 0, 0));
				
				subV3 = new Vertex(
						v0.add3(xOffset).add3M(xSideDelta).add3M(yOffset), 
						normal, 
						new Vector4((double)(x + 1) / (double)resolution, (double)(y + 0) / (double)resolution, 0, 0));
				

				tris.add(new Triangle(subV0, subV1, subV2));
				tris.add(new Triangle(subV0_2, subV2_2, subV3));
			}
		}
	}
	
	
	/* *********************************************************************************************
	 * Tessellation
	 * *********************************************************************************************/
	public MeshSurface tessellate(double resolution)
	{
		ArrayList<Triangle> triangles = generateTriangles(w, h, resolution);
		MeshSurface mesh = new MeshSurface();
		mesh.setTriangles(triangles);
		return mesh;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public void setMaterial(Material material)
	{
		this.material = material;
		for(Triangle tri: triangles)
			tri.setMaterial(material);
	}
}
