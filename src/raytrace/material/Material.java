package raytrace.material;

import math.Ray;
import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;

public abstract class Material {
	
	/*
	 * A base class for programmable materials
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected static final double RECURSIVE_EPSILON = 0.00001;
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 10;
	protected static final int SYSTEM_RESURSION_LIMIT = 2000;
	public static final double AIR_REFRACTIVE_INDEX = 1.0003;

	protected static final double oneOverPi = 1.0 / Math.PI;

	public static final Vector3 positiveXAxis = new Vector3(1,0,0);
	public static final Vector3 positiveYAxis = new Vector3(0,1,0);
	public static final Vector3 positiveZAxis = new Vector3(0,0,1);

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Abstract Programmable Methods
	 * *********************************************************************************************/
	public abstract Color shade(ShadingData data);
	

	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	protected Color diffuse(Color light, Vector3 normal, Vector3 fromLight)
	{
		double dot = normal.dot(fromLight);
		if(dot >= 0.0)
			return Color.black();
		return light.multiply3( dot * -1.0 * oneOverPi );
	}
	
	protected Color reflect(ShadingData data, Vector3 point, Vector3 normal, double refractiveIndex)
	{	
		Vector3 dir = data.getIntersectionData().getRay().getDirection();
		//Vector4 reflect = dir.add3( normal.multiply3( -2.0 * dir.dot3(normal) ) ).normalize3();
		
		double c = -2.0 * dir.dot(normal);
		
		double[] dirm = dir.getArray();
		double[] nm = normal.getArray();

		double rx = dirm[0] + nm[0] * c;
		double ry = dirm[1] + nm[1] * c;
		double rz = dirm[2] + nm[2] * c;
		
		Vector3 reflect = (new Vector3(rx, ry, rz)).normalizeM();
		
		return recurse(data, point, reflect, refractiveIndex);
	}

	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex)
	{		
		//If we're past the ABSOLUTE recursive limit, use black
		if(data.getRecursionDepth() >= SYSTEM_RESURSION_LIMIT) {
			return Color.black();
		}
		
		RayData rdata = new RayData();
		Ray ray = new Ray(point, direction, 0, 0);
		rdata.setRay(ray);
		rdata.setRootSurface(data.getRootScene());
		rdata.setTStart(RECURSIVE_EPSILON);
		rdata.setTEnd(Double.MAX_VALUE);
		
		ShadingData sdata = new ShadingData();
		sdata.setRay(rdata.getRay());
		sdata.setRootScene(data.getRootScene());
		sdata.setRecursionDepth(data.getRecursionDepth() + 1);
		sdata.setRefractiveIndex(refractiveIndex);
		
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		
		if(idata != null) {
			sdata.setIntersectionData(idata);
			return idata.getMaterial().shade(sdata);
		}		

		//If there wasn't an intersection, use the sky material
		sdata.setIntersectionData(null);
		return data.getRootScene().getSkyMaterial().shade(sdata);
	}
	
	protected Vector3 halfVector(Vector3 a, Vector3 b)
	{
		double[] ma = a.getArray();
		double[] mb = b.getArray();
		double maga = 1.0/a.magnitude();
		double magb = 1.0/b.magnitude();
		
		return (new Vector3(ma[0]*maga + mb[0]*magb,
							ma[1]*maga + mb[1]*magb,
							ma[2]*maga + mb[2]*magb)).normalizeM();
	}
	
	protected Vector3 cosineWeightedSample()
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
	
	protected Vector3 cosineWeightedSample(Vector3 xa, Vector3 ya, Vector3 za)
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
	
	protected Vector3 uniformHemisphereSample()
	{
		Vector3 sample = new Vector3();
		
		do {
			sample.set(2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0);
		} while(sample.magnitudeSqrd() > 1.0 || sample.get(1) < 0.0);
		
		return sample.normalizeM();
	}
	
	protected Vector3 uniformHemisphereSample(Vector3 xa, Vector3 ya, Vector3 za)
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

	protected Vector3 diskSample(double radius, double weight)
	{
		Vector3 sample = new Vector3();
		
		double theta = Math.random() * Math.PI * 2.0;
		double distance = Math.pow(Math.random(), weight) * radius;
		sample.set(distance * Math.cos(theta), distance * Math.sin(theta), 0);
		
		return sample;
	}
	
	protected Vector3 uniformSphereSample()
	{
		Vector3 sample = new Vector3();
		
		do {
			sample.set(2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0, 2.0 * Math.random() - 1.0);
		} while(sample.magnitudeSqrd() > 1.0);
		
		return sample.normalizeM();
	}
}
