package math;

import java.io.Serializable;

import process.utils.StringUtils;

public class Vector4 implements Serializable{
	
	/*
	 * An extension of the Vector4d class to allow for additional features
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	public static final Vector4 XAXIS = new Vector4(1.0, 0.0, 0.0, 0.0);
	public static final Vector4 YAXIS = new Vector4(0.0, 1.0, 0.0, 0.0);
	public static final Vector4 ZAXIS = new Vector4(0.0, 0.0, 1.0, 0.0);

	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double[] array;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vector4()
	{
		array = new double[4];
	}
	
	public Vector4(double x, double y, double z, double w)
	{
		this();
		set(x,y,z,w);
	}
	
	public Vector4(Vector4 v)
	{
		this();
		double[] vm = v.getM();
		set(vm);
	}
	
	public Vector4(double[] v)
	{
		this();
		set(v);
	}
	
	public double[] getM()
	{
		return array;
	}
	
	public double get(int element)
	{
		return array[element];
	}
	
	public void set(double x, double y, double z, double w)
	{
		array[0] = x;
		array[1] = y;
		array[2] = z;
		array[3] = w;
	}
	
	public void set(int element, double value)
	{
		array[element] = value;
	}
	
	public void set(double[] v)
	{
		for(int i = 0; i < 4; ++i)
			array[i] = v[i];
	}
	
	public void set(Vector4 v)
	{
		set(v.getM());
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public Vector4 cross3(Vector4 that)
	{
		double[] vm = that.getM();
		Vector4 res = new Vector4(array[1]*vm[2] - array[2]*vm[1],
								  array[2]*vm[0] - array[0]*vm[2],
								  array[0]*vm[1] - array[1]*vm[0],
								  0);
		return res;
	}
	
	public double dot3(Vector4 that)
	{
		double[] vm = that.getM();
		return array[0]*vm[0] + array[1]*vm[1] + array[2]*vm[2];
	}
	
	public double angle3(Vector4 that)
	{
		return Math.acos(this.dot3(that) / (this.magnitude3() * that.magnitude3()));
	}
	
	public Vector4 normalize3()
	{
		double mag = magnitude3();
		array[0] = array[0]/mag;
		array[1] = array[1]/mag;
		array[2] = array[2]/mag;
		return this;
	}
	
	public double magnitude3Sqrd()
	{
		return array[0] * array[0] + array[1] * array[1] + array[2] * array[2];
	}
	
	public double magnitude3()
	{
		return Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2]);
	}
	
	public Vector4 multiply3(double d)
	{
		return new Vector4(array[0]*d, array[1]*d, array[2]*d, array[3]);
	}
	
	public Vector4 add3(Vector4 that)
	{
		double[] vm = that.getM();
		return new Vector4(array[0]+vm[0], array[1]+vm[1], array[2]+vm[2], array[3]);
	}
	
	public Vector4 add3(double d)
	{
		return new Vector4(array[0]+d, array[1]+d, array[2]+d, array[3]);
	}
	
	public Vector4 add3M(Vector4 that)
	{
		double[] vm = that.getM();
		array[0] += vm[0];
		array[1] += vm[1];
		array[2] += vm[2];
		return this;
	}
	
	public Vector4 add3M(double d)
	{
		array[0] += d;
		array[1] += d;
		array[2] += d;
		return this;
	}
	
	public Vector4 addMultiRight3(Vector4 that, double c)
	{
		double[] vm = that.getM();
		return new Vector4(array[0]+vm[0]*c, array[1]+vm[1]*c, array[2]+vm[2]*c, array[3]);
	}
	
	public Vector4 addMultiRight3M(Vector4 that, double c)
	{
		double[] vm = that.getM();
		array[0] += vm[0]*c;
		array[1] += vm[1]*c;
		array[2] += vm[2]*c;
		return this;
	}
	
	public Vector4 subtract3(Vector4 that)
	{
		double[] vm = that.getM();
		return new Vector4(array[0]-vm[0], array[1]-vm[1], array[2]-vm[2], array[3]);
	}
	
	public Vector4 subtract3(double d)
	{
		return new Vector4(array[0]-d, array[1]-d, array[2]-d, array[3]);
	}
	
	public Vector4 minimize3(Vector4 that)
	{
		double[] vm = that.getM();
		array[0] = array[0] < vm[0] ? array[0] : vm[0];
		array[1] = array[1] < vm[1] ? array[1] : vm[1];
		array[2] = array[2] < vm[2] ? array[2] : vm[2];
		return this;
	}
	
	public Vector4 maximize3(Vector4 that)
	{
		double[] vm = that.getM();
		array[0] = array[0] > vm[0] ? array[0] : vm[0];
		array[1] = array[1] > vm[1] ? array[1] : vm[1];
		array[2] = array[2] > vm[2] ? array[2] : vm[2];
		return this;
	}

	

	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	public void print()
	{
		System.out.println("[" + StringUtils.column(""+array[0], 24) + ", " + 
								 StringUtils.column(""+array[1], 24) + ", " +
								 StringUtils.column(""+array[2], 24) + ", " +
								 StringUtils.column(""+array[3], 24) + "]");
	}
}
