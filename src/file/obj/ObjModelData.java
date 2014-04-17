package file.obj;

import java.util.ArrayList;

import math.Vector4;

public class ObjModelData {
	
	/*
	 * A storage object for data loaded from an .obj file
	 * Note: This is a stateful object under mutation.  
	 * 		The add() methods will change behavior based on the internal state
	 * In contrast,
	 * 		The get() methods will always return deterministically
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Object> objects;
	protected Object currentObject = null;
	protected Face currentFace = null;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ObjModelData()
	{
		objects = new ArrayList<Object>();
	}


	/* *********************************************************************************************
	 * Getters
	 * *********************************************************************************************/
	public ArrayList<Object> getObjects() {
		return objects;
	}
	

	/* *********************************************************************************************
	 * Adders
	 * *********************************************************************************************/
	public void addObject(String id)
	{
		if(id == null)
			id = "null";
		
		Object obj = new Object(id);
		objects.add(obj);
		currentObject = obj;
	}
	
	public void addVertex(double x, double y, double z)
	{
		prepareCurrentObject();
		
		currentObject.addVertex(new Vertex(x, y, z));
	}
	
	public void addNormal(double x, double y, double z)
	{
		prepareCurrentObject();
		
		currentObject.addNormal(new Normal(x, y, z));
	}
	
	public void addTexCoord(double x, double y, double z)
	{
		prepareCurrentObject();
		
		currentObject.addTexCoord(new TexCoord(x, y, z));
	}

	public void addFace()
	{
		prepareCurrentObject();
		
		Face face = new Face();
		currentObject.addFace(face);
		currentFace = face;
	}
	
	public void addVertexToFace(int v, int vt, int vn)
	{
		prepareCurrentFace();
		
		currentFace.addVertex(v, vt, vn);
	}
	
	
	/* *********************************************************************************************
	 * Private Helper Methods
	 * *********************************************************************************************/
	private void prepareCurrentObject()
	{
		if(currentObject == null)
			currentObject = new Object("AnonymousObject");
	}

	private void prepareCurrentFace()
	{
		if(currentFace == null)
			currentFace = new Face();
	}


	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	public static class Object
	{
		/*
		 * An object specified by an "o" line in an .obj file
		 */
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		protected String id;
		
		protected ArrayList<Vertex> vertices;
		protected ArrayList<Normal> normals;
		protected ArrayList<TexCoord> texCoords;
		protected ArrayList<Face> faces;

		
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public Object(String id)
		{
			this.id = id;
			
			vertices = new ArrayList<ObjModelData.Vertex>();
			normals = new ArrayList<ObjModelData.Normal>();
			texCoords = new ArrayList<ObjModelData.TexCoord>();
			faces = new ArrayList<ObjModelData.Face>();
		}
		

		/* *********************************************************************************************
		 * Getters
		 * *********************************************************************************************/
		public String getID() {
			return id;
		}

		public ArrayList<Vertex> getVertices() {
			return vertices;
		}

		public ArrayList<Normal> getNormals() {
			return normals;
		}

		public ArrayList<TexCoord> getTexCoords() {
			return texCoords;
		}

		public ArrayList<Face> getFaces() {
			return faces;
		}
		

		/* *********************************************************************************************
		 * Adders
		 * *********************************************************************************************/
		public void addVertex(Vertex v)
		{
			if(v == null)
				return;
			
			vertices.add(v);
		}
		
		public void addNormal(Normal n)
		{
			if(n == null)
				return;
			
			normals.add(n);
		}
		
		public void addTexCoord(TexCoord t)
		{
			if(t == null)
				return;
			
			texCoords.add(t);
		}
		
		public void addFace(Face f)
		{
			if(f == null)
				return;
			
			faces.add(f);
		}
	}
	
	public static class Vertex extends Vector4{
		public Vertex(double x, double y, double z) { super(x, y, z, 0); }
	}
	
	public static class Normal extends Vector4 {
		public Normal(double x, double y, double z) { super(x, y, z, 0); }
	}
	
	public static class TexCoord extends Vector4 {
		public TexCoord(double x, double y, double z) { super(x, y, z, 0); }
	}
	
	public static class Face
	{
		/*
		 * A face as specified by an "f" line in an .obj file
		 */
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		protected int[][] verts;
		protected int currentVert = 0;
		
		public Face()
		{
			verts = new int[3][3];
		}
		
		public void addVertex(int v, int vt, int vn)
		{
			if(currentVert >= verts.length)
				return;
			
			verts[currentVert][0] = v;
			verts[currentVert][1] = vt;
			verts[currentVert][2] = vn;
			
			currentVert++;
		}
	}
}
