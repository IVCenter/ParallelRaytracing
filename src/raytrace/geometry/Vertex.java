package raytrace.geometry;

import math.Vector3;

public class Vertex {

	/*
	 * A simple vertex class
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 position;
	protected Vector3 normal;
	protected Vector3 texCoord;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vertex()
	{
		position = new Vector3();
		normal = new Vector3();
		texCoord = new Vector3();
	}
	
	public Vertex(Vector3 position, Vector3 normal, Vector3 texCoord)
	{
		this.position = position;
		this.normal = normal;
		this.texCoord = texCoord;
	}
	
	public Vertex copy()
	{
		Vertex v = new Vertex(position.add(0), normal.add(0), texCoord.add(0));
		return v;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public void setPosition(double x, double y, double z) {
		position.set(x, y, z);
	}

	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
		this.normal = normal;
	}

	public void setNormal(double x, double y, double z) {
		normal.set(x, y, z);
	}

	public Vector3 getTexCoord() {
		return texCoord;
	}

	public void setTexCoord(Vector3 texCoord) {
		this.texCoord = texCoord;
	}

	public void setTexCoord(double u, double v, double w) {
		texCoord.set(u, v, w);
	}

	public void setTexCoord(double u, double v) {
		texCoord.set(u, v, texCoord.get(2));
	}
	
}
