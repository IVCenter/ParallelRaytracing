package raytrace.geometry.meshes;

import math.Vector4;
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
	//
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Cube()
	{
		//Unit cube centered at the origin
		super();
		initialize(1,1,1);
	}
	
	public Cube(double xLength, double yLength, double zLength)
	{
		super();
		initialize(xLength, yLength, zLength);
	}

	
	/* *********************************************************************************************
	 * Init Triangles
	 * *********************************************************************************************/
	public void initialize(double xLength, double yLength, double zLength)
	{
		double x = xLength * 0.5;
		double y = yLength * 0.5;
		double z = zLength * 0.5;

		//Vertices
		Vector4 p000 = new Vector4(-x,-y,-z,0);
		Vector4 p001 = new Vector4(-x,-y,z,0);
		Vector4 p010 = new Vector4(-x,y,-z,0);
		Vector4 p011 = new Vector4(-x,y,z,0);
		Vector4 p100 = new Vector4(x,-y,-z,0);
		Vector4 p101 = new Vector4(x,-y,z,0);
		Vector4 p110 = new Vector4(x,y,-z,0);
		Vector4 p111 = new Vector4(x,y,z,0);

		//TexCoords
		Vector4 t00 = new Vector4(0,0,0,0);
		Vector4 t01 = new Vector4(0,1,0,0);
		Vector4 t10 = new Vector4(1,0,0,0);
		Vector4 t11 = new Vector4(1,1,0,0);
		
		//Normals
		Vector4 XAxis = new Vector4(1,0,0,0);
		Vector4 negXAxis = new Vector4(-1,0,0,0);
		Vector4 YAxis = new Vector4(0,1,0,0);
		Vector4 negYAxis = new Vector4(0,-1,0,0);
		Vector4 ZAxis = new Vector4(0,0,1,0);
		Vector4 negZAxis = new Vector4(0,0,-1,0);

		//Right
		{
			Vertex v0 = new Vertex(p101, XAxis, t00);
			Vertex v1 = new Vertex(p100, XAxis, t10);
			Vertex v2 = new Vertex(p110, XAxis, t11);
			Vertex v3 = new Vertex(p111, XAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}

		//Left
		{
			Vertex v0 = new Vertex(p000, negXAxis, t00);
			Vertex v1 = new Vertex(p001, negXAxis, t10);
			Vertex v2 = new Vertex(p011, negXAxis, t11);
			Vertex v3 = new Vertex(p010, negXAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}

		//Top
		{
			Vertex v0 = new Vertex(p011, YAxis, t00);
			Vertex v1 = new Vertex(p111, YAxis, t10);
			Vertex v2 = new Vertex(p110, YAxis, t11);
			Vertex v3 = new Vertex(p010, YAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}

		//Bottom
		{
			Vertex v0 = new Vertex(p000, negYAxis, t00);
			Vertex v1 = new Vertex(p100, negYAxis, t10);
			Vertex v2 = new Vertex(p101, negYAxis, t11);
			Vertex v3 = new Vertex(p001, negYAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}

		//Front
		{
			Vertex v0 = new Vertex(p001, ZAxis, t00);
			Vertex v1 = new Vertex(p101, ZAxis, t10);
			Vertex v2 = new Vertex(p111, ZAxis, t11);
			Vertex v3 = new Vertex(p011, ZAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}

		//Back
		{
			Vertex v0 = new Vertex(p100, negZAxis, t00);
			Vertex v1 = new Vertex(p000, negZAxis, t10);
			Vertex v2 = new Vertex(p010, negZAxis, t11);
			Vertex v3 = new Vertex(p110, negZAxis, t01);
			triangles.add(new Triangle(v0, v1, v2));
			triangles.add(new Triangle(v0, v2, v3));
		}
		
		//for(Triangle tri : triangles)
		//	tri.generateFaceNormal();
		

		updateBoundingBox();
		dynamic = false;
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
