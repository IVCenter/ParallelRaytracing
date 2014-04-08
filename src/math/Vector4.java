package math;

import process.utils.StringUtils;

public class Vector4 {
	
	
	/*
	 * An extension of the Vector4d class to allow for additional features
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double[] m;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Vector4()
	{
		m = new double[4];
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
		return m;
	}
	
	public double get(int element)
	{
		return m[element];
	}
	
	public void set(double x, double y, double z, double w)
	{
		m[0] = x;
		m[1] = y;
		m[2] = z;
		m[3] = w;
	}
	
	public void set(int element, double value)
	{
		m[element] = value;
	}
	
	public void set(double[] v)
	{
		for(int i = 0; i < 4; ++i)
			m[i] = v[i];
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
		Vector4 res = new Vector4(m[1]*vm[2] - m[2]*vm[1],
								  m[2]*vm[0] - m[0]*vm[2],
								  m[0]*vm[1] - m[1]*vm[0],
								  0);
		return res;
	}
	
	public double dot3(Vector4 that)
	{
		double[] vm = that.getM();
		return m[0]*vm[0] + m[1]*vm[1] + m[2]*vm[2];
	}
	
	public double angle3(Vector4 that)
	{
		return Math.acos(this.dot3(that) / (this.magnitude3() * that.magnitude3()));
	}
	
	public Vector4 normalize3()
	{
		double mag = magnitude3();
		m[0] = m[0]/mag;
		m[1] = m[1]/mag;
		m[2] = m[2]/mag;
		return this;
	}
	
	public double magnitude3Sqrd()
	{
		return m[0] * m[0] + m[1] * m[1] + m[2] * m[2];
	}
	
	public double magnitude3()
	{
		return Math.sqrt(m[0] * m[0] + m[1] * m[1] + m[2] * m[2]);
	}
	
	public Vector4 multiply3(double d)
	{
		return new Vector4(m[0]*d, m[1]*d, m[2]*d, m[3]);
	}
	
	public Vector4 add3(Vector4 that)
	{
		double[] vm = that.getM();
		return new Vector4(m[0]+vm[0], m[1]+vm[1], m[2]+vm[2], m[3]);
	}
	
	public Vector4 add3(double d)
	{
		return new Vector4(m[0]+d, m[1]+d, m[2]+d, m[3]);
	}
	
	public Vector4 addMultiRight3(Vector4 that, double c)
	{
		double[] vm = that.getM();
		return new Vector4(m[0]+vm[0]*c, m[1]+vm[1]*c, m[2]+vm[2]*c, m[3]);
	}
	
	public Vector4 subtract3(Vector4 that)
	{
		double[] vm = that.getM();
		return new Vector4(m[0]-vm[0], m[1]-vm[1], m[2]-vm[2], m[3]);
	}
	
	public Vector4 subtract3(double d)
	{
		return new Vector4(m[0]-d, m[1]-d, m[2]-d, m[3]);
	}
	
	public Vector4 minimize3(Vector4 that)
	{
		double[] vm = that.getM();
		m[0] = m[0] < vm[0] ? m[0] : vm[0];
		m[1] = m[1] < vm[1] ? m[1] : vm[1];
		m[2] = m[2] < vm[2] ? m[2] : vm[2];
		return this;
	}
	
	public Vector4 maximize3(Vector4 that)
	{
		double[] vm = that.getM();
		m[0] = m[0] > vm[0] ? m[0] : vm[0];
		m[1] = m[1] > vm[1] ? m[1] : vm[1];
		m[2] = m[2] > vm[2] ? m[2] : vm[2];
		return this;
	}

	

	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	public void print()
	{
		System.out.println("[" + StringUtils.column(""+m[0], 8) + ", " + 
								 StringUtils.column(""+m[1], 8) + ", " +
								 StringUtils.column(""+m[2], 8) + ", " +
								 StringUtils.column(""+m[3], 8) + "]");
	}
}
