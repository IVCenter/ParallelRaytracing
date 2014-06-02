package raytrace.geometry;

import java.util.ArrayList;

import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.surfaces.TerminalSurface;

public class Sphere extends TerminalSurface implements Positionable {
	
	/*
	 * A simple sphere class
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double radius;
	protected Vector4 center;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Sphere()
	{
		radius = 1.0;
		center = new Vector4(0,0,0,1);
	}
	
	public Sphere(double radius, Vector4 center)
	{
		this.radius = radius;
		this.center = center;
		updateBoundingBox();
		dynamic = false;
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
		
		Vector4 e = ray.getOrigin();
		Vector4 d = ray.getDirection();
		
		//Precalc frequently used values/vectors
		Vector4 EminusC = e.subtract3(center);
		double DdotD = d.dot3(d);
		double DdotEminusC = d.dot3(EminusC);
		
		double discrim = (DdotEminusC * DdotEminusC) - DdotD * (EminusC.magnitude3Sqrd() - (radius * radius));
		
		//If the discriminant is negative then the ray doesn't intersect in real space
		if(discrim < 0.0) {
			return null;
		}
		
		//Now that we know its >= 0, root it
		discrim = Math.sqrt(discrim);
		
		//Get the negation of d
		Vector4 negD = d.multiply3(-1);
		double negDdotEminusC = negD.dot3(EminusC);
		
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
		Vector4 point = ray.evaluateAtTime(t);
		Vector4 pointFromCenter = point.subtract3(center).normalize3();
		
		
		//Calculate Texcoords
		double[] pcfM = pointFromCenter.getArray();
		
		double uCoord = (Math.atan2(pcfM[2], pcfM[0]) + Math.PI) / (2.0 * Math.PI);
		double vCoord = Math.acos(pcfM[1]) / Math.PI;
		Vector4 texcoord = new Vector4(uCoord, vCoord, 0, 0);
		
		
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(pointFromCenter);
		idata.setMaterial(material);

		idata.setSurface(this);
		idata.setTexcoord(texcoord);
		idata.setLocalPoint(new Vector4(point));
		
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
	/**
	 * 
	 */
	public void updateBoundingBox()
	{
		boundingBox.clear();
		boundingBox.min.set(center.subtract3(radius));
		boundingBox.max.set(center.add3(radius));
	}
	
	
	/* *********************************************************************************************
	 * Tessellation Methods
	 * *********************************************************************************************/
	public MeshSurface tessellate(int resolution)
	{
		//Triangles
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		
		double phi = 0.0;
		double phiDelta = (2.0 * Math.PI) * (1.0 / (double)(resolution*2));
		double theta = 0.0;
		double thetaDelta = (Math.PI) * (1.0 / (double)resolution);

		Vector4 v0 = new Vector4();
		Vector4 v1 = new Vector4();
		Vector4 v2 = new Vector4();
		Vector4 v3 = new Vector4();

		Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;

		//Polar Phi
		for(int x = 0; x < resolution*2; ++x)
		{
			phi = (2.0 * Math.PI) * ((double)x / (double)(resolution*2));
			
			//Vertical Theta
			for(int y = 0; y < resolution; ++y)
			{
				theta = (Math.PI) * ((double)y / (double)resolution);
				
				v0.set(radius * Math.sin(theta) * Math.cos(phi), 
					   radius * Math.cos(theta),
					   radius * Math.sin(theta) * Math.sin(phi),  0);
				v1.set(radius * Math.sin(theta+thetaDelta) * Math.cos(phi), 
					   radius * Math.cos(theta+thetaDelta),
					   radius * Math.sin(theta+thetaDelta) * Math.sin(phi),  0);
				v2.set(radius * Math.sin(theta+thetaDelta) * Math.cos(phi+phiDelta), 
					   radius * Math.cos(theta+thetaDelta),
					   radius * Math.sin(theta+thetaDelta) * Math.sin(phi+phiDelta),  0);
				v3.set(radius * Math.sin(theta) * Math.cos(phi+phiDelta), 
					   radius * Math.cos(theta),
					   radius * Math.sin(theta) * Math.sin(phi+phiDelta),  0);
				
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add3(center), 
						v0.add3(0.0).normalize3(), 
						new Vector4((double)(x + 0) / (double)(resolution*2), (double)(y + 0) / (double)resolution, 0, 0));
				
				subV0_2 = new Vertex(
						v0.add3(center), 
						v0.add3(0.0).normalize3(), 
						new Vector4((double)(x + 0) / (double)(resolution*2), (double)(y + 0) / (double)resolution, 0, 0));
				
				subV1 = new Vertex(
						v1.add3(center), 
						v1.add3(0.0).normalize3(), 
						new Vector4((double)(x + 0) / (double)(resolution*2), (double)(y + 1) / (double)resolution, 0, 0));
				
				subV2 = new Vertex(
						v2.add3(center), 
						v2.add3(0.0).normalize3(), 
						new Vector4((double)(x + 1) / (double)(resolution*2), (double)(y + 1) / (double)resolution, 0, 0));
				
				subV2_2 = new Vertex(
						v2.add3(center), 
						v2.add3(0.0).normalize3(),
						new Vector4((double)(x + 1) / (double)(resolution*2), (double)(y + 1) / (double)resolution, 0, 0));
				
				subV3 = new Vertex(
						v3.add3(center), 
						v3.add3(0.0).normalize3(), 
						new Vector4((double)(x + 1) / (double)(resolution*2), (double)(y + 0) / (double)resolution, 0, 0));
				
				if(y != resolution-1)
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
	public Vector4 getPosition()
	{
		return center;
	}

	@Override
	public void setPosition(Vector4 position)
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
