package raytrace.geometry.meshes;

import java.util.ArrayList;

import math.Vector3;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.material.Material;

public class Cube extends MeshSurface {
	
	/*
	 * A simple cube
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double x;
	protected double y;
	protected double z;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Cube()
	{
		//Unit cube centered at the origin
		super();
		initialize(1,1,1, new Vector3());
	}
	
	public Cube(double xLength, double yLength, double zLength)
	{
		super();
		initialize(xLength, yLength, zLength, new Vector3());
	}
	
	public Cube(double length, Vector3 offset)
	{
		super();
		initialize(length, length, length, offset);
	}
	
	public Cube(double xLength, double yLength, double zLength, Vector3 offset)
	{
		super();
		initialize(xLength, yLength, zLength, offset);
	}

	
	/* *********************************************************************************************
	 * Init Triangles
	 * *********************************************************************************************/
	public void initialize(double xLength, double yLength, double zLength, Vector3 offset)
	{
		x = xLength * 0.5;
		y = yLength * 0.5;
		z = zLength * 0.5;

		ArrayList<Triangle> tris = generateTriangles(x, y, z, offset, 1);
		this.triangles = tris;
	}
	
	protected ArrayList<Triangle> generateTriangles(double x, double y, double z, Vector3 offset, double resolution)
	{
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		//Vertices
		Vector3 p000 = offset.add(-x,-y,-z);
		Vector3 p001 = offset.add(-x,-y,z);
		Vector3 p010 = offset.add(-x,y,-z);
		Vector3 p011 = offset.add(-x,y,z);
		Vector3 p100 = offset.add(x,-y,-z);
		Vector3 p101 = offset.add(x,-y,z);
		Vector3 p110 = offset.add(x,y,-z);
		Vector3 p111 = offset.add(x,y,z);

		//TexCoords
		/*
		Vector4 t00 = new Vector4(0,0,0,0);
		Vector4 t01 = new Vector4(0,1,0,0);
		Vector4 t10 = new Vector4(1,0,0,0);
		Vector4 t11 = new Vector4(1,1,0,0);
		*/
		
		//Normals
		Vector3 XAxis = new Vector3(1,0,0);
		Vector3 negXAxis = new Vector3(-1,0,0);
		Vector3 YAxis = new Vector3(0,1,0);
		Vector3 negYAxis = new Vector3(0,-1,0);
		Vector3 ZAxis = new Vector3(0,0,1);
		Vector3 negZAxis = new Vector3(0,0,-1);

		//Right
		generateFaceTriangle(triangles, p101, p100, p110, p111, XAxis, resolution);

		//Left
		generateFaceTriangle(triangles, p000, p001, p011, p010, negXAxis, resolution);

		//Top
		generateFaceTriangle(triangles, p011, p111, p110, p010, YAxis, resolution);

		//Bottom
		generateFaceTriangle(triangles, p000, p100, p101, p001, negYAxis, resolution);

		//Front
		generateFaceTriangle(triangles, p001, p101, p111, p011, ZAxis, resolution);

		//Back
		generateFaceTriangle(triangles, p100, p000, p010, p110, negZAxis, resolution);
				
		return triangles;
	}
	
	protected void generateFaceTriangle(ArrayList<Triangle> tris, 
			Vector3 v0, Vector3 v1, Vector3 v2, Vector3 v3, Vector3 normal,
			double resolution)
	{
		Vector3 xOffset;
		Vector3 yOffset;
		Vector3 xSide = v3.subtract(v0);
		Vector3 ySide = v1.subtract(v0);
		Vector3 xSideDelta = v3.subtract(v0).multiplyM(1.0/(double)resolution);
		Vector3 ySideDelta = v1.subtract(v0).multiplyM(1.0/(double)resolution);
		
		Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;
		
		//Interpolate the 4 corners
		for(int x = 0; x < resolution; x++)
		{
			xOffset = xSide.multiply((double)x / (double)resolution);
			for(int y = 0; y < resolution; y++)
			{
				yOffset = ySide.multiply((double)y / (double)resolution);
				
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add(xOffset).addM(yOffset), 
						normal, 
						new Vector3((double)(x + 0) / (double)resolution, (double)(y + 0) / (double)resolution, 0));
				
				subV0_2 = new Vertex(
						v0.add(xOffset).addM(yOffset), 
						normal, 
						new Vector3((double)(x + 0) / (double)resolution, (double)(y + 0) / (double)resolution, 0));
				
				subV1 = new Vertex(
						v0.add(xOffset).addM(yOffset).addM(ySideDelta), 
						normal, 
						new Vector3((double)(x + 0) / (double)resolution, (double)(y + 1) / (double)resolution, 0));
				
				subV2 = new Vertex(
						v0.add(xOffset).addM(xSideDelta).addM(yOffset).addM(ySideDelta), 
						normal, 
						new Vector3((double)(x + 1) / (double)resolution, (double)(y + 1) / (double)resolution, 0));
				
				subV2_2 = new Vertex(
						v0.add(xOffset).addM(xSideDelta).addM(yOffset).addM(ySideDelta), 
						normal, 
						new Vector3((double)(x + 1) / (double)resolution, (double)(y + 1) / (double)resolution, 0));
				
				subV3 = new Vertex(
						v0.add(xOffset).addM(xSideDelta).addM(yOffset), 
						normal, 
						new Vector3((double)(x + 1) / (double)resolution, (double)(y + 0) / (double)resolution, 0));
				

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
		ArrayList<Triangle> triangles = generateTriangles(x, y, z, new Vector3(), resolution);
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
