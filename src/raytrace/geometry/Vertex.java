package raytrace.geometry;

import math.Vector4;

public class Vertex {

	/*
	 * A simple vertex class
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 position;
	protected Vector4 normal;
	protected Vector4 texCoord;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vertex()
	{
		position = new Vector4();
		normal = new Vector4();
		texCoord = new Vector4();
	}
	
	public Vertex(Vector4 position, Vector4 normal, Vector4 texCoord)
	{
		this.position = position;
		this.normal = normal;
		this.texCoord = texCoord;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Vector4 getPosition() {
		return position;
	}

	public void setPosition(Vector4 position) {
		this.position = position;
	}

	public void setPosition(double x, double y, double z) {
		position.set(x, y, z, position.get(3));
	}

	public Vector4 getNormal() {
		return normal;
	}

	public void setNormal(Vector4 normal) {
		this.normal = normal;
	}

	public void setNormal(double x, double y, double z) {
		normal.set(x, y, z, normal.get(3));
	}

	public Vector4 getTexCoord() {
		return texCoord;
	}

	public void setTexCoord(Vector4 texCoord) {
		this.texCoord = texCoord;
	}

	public void setTexCoord(double u, double v, double w) {
		texCoord.set(u, v, w, texCoord.get(3));
	}

	public void setTexCoord(double u, double v) {
		texCoord.set(u, v, texCoord.get(2), texCoord.get(3));
	}
	
}
