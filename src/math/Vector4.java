package math;

import javax.vecmath.Vector4d;

public class Vector4 extends Vector4d {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6002825425579913543L;

	
	
	/*
	 * An extension of the Vector4d class to allow for additional features
	 */
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vector4()
	{
		super(0,0,0,0);
	}
	
	public Vector4(double x, double y, double z, double w)
	{
		super(x,y,z,w);
	}
	
	public Vector4(Vector4 v)
	{
		super(v.x,v.y,v.z,v.w);
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public Vector4 cross3(Vector4 that)
	{
		Vector4 res = new Vector4();
		res.x = this.y*that.z - this.z*that.y;
		res.y = this.z*that.x - this.x*that.z;
		res.z = this.x*that.y - this.y*that.x;
		res.w = 0;
		return res;
	}
	
	public double dot3(Vector4 that)
	{
		return this.x*that.x + this.y*that.y + this.z*that.z;
	}
	
	public double angle3(Vector4 that)
	{
		return  Math.acos(this.dot3(that) / (this.magnitude3() * that.magnitude3()));
	}
	
	public Vector4 normalize3()
	{
		double m = magnitude3();
		x = x/m;
		y = y/m;
		z = z/m;
		return this;
	}
	
	public double magnitude3Sqrd()
	{
		return Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0);
	}
	
	public double magnitude3()
	{
		return Math.pow(magnitude3Sqrd(), 0.5);
	}
	
	public Vector4 multiply3(double d)
	{
		return new Vector4(x*d, y*d, z*d, w);
	}
}
