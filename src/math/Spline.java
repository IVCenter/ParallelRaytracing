package math;

import java.util.ArrayList;

import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;

public class Spline extends ArrayList<Vector3> {
	
	/*
	 * A spline implementation
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static final long serialVersionUID = 1L;
	protected static double approxTangentDelta = 0.000001;
	protected static ArrayList<Long> factorials;

	
	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		//Set up the first 100 factorials
		factorials = new ArrayList<Long>(101);
		factorials.add(1L);
		for(long i = 1; i < 100; ++i)
		{
			factorials.add(factorials.get((int)i-1) * i);
		}
	}
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public Spline()
	{
		super();
	}
	
	public Spline(ArrayList<Vector3> points)
	{
		super(points);
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public ArrayList<Vector3> points(int resolution)
	{
	    ArrayList<Vector3> points = new ArrayList<Vector3>();
	    double interval = (1.0 / (double)resolution);
	    for(double t = 0.0; t <= 1.0; t += interval)
	    {
	        points.add(point(t));
	    }
	    
	    return points;
	}
	
	public Vector3 point(double t)
	{
	    double[] coords = new double[3];
	    coords[0] = 0.0;
	    coords[1] = 0.0;
	    coords[2] = 0.0;
	    
	    t = Math.min(1.0, Math.max(0.0, t));
	    
	    long n = this.size()-1;
	    long i = 0;
	    for(Vector3 point : this)
	    {
	        coords[0] += bezier(n, i, t, point.get(0));
	        coords[1] += bezier(n, i, t, point.get(1));
	        coords[2] += bezier(n, i, t, point.get(2));
	        i++;
	    }
	    
	    return new Vector3(coords);
	}
	
	public Vector3 tangent(double t)
	{
	    t = Math.min(1.0 - approxTangentDelta, Math.max(0.0, t));
	    
	    Vector3 pre = point(t);
	    Vector3 post = point(t+approxTangentDelta);
	    
	    post.subtractM(pre);
	    
	    return post.normalizeM();
	}
	
	public ArrayList<Triangle> tessellate(int segments, int slices, double radiusStart, double radiusEnd)
	{
		ArrayList<Triangle> triangles = new ArrayList<Triangle>(segments * slices * 2 + 1);
		
		double segmentDelta = 1.0 / (double)segments;
		
		Vector3 thisPoint;
		Vector3 nextPoint = this.get(0);
		
		//Vector4 thisNormal;
		Vector3 nextNormal = tangent(0);
		
		double thisRadius;
		double nextRadius = radiusStart;
		
		//This Basis
		Vector3 thisUTangent;
		Vector3 thisVTangent;

		//Next Basis
		Vector3 nextUTangent;
		Vector3 nextVTangent;
		
		if(Math.abs(nextNormal.dot(Vector3.positiveYAxis)) == 1.0)
			nextUTangent = nextNormal.cross(Vector3.positiveXAxis).normalizeM();
		else
			nextUTangent = nextNormal.cross(Vector3.positiveYAxis).normalizeM();
		nextVTangent = nextUTangent.cross(nextNormal).normalizeM();
		
		
		for(double t = 0.0; t < 1.0; t += segmentDelta)
		{	
			thisPoint = nextPoint;
			nextPoint = point(t + segmentDelta);
			
			//thisNormal = nextNormal;
			nextNormal = tangent(t + segmentDelta);
			
			thisRadius = nextRadius;
			nextRadius = (t + segmentDelta) * radiusEnd + (1.0 - (t + segmentDelta)) * radiusStart;
			
			thisUTangent = nextUTangent;
			thisVTangent = nextVTangent;
			
			if(Math.abs(nextNormal.dot(Vector3.positiveYAxis)) == 1.0)
				nextUTangent = nextNormal.cross(Vector3.positiveXAxis).normalizeM();
			else
				nextUTangent = nextNormal.cross(Vector3.positiveYAxis).normalizeM();
			
			if(nextUTangent.dot(thisUTangent) < 0.0)
				nextUTangent.multiplyM(-1.0);
			
			nextVTangent = nextUTangent.cross(nextNormal).normalizeM();
			
			
			/*
			 * TODO:
			 * 
			 * 		Radially sample both tangent disks n slices
			 * 		Generate triangles between sample points
			 */
			double angleDelta = (Math.PI * 2.0) / (double) slices;
			Vector3 v0, v1, v2, v3;
			Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;
			Triangle tri1, tri2;
			
			for(double theta = 0; theta < (Math.PI * 2.0); theta += angleDelta)
			{
				//Get the four points
				v1 = thisUTangent.multiply(Math.cos(theta) * thisRadius).addM(thisVTangent.multiply(Math.sin(theta) * thisRadius));
				v0 = nextUTangent.multiply(Math.cos(theta) * nextRadius).addM(nextVTangent.multiply(Math.sin(theta) * nextRadius));
				v2 = thisUTangent.multiply(Math.cos(theta + angleDelta) * thisRadius).addM(thisVTangent.multiply(Math.sin(theta + angleDelta) * thisRadius));
				v3 = nextUTangent.multiply(Math.cos(theta + angleDelta) * nextRadius).addM(nextVTangent.multiply(Math.sin(theta + angleDelta) * nextRadius));
				
				//Make vertices
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add(nextPoint), 
						v0.add(0.0).normalizeM(), 
						new Vector3(theta / (Math.PI*2.0), t + segmentDelta, 0));
				
				subV0_2 = new Vertex(
						v0.add(nextPoint), 
						v0.add(0.0).normalizeM(), 
						new Vector3(theta / (Math.PI*2.0), t + segmentDelta, 0));
				
				subV1 = new Vertex(
						v1.add(thisPoint), 
						v1.add(0.0).normalizeM(), 
						new Vector3(theta / (Math.PI*2.0), t, 0));
				
				subV2 = new Vertex(
						v2.add(thisPoint), 
						v2.add(0.0).normalizeM(), 
						new Vector3((theta + angleDelta) / (Math.PI*2.0), t, 0));
				
				subV2_2 = new Vertex(
						v2.add(thisPoint), 
						v2.add(0.0).normalizeM(),
						new Vector3((theta + angleDelta) / (Math.PI*2.0), t, 0));
				
				subV3 = new Vertex(
						v3.add(nextPoint), 
						v3.add(0.0).normalizeM(), 
						new Vector3((theta + angleDelta) / (Math.PI*2.0), t + segmentDelta, 0));
				
				tri1 = new Triangle(subV0, subV1, subV2);
				tri2 = new Triangle(subV0_2, subV2_2, subV3);
				tri1.generateFaceNormal();
				tri2.generateFaceNormal();
				triangles.add(tri1);
				triangles.add(tri2);
			}
			
		}
		
		return triangles;
	}

	
	/* *********************************************************************************************
	 * Helper Math Methods
	 * *********************************************************************************************/
	protected double bezier(long n, long i, double t, double control)
	{
	    return combo(n, i) * Math.pow(1.0-t, n-i) * Math.pow(t, i) * control;
	}
	
	protected long combo(long n, long i)
	{
	    return factorial(n) / ( factorial(n-i)*factorial(i) );
	}
	
	protected long factorial(long n)
	{
	    if(n < factorials.size())
	        return factorials.get((int)n);
	    
	    //If the factorial cache needs updating, synchronize to prevent corruption.
	    synchronized(factorials)
	    {
		    for(long i = factorials.size(); i <= n+1; i++)
		    {
		        factorials.add(factorials.get((int)i-1) * i);
		    }
	    }
	    
	    return factorials.get((int)n);
	}

}
