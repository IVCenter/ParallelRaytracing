package math;

public class Quaternion {
	
	/*
	 * An implementation of a 3D vector.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/

	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double[] array;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Quaternion()
	{
		array = new double[4];
	}
	
	public Quaternion(double w, double x, double y, double z)
	{
		this();
		set(w,x,y,z);
	}
	
	public Quaternion(Quaternion q)
	{
		this();
		double[] qm = q.getArray();
		set(qm);
	}
	
	public Quaternion(Vector3 v, double z)
	{
		this();
		double[] vm = v.getArray();
		set(vm[0], vm[1], vm[2], z);
	}
	
	public Quaternion(double[] q)
	{
		this();
		set(q);
	}
	
	public double[] getArray()
	{
		return array;
	}
	
	public double get(int element)
	{
		return array[element];
	}

	public void set(Vector3 v, double z)
	{
		double[] vm = v.array;
		array[0] = vm[0];
		array[1] = vm[1];
		array[2] = vm[2];
		array[3] = z;
	}
	
	public void set(double w, double x, double y, double z)
	{
		array[0] = w;
		array[1] = x;
		array[2] = y;
		array[3] = z;
	}
	
	public void set(int element, double value)
	{
		array[element] = value;
	}
	
	public void set(double[] v)
	{
		for(int i = 0; i < v.length && i < 4; ++i)
			array[i] = v[i];
	}
	
	public void set(Quaternion q)
	{
		set(q.getArray());
	}


	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public double dot(Quaternion q)
	{
		double[] vm = q.array;
		return array[0] * vm[0] + array[1] * vm[1] + array[2] * vm[2] + array[3] * vm[3];
	}
	
	public Quaternion add(double w, double x, double y, double z)
	{
		return new Quaternion(array[0] + w, array[1] + x, array[2] + y, array[3] + z);
	}
	
	public Quaternion addM(double w, double x, double y, double z)
	{
		set(array[0] + w, array[1] + x, array[2] + y, array[3] + z);
		return this;
	}
	
	public Quaternion add(Quaternion q)
	{
		double[] vm = q.array;
		return new Quaternion(array[0] + vm[0], array[1] + vm[1], array[2] + vm[2], array[3] + vm[3]);
	}
	
	public Quaternion addM(Quaternion q)
	{
		double[] vm = q.array;
		set(array[0] + vm[0], array[1] + vm[1], array[2] + vm[2], array[3] + vm[3]);
		return this;
	}
	
	public Quaternion multiply(double d)
	{
		return new Quaternion(array[0] * d, array[1] * d, array[2] * d, array[3] * d);
	}
	
	public Quaternion multiplyM(double d)
	{
		set(array[0] * d, array[1] * d, array[2] * d, array[3] * d);
		return this;
	}
	
	public Quaternion multiply(Quaternion q)
	{
		double[] vm = q.array;
		double w = array[0] * vm[0] - (array[1] * vm[1] + array[2] * vm[2] + array[3] * vm[3]);
		double x = array[0] * vm[1] + vm[0] * array[1] + (array[2] * vm[3] - array[3] * vm[2]);
		double y = array[0] * vm[2] + vm[0] * array[2] + (array[3] * vm[1] - array[1] * vm[3]);
		double z = array[0] * vm[3] + vm[0] * array[3] + (array[1] * vm[2] - array[2] * vm[1]);
		return new Quaternion(w,x,y,z);
	}
	
	public Quaternion multiplyM(Quaternion q)
	{
		double[] vm = q.array;
		double w = array[0]*vm[0] - (array[1] * vm[1] + array[2] * vm[2] + array[3] * vm[3]);
		double x = array[0] * vm[1] + vm[0] * array[1] + (array[2]*vm[3] - array[3]*vm[2]);
		double y = array[0] * vm[2] + vm[0] * array[2] + (array[3]*vm[1] - array[1]*vm[3]);
		double z = array[0] * vm[3] + vm[0] * array[3] + (array[1]*vm[2] - array[2]*vm[1]);
		set(w,x,y,z);
		return this;
	}
	
	public Quaternion square()
	{
		double w = array[0] * array[0] - (array[1] * array[1] + array[2] * array[2] + array[3] * array[3]);
		double x = 2 * array[0] * array[1];
		double y = 2 * array[0] * array[2];
		double z = 2 * array[0] * array[3];
		return new Quaternion(w,x,y,z);
	}
	
	public Quaternion squareM()
	{
		double w = array[0] * array[0] - (array[1] * array[1] + array[2] * array[2] + array[3] * array[3]);
		double x = 2 * array[0] * array[1];
		double y = 2 * array[0] * array[2];
		double z = 2 * array[0] * array[3];
		set(w,x,y,z);
		return this;
	}
	
	public double magnitudeSqrd()
	{
		return array[0] * array[0] + array[1] * array[1] + array[2] * array[2] + array[3] * array[3];
	}
	
	public double magnitude()
	{
		return Math.sqrt(array[0] * array[0] + array[1] * array[1] + array[2] * array[2] + array[3] * array[3]);
	}
}
