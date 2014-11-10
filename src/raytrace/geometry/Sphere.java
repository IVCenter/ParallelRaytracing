package raytrace.geometry;

import java.util.ArrayList;

import math.Vector3;
import math.ray.Ray;
import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.surfaces.GeometrySurface;

public class Sphere extends GeometrySurface implements Positionable {
	
	/*
	 * A simple sphere class
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double radius;
	protected Vector3 center;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Sphere()
	{
		radius = 1.0;
		center = new Vector3(0, 0, 0);
	}
	
	public Sphere(double radius, Vector3 center)
	{
		this.radius = radius;
		this.center = center;
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data)
	{
		Ray ray = data.getRay();
		
		Vector3 e = ray.getOrigin();
		Vector3 d = ray.getDirection();
		
		//Precalc frequently used values/vectors
		Vector3 EminusC = e.subtract(center);
		double DdotD = d.dot(d);
		double DdotEminusC = d.dot(EminusC);
		
		double discrim = (DdotEminusC * DdotEminusC) - DdotD * (EminusC.magnitudeSqrd() - (radius * radius));
		
		//If the discriminant is negative then the ray doesn't intersect in real space
		if(discrim < 0.0) {
			return null;
		}
		
		//Now that we know its >= 0, root it
		discrim = Math.sqrt(discrim);
		
		//Get the negation of d
		Vector3 negD = d.multiply(-1);
		double negDdotEminusC = negD.dot(EminusC);
		
		//Get the time of intersection
		double t = (negDdotEminusC - discrim) / DdotD;
		
		double t0 = data.getTStart();
		double t1 = data.getTEnd();
		
		//Test if t is in the given time range
		if(t < t0)
			t = (negDdotEminusC + discrim) / DdotD;
		if(t <= t0 || t > t1)
			return null;
		
		
		//Point
		Vector3 point = ray.evaluateAtTime(t);
		Vector3 pointFromCenter = point.subtract(center).normalizeM();
		
		
		//Calculate Texcoords
		double[] pcfM = pointFromCenter.getArray();
		
		double uCoord = (Math.atan2(pcfM[2], pcfM[0]) + Math.PI) / (2.0 * Math.PI);
		double vCoord = Math.acos(pcfM[1]) / Math.PI;
		Vector3 texcoord = new Vector3(uCoord, vCoord, 0);
		
		
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude() * t);
		idata.setNormal(pointFromCenter);
		idata.setMaterial(material);

		idata.setSurface(this);
		idata.setTexcoord(texcoord);
		idata.setLocalPoint(new Vector3(point));
		idata.setSurfaceID(surfaceID);
		
		return idata;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
	}
	
	@Override
	public BoundingBox getBoundingBox()
	{
		BoundingBox boundingBox = new BoundingBox();
		boundingBox.min.set(center.subtract(radius));
		boundingBox.max.set(center.add(radius));
		return boundingBox;
	}
	
	
	/* *********************************************************************************************
	 * Tessellation Methods
	 * *********************************************************************************************/
	public MeshSurface tessellate(int resolution)
	{
		return tessellate(resolution, 2 * resolution);
	}
	public MeshSurface tessellate(int verticalResolution, int horizontalResolution)
	{
		//Triangles
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		double phi = 0.0;
		double phiDelta = (2.0 * Math.PI) * (1.0 / (double)(horizontalResolution));
		double theta = 0.0;
		double thetaDelta = (Math.PI) * (1.0 / (double)verticalResolution);

		Vector3 v0 = new Vector3();
		Vector3 v1 = new Vector3();
		Vector3 v2 = new Vector3();
		Vector3 v3 = new Vector3();

		Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;

		//Polar Phi
		for(int x = 0; x < horizontalResolution; ++x)
		{
			phi = (2.0 * Math.PI) * ((double)x / (double)(horizontalResolution));
			
			//Vertical Theta
			for(int y = 0; y < verticalResolution; ++y)
			{
				theta = (Math.PI) * ((double)y / (double)verticalResolution);
				
				v0.set(radius * Math.sin(theta) * Math.cos(phi), 
					   radius * Math.cos(theta),
					   radius * Math.sin(theta) * Math.sin(phi));
				v1.set(radius * Math.sin(theta+thetaDelta) * Math.cos(phi), 
					   radius * Math.cos(theta+thetaDelta),
					   radius * Math.sin(theta+thetaDelta) * Math.sin(phi));
				v2.set(radius * Math.sin(theta+thetaDelta) * Math.cos(phi+phiDelta), 
					   radius * Math.cos(theta+thetaDelta),
					   radius * Math.sin(theta+thetaDelta) * Math.sin(phi+phiDelta));
				v3.set(radius * Math.sin(theta) * Math.cos(phi+phiDelta), 
					   radius * Math.cos(theta),
					   radius * Math.sin(theta) * Math.sin(phi+phiDelta));
				
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add(center), 
						v0.add(0.0).normalizeM(), 
						new Vector3((double)(x + 0) / (double)(horizontalResolution), (double)(y + 0) / (double)verticalResolution, 0));
				
				subV0_2 = new Vertex(
						v0.add(center), 
						v0.add(0.0).normalizeM(), 
						new Vector3((double)(x + 0) / (double)(horizontalResolution), (double)(y + 0) / (double)verticalResolution, 0));
				
				subV1 = new Vertex(
						v1.add(center), 
						v1.add(0.0).normalizeM(), 
						new Vector3((double)(x + 0) / (double)(horizontalResolution), (double)(y + 1) / (double)verticalResolution, 0));
				
				subV2 = new Vertex(
						v2.add(center), 
						v2.add(0.0).normalizeM(), 
						new Vector3((double)(x + 1) / (double)(horizontalResolution), (double)(y + 1) / (double)verticalResolution, 0));
				
				subV2_2 = new Vertex(
						v2.add(center), 
						v2.add(0.0).normalizeM(),
						new Vector3((double)(x + 1) / (double)(horizontalResolution), (double)(y + 1) / (double)verticalResolution, 0));
				
				subV3 = new Vertex(
						v3.add(center), 
						v3.add(0.0).normalizeM(), 
						new Vector3((double)(x + 1) / (double)(horizontalResolution), (double)(y + 0) / (double)verticalResolution, 0));
				
				if(y != verticalResolution-1)
					triangles.add(new Triangle(subV0, subV1, subV2));
				if(y != 0)
					triangles.add(new Triangle(subV0_2, subV2_2, subV3));
			}
		}
		
		MeshSurface mesh = new MeshSurface();
		mesh.setTriangles(triangles);
		
		return mesh;
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	public Vector3 getPosition()
	{
		return center;
	}

	@Override
	public void setPosition(Vector3 position)
	{
		center = position;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	

}
