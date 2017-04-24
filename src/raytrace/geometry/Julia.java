package raytrace.geometry;

import java.util.ArrayList;

import math.Quaternion;
import math.Vector3;
import math.ray.Ray;
import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.framework.Positionable;
import raytrace.geometry.meshes.MeshSurface;
import raytrace.surfaces.GeometrySurface;

public class Julia extends GeometrySurface {
	
	/*
	 * A simple sphere class
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Quaternion parameters;
	protected double boundingRadiusSquared = 3.0;
	protected double escapeThreshold = 1e1;
	protected double surfaceDelta = 1e-7;
	protected int maxIntersectionIterations = 20;//50;
	protected int maxNormalIterations = 10;
	protected double epsilon = 0.0001;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Julia()
	{
		parameters = new Quaternion(0, 0, 0, 0);
	}
	
	public Julia(Quaternion parameters)
	{
		this.parameters = parameters;
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
		Vector3 d = ray.getDirection();//.normalize();
		
		//Intersect here
		double t;
		Vector3 e0 = new Vector3(e);
		Quaternion z = new Quaternion();
		Quaternion zp = new Quaternion();
		double dist;
		while(true)
		{
			z.set(e0, 0);
			zp.set( 1, 0, 0, 0 );
			
			iterateIntersect(z, zp, parameters, maxIntersectionIterations);
			
			double normZ = z.magnitude();
			dist = 0.5 * normZ * Math.log( normZ ) / zp.magnitude();
			e0.addMultiRightM(d, dist);
			
			if( dist < epsilon || Double.isNaN(e0.magnitudeSqrd()))// || (++iters > 10 && e0.magnitudeSqrd() > BOUNDING_RADIUS_2) )
				break;
		}
		
		if(dist >= epsilon || Double.isNaN(e0.magnitudeSqrd()) || Double.isInfinite(e0.magnitudeSqrd()))
			return null;
		
		t = e.distance(e0) / ray.getDirection().magnitude();
		
		if(e0.subtract(e).dot(d) < 0.0)
			t *= -1.0;
		
		if(t < data.getTStart() + epsilon)
			return null;
		
		//System.out.println("INTERSECTION YOOOOO [" + t + "]");
		
		Vector3 point = e0;//.addMultiRight(d, -0.00001);
		Vector3 pointFromCenter = point.normalize();
		Vector3 normal = normEstimate(e0, parameters, maxNormalIterations);
		//normal = e0.normalize();
		//normal = new Vector3(0,1,0);
		//if(normal.dot(d) > 0)
		//	normal.multiplyM(-1);
		//normal.print();
		
		
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
		idata.setNormal(normal);
		idata.setMaterial(material);

		//System.out.println("Julia Data");
		//System.out.println("Time [" + t + "]");
		//System.out.println("Point [" + point.get(0) + ", " + point.get(1) + ", " + point.get(2) + "]");
		//System.out.println("Distance [" + (ray.getDirection().magnitude() * t) + "]");
		//System.out.println("Normal [" + normal.get(0) + ", " + normal.get(1) + ", " + normal.get(2) + "]");

		idata.setSurface(this);
		idata.setTexcoord(texcoord);
		idata.setLocalPoint(new Vector3(point));
		idata.setSurfaceID(surfaceID);
		
		return idata;
	}
	
	protected Double intersectSphere(Vector3 e, Vector3 d, Vector3 center, double radius)
	{
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
		
		//Test if t is in the given time range
		if(t < 0.00001)
			t = (negDdotEminusC + discrim) / DdotD;
		if(t <= 0.00001 || t > 100000000.0)
			return null;
		
		//Point
		return t;
	}
	
	//Modifies q and qp
	protected void iterateIntersect(Quaternion q, Quaternion qp, Quaternion c, int maxIterations)
	{
		for(int i = 0; i < maxIterations; ++i )
		{
			qp.multiplyM(q).multiplyM(2.0);
			q.squareM().addM(c);
			if(q.dot(q) > escapeThreshold)
				break;
		}
	}
	
	protected Vector3 normEstimate(Vector3 p, Quaternion c, int maxIterations)
	{
		Quaternion qP = new Quaternion(p, 0);
		
		Quaternion gx1 = qP.add(-surfaceDelta, 0, 0, 0);
		Quaternion gx2 = qP.add(surfaceDelta, 0, 0, 0);
		Quaternion gy1 = qP.add(0, -surfaceDelta, 0, 0);
		Quaternion gy2 = qP.add(0, surfaceDelta, 0, 0);
		Quaternion gz1 = qP.add(0, 0, -surfaceDelta, 0);
		Quaternion gz2 = qP.add(0, 0, surfaceDelta, 0);
		
		for(int i = 0; i < maxIterations; ++i)
		{
//			gx1.squareM().addM(c);
//			gx2.squareM().addM(c);
//			gy1.squareM().addM(c);
//			gy2.squareM().addM(c);
//			gz1.squareM().addM(c);
//			gz2.squareM().addM(c);
			gx1.multiplyM(gx1).addM(c);
			gx2.multiplyM(gx2).addM(c);
			gy1.multiplyM(gy1).addM(c);
			gy2.multiplyM(gy2).addM(c);
			gz1.multiplyM(gz1).addM(c);
			gz2.multiplyM(gz2).addM(c);
		}
		//System.out.println("GX1 [" + gx1.get(0) + ", " + gx1.get(1) + ", " + gx1.get(2) + ", " + gx1.get(3) + "]");
		
		Vector3 n = new Vector3(gx2.magnitude() - gx1.magnitude(), gy2.magnitude() - gy1.magnitude(), gz2.magnitude() - gz1.magnitude());
		n.normalizeM();
		
		//n = p.subtract(new Vector3(0, 0, 0)).normalizeM();
		
		return n;
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
		boundingBox.min.subtractM(10.0);
		boundingBox.max.addM(10.0);
		return boundingBox;
	}
	
	
	/* *********************************************************************************************
	 * Tessellation Methods
	 * *********************************************************************************************/
	public MeshSurface tessellate(int resolution)
	{
		return null;
	}
	public MeshSurface tessellate(int verticalResolution, int horizontalResolution)
	{
		return null;
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public Quaternion getParameters() {
		return parameters;
	}

	public void setParameters(Quaternion parameters) {
		this.parameters = parameters;
	}

	public double getBoundingRadiusSquared() {
		return boundingRadiusSquared;
	}

	public void setBoundingRadiusSquared(double boundingRadiusSquared) {
		this.boundingRadiusSquared = boundingRadiusSquared;
	}

	public double getEscapeThreshold() {
		return escapeThreshold;
	}

	public void setEscapeThreshold(double escapeThreshold) {
		this.escapeThreshold = escapeThreshold;
	}

	public double getSurfaceDelta() {
		return surfaceDelta;
	}

	public void setSurfaceDelta(double surfaceDelta) {
		this.surfaceDelta = surfaceDelta;
	}

	public int getMaxIntersectionIterations() {
		return maxIntersectionIterations;
	}

	public void setMaxIntersectionIterations(int maxIntersectionIterations) {
		this.maxIntersectionIterations = maxIntersectionIterations;
	}

	public int getMaxNormalIterations() {
		return maxNormalIterations;
	}

	public void setMaxNormalIterations(int maxNormalIterations) {
		this.maxNormalIterations = maxNormalIterations;
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	

}
