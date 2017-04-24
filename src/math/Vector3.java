package math;

import java.io.Serializable;

import process.utils.StringUtils;

public class Vector3 implements Serializable{
	
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
	public static final Vector3 positiveXAxis = new Vector3(1,0,0);
	public static final Vector3 positiveYAxis = new Vector3(0,1,0);
	public static final Vector3 positiveZAxis = new Vector3(0,0,1);
	
	public static final Vector3 negativeXAxis = new Vector3(-1,0,0);
	public static final Vector3 negativeYAxis = new Vector3(0,-1,0);
	public static final Vector3 negativeZAxis = new Vector3(0,0,-1);

	
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
		
		if(mag == 0)
			return this;
		
		double invmag = 1.0 / mag;
		array[0] = array[0]*invmag;
		array[1] = array[1]*invmag;
		array[2] = array[2]*invmag;
		return this;
	}

	public Vector3 normalize()
	{
		double invmag = 1.0 / magnitude();
		return new Vector3(array[0]*invmag, array[1]*invmag, array[2]*invmag);
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
	
	public Vector3 multiply(double d, double e, double f)
	{
		return new Vector3(array[0]*d, array[1]*e, array[2]*f);
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
	
	public Vector3 multiplyM(double d, double e, double f)
	{
		array[0] *= d;
		array[1] *= e;
		array[2] *= f;
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
	
	public Vector3 add(double u, double v, double w)
	{
		return new Vector3(array[0]+u, array[1]+v, array[2]+w);
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
	
	public Vector3 addM(double u, double v, double w)
	{
		array[0] += u;
		array[1] += v;
		array[2] += w;
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

	//From SmallPPM
	//Vec operator%(Vec&b) {return Vec(y*b.z-z*b.y,z*b.x-x*b.z,x*b.y-y*b.x);};
	public Vector3 mod(Vector3 b)
	{
		return new Vector3(
				array[1] * b.array[2] - array[2] * b.array[1],
				array[2] * b.array[0] - array[0] * b.array[2],
				array[0] * b.array[1] - array[1] * b.array[0]);
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
	

	/* *********************************************************************************************
	 * Sample Methods
	 * *********************************************************************************************/

	public static Vector3 halfVector(Vector3 a, Vector3 b)
	{
		double[] ma = a.getArray();
		double[] mb = b.getArray();
		double maga = 1.0/a.magnitude();
		double magb = 1.0/b.magnitude();
		
		return (new Vector3(ma[0]*maga + mb[0]*magb,
							ma[1]*maga + mb[1]*magb,
							ma[2]*maga + mb[2]*magb)).normalizeM();
	}

	public static Vector3 cosineWeightedSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		Vector3 s = cosineWeightedSample();
		
		double[] sm = s.getArray();
	
		double[] xam = xa.getArray();
		double[] yam = ya.getArray();
		double[] zam = za.getArray();
		
		s.set(sm[0] * xam[0] + sm[1] * yam[0] + sm[2] * zam[0],
						   sm[0] * xam[1] + sm[1] * yam[1] + sm[2] * zam[1],
						   sm[0] * xam[2] + sm[1] * yam[2] + sm[2] * zam[2]);
		
		return s.normalizeM();
	}

	public static Vector3 cosineWeightedSample()
	{
		double s = Math.random();
		double t = Math.random();
		
		double u = 2.0 * Math.PI * s;
		double v = Math.sqrt(1.0 - t);
		
		double x = v * Math.cos(u);
		double y = Math.sqrt(t);
		double z = v * Math.sin(u);
		
		return (new Vector3(x, y, z)).normalizeM();
	}

	public static Vector3 uniformHemisphereSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		Vector3 s = uniformHemisphereSample();
		
		double[] sm = s.getArray();
	
		double[] xam = xa.getArray();
		double[] yam = ya.getArray();
		double[] zam = za.getArray();
		
		s.set(sm[0] * xam[0] + sm[1] * yam[0] + sm[2] * zam[0],
						   sm[0] * xam[1] + sm[1] * yam[1] + sm[2] * zam[1],
						   sm[0] * xam[2] + sm[1] * yam[2] + sm[2] * zam[2]);
		
		return s.normalizeM();
	}

	public static Vector3 uniformHemisphereSample()
	{
		Vector3 sample = new Vector3();
		
		do {
			sample.set(2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0);
		} while(sample.magnitudeSqrd() > 1.0 || sample.get(1) < 0.0);
		
		return sample.normalizeM();
	}

	public static Vector3 diskSample(double radius, double weight)
	{
		Vector3 sample = new Vector3();
		
		double theta = Math.random() * Math.PI * 2.0;
		double distance = Math.pow(Math.random(), weight) * radius;
		sample.set(distance * Math.cos(theta), distance * Math.sin(theta), 0);
		
		return sample;
	}

	public static Vector3 uniformSphereSample()
	{
		Vector3 sample = new Vector3();
		
		do {
			sample.set(2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0);
		} while(sample.magnitudeSqrd() > 1.0);
		
		return sample.normalizeM();
	}

	/*
	 * This is terribly inefficient.
	 * TODO: Implement a faster solution
	 */
	//Based on: http://math.stackexchange.com/a/205589
	public static Vector3 uniformConeSample(Vector3 direction, double theta)
	{
		Vector3 ndir = direction.normalize();
		double cosTheta = Math.cos(theta);
		double phi = Math.random() * 2.0 * Math.PI;
		
		double y = cosTheta + (1.0 - cosTheta) * Math.random();
		double yi = Math.sqrt(1.0 - y * y);
		double x = yi * Math.sin(phi);
		double z = yi * Math.cos(phi);
		
		Vector3 sample = new Vector3(x, y, z);
		
		//Rotate if the direction is not in the positive-y
		if(1.0 - Math.abs(ndir.dot(Vector3.positiveYAxis)) > 0.0001)
		{
			Vector3 axis = Vector3.positiveYAxis.cross(ndir);
			double angle = Math.acos(Vector3.positiveYAxis.dot(ndir));
			Matrix4 rot = new Matrix4();
			rot.identity();
			rot.rotateArbitrary(axis, angle);
			sample = rot.multiply3(sample);
		}
		
		return sample.normalizeM();
	}
	
}
