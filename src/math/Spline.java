package math;

import java.util.ArrayList;

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
