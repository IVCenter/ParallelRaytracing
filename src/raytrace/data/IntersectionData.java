package raytrace.data;

import raytrace.material.Material;
import raytrace.surfaces.CompositeSurface;
import math.Ray;
import math.Vector4;

public class IntersectionData {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//Since API v1.0
	protected double time;
	protected double distance;
	protected Vector4 point;
	protected Vector4 normal;
	protected boolean twoSided;
	protected Ray ray;
	protected Material material;
	
	//Since API v2.0
	protected CompositeSurface surface;
	protected Vector4 texcoord;
	//protected Vector4 uTangent; //Calculated in material on demand
	//protected Vector4 vTangent; //Calculated in material on demand
	protected Vector4 localPoint;
	protected int meshID;


	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public IntersectionData()
	{
		time = Double.NEGATIVE_INFINITY;
		distance = Double.POSITIVE_INFINITY;
		point = null;
		normal = null;
		twoSided = false;
		ray = null;
		material = null;
		
		surface = null;
		texcoord = null;
		localPoint = null;
		meshID = -1;
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//Time
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	//Distance
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	//Point
	public Vector4 getPoint() {
		return point;
	}

	public void setPoint(Vector4 point) {
		this.point = point;
	}

	//Normal
	public Vector4 getNormal() {
		return normal;
	}

	public void setNormal(Vector4 normal) {
		this.normal = normal;
	}

	//Ray
	public Ray getRay() {
		return ray;
	}

	public void setRay(Ray ray) {
		this.ray = ray;
	}

	//Two Sided
	public boolean isTwoSided() {
		return twoSided;
	}

	public void setTwoSided(boolean twoSided) {
		this.twoSided = twoSided;
	}
	
	//Material
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	//Surface
	public CompositeSurface getSurface() {
		return surface;
	}

	public void setSurface(CompositeSurface surface) {
		this.surface = surface;
	}

	//Texcoord
	public Vector4 getTexcoord() {
		return texcoord;
	}

	public void setTexcoord(Vector4 texcoord) {
		this.texcoord = texcoord;
	}

	//Local Point
	public Vector4 getLocalPoint() {
		return localPoint;
	}

	public void setLocalPoint(Vector4 localPoint) {
		this.localPoint = localPoint;
	}

	//MeshID
	public int getMeshID() {
		return meshID;
	}

	public void setMeshID(int meshID) {
		this.meshID = meshID;
	}
}
