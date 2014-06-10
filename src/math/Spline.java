package math;

import java.util.ArrayList;

import process.logging.Logger;

import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
import raytrace.material.Material;

public class Spline extends ArrayList<Vector4> {
	
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
	
	public Spline(ArrayList<Vector4> points)
	{
		super(points);
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public ArrayList<Vector4> points(int resolution)
	{
	    ArrayList<Vector4> points = new ArrayList<Vector4>();
	    double interval = (1.0 / (double)resolution);
	    for(double t = 0.0; t <= 1.0; t += interval)
	    {
	        points.add(point(t));
	    }
	    
	    return points;
	}
	
	public Vector4 point(double t)
	{
	    double[] coords = new double[3];
	    coords[0] = 0.0;
	    coords[1] = 0.0;
	    coords[2] = 0.0;
	    
	    t = Math.min(1.0, Math.max(0.0, t));
	    
	    long n = this.size()-1;
	    long i = 0;
	    for(Vector4 point : this)
	    {
	        coords[0] += bezier(n, i, t, point.get(0));
	        coords[1] += bezier(n, i, t, point.get(1));
	        coords[2] += bezier(n, i, t, point.get(2));
	        i++;
	    }
	    
	    return new Vector4(coords);
	}
	
	public Vector4 tangent(double t)
	{
	    t = Math.min(1.0 - approxTangentDelta, Math.max(0.0, t));
	    
	    Vector4 pre = point(t);
	    Vector4 post = point(t+approxTangentDelta);
	    
	    post.subtract3M(pre);
	    
	    return post.normalize3();
	}
	
	public ArrayList<Triangle> tessellate(int segments, int slices, double radiusStart, double radiusEnd)
	{
		ArrayList<Triangle> triangles = new ArrayList<Triangle>(segments * slices * 2 + 1);
		
		double segmentDelta = 1.0 / (double)segments;
		
		Vector4 thisPoint;
		Vector4 nextPoint = this.get(0);
		
		Vector4 thisNormal;
		Vector4 nextNormal = tangent(0);
		
		double thisRadius;
		double nextRadius = radiusStart;
		
		//This Basis
		Vector4 thisUTangent;
		Vector4 thisVTangent;

		//Next Basis
		Vector4 nextUTangent;
		Vector4 nextVTangent;
		
		if(Math.abs(nextNormal.dot3(Material.positiveYAxis)) == 1.0)
			nextUTangent = nextNormal.cross3(Material.positiveXAxis).normalize3();
		else
			nextUTangent = nextNormal.cross3(Material.positiveYAxis).normalize3();
		nextVTangent = nextUTangent.cross3(nextNormal).normalize3();
		
		
		for(double t = 0.0; t < 1.0; t += segmentDelta)
		{	
			Logger.progress(-79, "T as of now: " + t);
			thisPoint = nextPoint;
			nextPoint = point(t + segmentDelta);
			
			thisNormal = nextNormal;
			nextNormal = tangent(t + segmentDelta);
			
			thisRadius = nextRadius;
			nextRadius = (t + segmentDelta) * radiusEnd + (1.0 - (t + segmentDelta)) * radiusStart;
			
			thisUTangent = nextUTangent;
			thisVTangent = nextVTangent;
			
			if(Math.abs(nextNormal.dot3(Material.positiveYAxis)) == 1.0)
				nextUTangent = nextNormal.cross3(Material.positiveXAxis).normalize3();
			else
				nextUTangent = nextNormal.cross3(Material.positiveYAxis).normalize3();
			
			if(nextUTangent.dot3(thisUTangent) < 0.0)
				nextUTangent.multiply3M(-1.0);
			
			nextVTangent = nextUTangent.cross3(nextNormal).normalize3();
			
			
			/*
			 * TODO:
			 * 
			 * 		Radially sample both tangent disks n slices
			 * 		Generate triangles between sample points
			 */
			double angleDelta = (Math.PI * 2.0) / (double) slices;
			Vector4 v0, v1, v2, v3;
			Vertex subV0, subV0_2, subV1, subV2, subV2_2, subV3;
			Triangle tri1, tri2;
			
			for(double theta = 0; theta < (Math.PI * 2.0); theta += angleDelta)
			{
				//Get the four points
				v1 = thisUTangent.multiply3(Math.cos(theta) * thisRadius).add3M(thisVTangent.multiply3(Math.sin(theta) * thisRadius));
				v0 = nextUTangent.multiply3(Math.cos(theta) * nextRadius).add3M(nextVTangent.multiply3(Math.sin(theta) * nextRadius));
				v2 = thisUTangent.multiply3(Math.cos(theta + angleDelta) * thisRadius).add3M(thisVTangent.multiply3(Math.sin(theta + angleDelta) * thisRadius));
				v3 = nextUTangent.multiply3(Math.cos(theta + angleDelta) * nextRadius).add3M(nextVTangent.multiply3(Math.sin(theta + angleDelta) * nextRadius));
				
				//Make vertices
				//Generate for sub vertices
				subV0 = new Vertex(
						v0.add3(nextPoint), 
						v0.add3(0.0).normalize3(), 
						new Vector4(theta / (Math.PI*2.0), t + segmentDelta, 0, 0));
				
				subV0_2 = new Vertex(
						v0.add3(nextPoint), 
						v0.add3(0.0).normalize3(), 
						new Vector4(theta / (Math.PI*2.0), t + segmentDelta, 0, 0));
				
				subV1 = new Vertex(
						v1.add3(thisPoint), 
						v1.add3(0.0).normalize3(), 
						new Vector4(theta / (Math.PI*2.0), t, 0, 0));
				
				subV2 = new Vertex(
						v2.add3(thisPoint), 
						v2.add3(0.0).normalize3(), 
						new Vector4((theta + angleDelta) / (Math.PI*2.0), t, 0, 0));
				
				subV2_2 = new Vertex(
						v2.add3(thisPoint), 
						v2.add3(0.0).normalize3(),
						new Vector4((theta + angleDelta) / (Math.PI*2.0), t, 0, 0));
				
				subV3 = new Vertex(
						v3.add3(nextPoint), 
						v3.add3(0.0).normalize3(), 
						new Vector4((theta + angleDelta) / (Math.PI*2.0), t + segmentDelta, 0, 0));
				
				tri1 = new Triangle(subV0, subV1, subV2);
				tri2 = new Triangle(subV0_2, subV2_2, subV3);
				tri1.generateFaceNormal();
				tri2.generateFaceNormal();
				triangles.add(tri1);
				triangles.add(tri2);
				
				Logger.progress(-79, "Triangles: " + triangles.size());
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
