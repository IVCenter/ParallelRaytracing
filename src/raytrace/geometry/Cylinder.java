package raytrace.geometry;

import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.surfaces.TerminalSurface;

public class Cylinder extends TerminalSurface {
	
	/*
	 * A simple sphere class
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double height;
	protected double radius;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Cylinder()
	{
		height = 2.0;
		radius = 0.5;
	}
	
	public Cylinder(double height, double radius)
	{
		this.height = height;
		this.radius = radius;
		updateBoundingBox();
		//dynamic = false;
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data)
	{
		//TODO:  These equations do not test if the ray is exactly looking down the center of the cylinder
		//			In the (extremely rare) case that is does, the cylinder will not be visible.
		
		int capIntersection = 0;
		
		Ray ray = data.getRay();
		
		double[] e = ray.getOrigin().getArray();
		double[] d = ray.getDirection().getArray();
		
		//
		double a = (d[0] * d[0]) + (d[2] * d[2]);
		double b = (2.0 * e[0] * d[0]) + (2.0 * e[2] * d[2]);
		double c = (e[0] * e[0]) + (e[2] * e[2]) - 1.0;
		
		
		double discrim = (b * b) - (4.0 * a * c);
		
		//If the discriminant is negative then the ray doesn't intersect in real space
		if(discrim < 0.0) {
			return null;
		}
		
		//Now that we know its >= 0, root it
		discrim = Math.sqrt(discrim);
		
		//Get the time of intersection
		double t = ((-1.0 * b) - discrim) / (2.0 * a);
		
		double t0 = data.getTStart();
		double t1 = data.getTEnd();
		

		//Test if the intersection is within the height range
		double ymin = 0;//p[1];
		double ymax = height;//p[1] + height;
		double yinter = e[1] + d[1] * t;
		
		//Test if t is in the given time range
		if(t < t0 || yinter < ymin || yinter > ymax)
			t = ((-1.0 * b) + discrim) / (2.0 * a);
		if(t <= t0 || t > t1)
			return null;
		
		//Test if the intersection is within the height range
		yinter = e[1] + d[1] * t;
		
		if(yinter < ymin || yinter > ymax)
			return null;
		
		
		
		
		//Test if it intersects the caps
		
		//If D is orthogonal to N, then it is parallel to the plane and will never intersect the caps
		//So don't calculate anything further
		
		CapIntersectionTest:
		if(d[1] != 0.0 && !Double.isInfinite(height))
		{
			double tcapmin = (-e[1]) / d[1];
			double tcapmax = (height - e[1]) / d[1];
			
			//if(t <= tcapmin && t <= tcapmax)
			//	break CapIntersectionTest;
			
			//if(tcapmin < 0 && tcapmax > 0 && t <= tcapmax)
			//	break CapIntersectionTest;
			
			//if(tcapmax < 0 && tcapmin > 0 && t <= tcapmin)
			//	break CapIntersectionTest;
			
			double tcminx = e[0] + d[0] * tcapmin;
			double tcminz = e[2] + d[2] * tcapmin;
			
			double radius2 = radius * radius;
			
			if(tcapmin > 0 && tcapmin < t && tcminx * tcminx + tcminz * tcminz <= radius2)
			{
				t = tcapmin;
				capIntersection = -1;
				break CapIntersectionTest;
			}
			
			double tcmaxx = e[0] + d[0] * tcapmax;
			double tcmaxz = e[2] + d[2] * tcapmax;
			

			if(tcapmax > 0 && tcapmax < t && tcmaxx * tcmaxx + tcmaxz * tcmaxz <= radius2)
			{
				t = tcapmax;
				capIntersection = 1;
				break CapIntersectionTest;
			}
		}
		
		
		
		//Evaluate point of intersection
		Vector4 point = ray.evaluateAtTime(t);
		
		//Normal
		Vector4 normal;
		
		//Texcoord
		Vector4 texcoord;
		
		//Allocate a new idata
		IntersectionData idata = new IntersectionData();
		
		//Configure idata for either cap or cylinder body intersection
		if(capIntersection != 0)
		{
			//Normal
			normal = new Vector4(0, capIntersection, 0, 0);
			
			//Texcoord
			texcoord = new Vector4(point.get(0), point.get(2), 0, 0);
			
		}else{
			
			//Normal
			normal = new Vector4(point);
			normal.set(1, 0);
			normal.normalize3M();
			
			//Calculate Texcoords
			double[] pcfM = normal.getArray();
			
			double uCoord = (Math.atan2(pcfM[2], pcfM[0]) + Math.PI) / (2.0 * Math.PI);
			double vCoord;
			
			//If the height is infinite then the texcoord is in reference to the unit height cylinder
			if(Double.isInfinite(height))
			{
				vCoord = yinter;
			}else{
				vCoord = yinter / (ymax-ymin);
			}
			
			texcoord = new Vector4(uCoord, vCoord, 0, 0);
		}
		

		//Return data about the intersection
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(normal);
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
		boundingBox.min.set(-radius, 0, -radius, 0);
		boundingBox.max.set(radius, height, radius, 0);
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
	
}
