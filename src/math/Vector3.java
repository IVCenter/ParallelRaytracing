package math;

import java.io.Serializable;

import process.utils.StringUtils;

public class Vector3 implements Serializable{
	
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
	public static final Vector3 XAXIS = new Vector3(1.0, 0.0, 0.0);
	public static final Vector3 YAXIS = new Vector3(0.0, 1.0, 0.0);
	public static final Vector3 ZAXIS = new Vector3(0.0, 0.0, 1.0);

	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double[] array;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vector3()
	{
		array = new double[3];
	}
	
	public Vector3(double x, double y, double z)
	{
		this();
		set(x,y,z);
	}
	
	public Vector3(Vector3 v)
	{
		this();
		double[] vm = v.getArray();
		set(vm);
	}
	
	public Vector3(double[] v)
	{
		this();
		set(v);
	}
	
	public double[] getArray()
	{
		return array;
	}
	
	public double get(int element)
	{
		return array[element];
	}
	
	public void set(double x, double y, double z)
	{
		array[0] = x;
		array[1] = y;
		array[2] = z;
	}
	
	public void set(int element, double value)
	{
		array[element] = value;
	}
	
	public void set(double[] v)
	{
		for(int i = 0; i < v.length && i < 3; ++i)
			array[i] = v[i];
	}
	
	public void set(Vector3 v)
	{
		set(v.getArray());
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public Vector3 cross(Vector3 that)
	{
		double[] vm = that.getArray();
		Vector3 res = new Vector3(array[1]*vm[2] - array[2]*vm[1],
								  array[2]*vm[0] - array[0]*vm[2],
								  array[0]*vm[1] - array[1]*vm[0]);
		return res;
	}
	
	public double dot(Vector3 that)
	{
		double[] vm = that.getArray();
		return array[0]*vm[0] + array[1]*vm[1] + array[2]*vm[2];
	}
	
	public double angle(Vector3 that)
	{
		return Math.acos(this.dot(that) / (this.magnitude() * that.magnitude()));
	}
	
	public Vector3 normalizeM()
	{
		double mag = magnitude();
		array[0] = array[0]/mag;
		array[1] = array[1]/mag;
		array[2] = array[2]/mag;
		return this;
	}

	public Vector3 normalize()
	{
		double mag = magnitude();
		return new Vector3(array[0]/mag, array[0]/mag, array[2]/mag);
	}
	
	public double magnitudeSqrd()
	{
		return array[0] * array[0] + array[1] * array[1] + array[2] * array[2];
	}
	
	public double magnitude()
	{
		return Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2]);
	}
	
	public Vector3 multiply(double d)
	{
		return new Vector3(array[0]*d, array[1]*d, array[2]*d);
	}

	public Vector3 multiply(Vector3 that)
	{
		return new Vector3(array[0]*that.array[0], array[1]*that.array[1], array[2]*that.array[2]);
	}
	
	public Vector3 multiplyM(double d)
	{
		array[0] *= d;
		array[1] *= d;
		array[2] *= d;
		return this;
	}
	
	public Vector3 multiplyM(Vector3 that)
	{
		array[0] *= that.array[0];
		array[1] *= that.array[1];
		array[2] *= that.array[2];
		return this;
	}
	
	public Vector3 add(Vector3 that)
	{
		double[] vm = that.getArray();
		return new Vector3(array[0]+vm[0], array[1]+vm[1], array[2]+vm[2]);
	}
	
	public Vector3 add(double d)
	{
		return new Vector3(array[0]+d, array[1]+d, array[2]+d);
	}
	
	public Vector3 addM(Vector3 that)
	{
		double[] vm = that.getArray();
		array[0] += vm[0];
		array[1] += vm[1];
		array[2] += vm[2];
		return this;
	}
	
	public Vector3 addM(double d)
	{
		array[0] += d;
		array[1] += d;
		array[2] += d;
		return this;
	}
	
	public Vector3 addMultiRight(Vector3 that, double c)
	{
		double[] vm = that.getArray();
		return new Vector3(array[0]+vm[0]*c, array[1]+vm[1]*c, array[2]+vm[2]*c);
	}
	
	public Vector3 addMultiRightM(Vector3 that, double c)
	{
		double[] vm = that.getArray();
		array[0] += vm[0]*c;
		array[1] += vm[1]*c;
		array[2] += vm[2]*c;
		return this;
	}
	
	public Vector3 subtract(Vector3 that)
	{
		double[] vm = that.getArray();
		return new Vector3(array[0]-vm[0], array[1]-vm[1], array[2]-vm[2]);
	}
	
	public Vector3 subtract(double d)
	{
		return new Vector3(array[0]-d, array[1]-d, array[2]-d);
	}
	
	public Vector3 subtractM(Vector3 that)
	{
		double[] vm = that.getArray();
		array[0] -= vm[0];
		array[1] -= vm[1];
		array[2] -= vm[2];
		return this;
	}
	
	public Vector3 subtractM(double d)
	{
		array[0] -= d;
		array[1] -= d;
		array[2] -= d;
		return this;
	}
	
	public Vector3 minimizeM(Vector3 that)
	{
		double[] vm = that.getArray();
		array[0] = array[0] < vm[0] ? array[0] : vm[0];
		array[1] = array[1] < vm[1] ? array[1] : vm[1];
		array[2] = array[2] < vm[2] ? array[2] : vm[2];
		return this;
	}
	
	public Vector3 maximizeM(Vector3 that)
	{
		double[] vm = that.getArray();
		array[0] = array[0] > vm[0] ? array[0] : vm[0];
		array[1] = array[1] > vm[1] ? array[1] : vm[1];
		array[2] = array[2] > vm[2] ? array[2] : vm[2];
		return this;
	}

	public double distance(Vector3 that)
	{
		double x = array[0] - that.array[0];
		double y = array[1] - that.array[1];
		double z = array[2] - that.array[2];
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double manhattanDistance(Vector3 that)
	{
		double x = array[0] - that.array[0];
		double y = array[1] - that.array[1];
		double z = array[2] - that.array[2];
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}

	public double tchebyshevDistance(Vector3 that)
	{
		double x = array[0] - that.array[0];
		double y = array[1] - that.array[1];
		double z = array[2] - that.array[2];
		return Math.max(Math.abs(x), Math.max(Math.abs(y), Math.abs(z)));
	}
	

	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	public void print()
	{
		System.out.println("[" + StringUtils.column(""+array[0], 24) + ", " + 
								 StringUtils.column(""+array[1], 24) + ", " +
								 StringUtils.column(""+array[2], 24) + "]");
	}
	
}
