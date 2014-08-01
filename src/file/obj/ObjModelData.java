package file.obj;

import java.util.ArrayList;

import process.logging.Logger;

import math.Vector3;

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
		if(currentObject == null) {
			currentObject = new Object("AnonymousObject");
			objects.add(currentObject);
		}
	}

	private void prepareCurrentFace()
	{
		prepareCurrentObject();
		
		if(currentFace == null) {
			currentFace = new Face();
			currentObject.addFace(currentFace);
		}
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
			vertices.add(new Vertex(0,0,0));
			
			normals = new ArrayList<ObjModelData.Normal>();
			normals.add(new Normal(0,1,0));
			
			texCoords = new ArrayList<ObjModelData.TexCoord>();
			texCoords.add(new TexCoord(0,0,0));
			
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
		
		public Vector3 getVertex(int index)
		{
			if(index < 0)
				index = -1 * index;
			
			if(index >= vertices.size())
				index = index % vertices.size();
			
			if(index == 0)
				Logger.warning(-14, "ObjModelData: Attempting to access default vertex at index 0.");
			
			return vertices.get(index);
		}

		public ArrayList<Normal> getNormals() {
			return normals;
		}
		
		public Vector3 getNormal(int index)
		{
			if(index < 0)
				index = -1 * index;
		
			if(index >= normals.size())
				index = index % normals.size();
			
			if(index == 0)
				Logger.warning(-14, "ObjModelData: Attempting to access default normal at index 0.");
			
			return normals.get(index);
		}

		public ArrayList<TexCoord> getTexCoords() {
			return texCoords;
		}
		
		public Vector3 getTexCoord(int index)
		{
			if(index >= texCoords.size())
				index = index % texCoords.size();
			
			return texCoords.get(index);
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
	
	public static class Vertex extends Vector3{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Vertex(double x, double y, double z) { super(x, y, z); }
	}
	
	public static class Normal extends Vector3 {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Normal(double x, double y, double z) { super(x, y, z); super.normalizeM(); }
	}
	
	public static class TexCoord extends Vector3 {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TexCoord(double x, double y, double z) { super(x, y, z); }
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
			if(currentVert >= verts.length) {
				Logger.warning(-14, "ObjModelData.Face.addVertex(): Over-packing of a face detected!");
				return;
			}
			
			verts[currentVert][0] = v;
			verts[currentVert][1] = vt;
			verts[currentVert][2] = vn;
			
			currentVert++;
		}
		
		public int[][] getVertices()
		{
			return verts;
		}
	}
}
