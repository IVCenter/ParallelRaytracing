package raytrace.data;

import raytrace.material.Material;
import raytrace.surfaces.AbstractSurface;
import math.Vector3;
import math.ray.Ray;

public class IntersectionData {
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//Since API v1.0
	protected double time;
	protected double distance;
	protected Vector3 point;
	protected Vector3 normal;
	protected boolean twoSided;
	protected Ray ray;
	protected Material material;
	
	//Since API v2.0
	protected AbstractSurface surface;
	protected Vector3 texcoord;
	//protected Vector4 uTangent; //Calculated in material on demand
	//protected Vector4 vTangent; //Calculated in material on demand
	protected Vector3 localPoint;
	protected int surfaceID;


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
		surfaceID = 0;
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
	public Vector3 getPoint() {
		return point;
	}

	public void setPoint(Vector3 point) {
		this.point = point;
	}

	//Normal
	public Vector3 getNormal() {
		return normal;
	}

	public void setNormal(Vector3 normal) {
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
	public AbstractSurface getSurface() {
		return surface;
	}

	public void setSurface(AbstractSurface surface) {
		this.surface = surface;
	}

	//Texcoord
	public Vector3 getTexcoord() {
		return texcoord;
	}

	public void setTexcoord(Vector3 texcoord) {
		this.texcoord = texcoord;
	}

	//Local Point
	public Vector3 getLocalPoint() {
		return localPoint;
	}

	public void setLocalPoint(Vector3 localPoint) {
		this.localPoint = localPoint;
	}

	//MeshID
	public int getSurfaceID() {
		return surfaceID;
	}

	public void setSurfaceID(int surfaceID) {
		this.surfaceID = surfaceID;
	}
}
