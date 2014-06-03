package raytrace.material;

import math.Ray;
import math.Vector4;
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
	protected static final double RECURSIVE_EPSILON = 0.0001;
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 10;
	protected static final int SYSTEM_RESURSION_LIMIT = 2000;
	public static final double AIR_REFRACTIVE_INDEX = 1.0003;

	protected static final double oneOverPi = 1.0 / Math.PI;

	public static final Vector4 positiveXAxis = new Vector4(1,0,0,0);
	public static final Vector4 positiveYAxis = new Vector4(0,1,0,0);
	public static final Vector4 positiveZAxis = new Vector4(0,0,1,0);

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
	protected Color diffuse(Color light, Vector4 normal, Vector4 fromLight)
	{
		double dot = normal.dot3(fromLight);
		if(dot >= 0.0)
			return Color.black();
		return light.multiply3( dot * -1.0 * oneOverPi );
	}
	
	protected Color reflect(ShadingData data, Vector4 point, Vector4 normal, double refractiveIndex)
	{	
		Vector4 dir = data.getIntersectionData().getRay().getDirection();
		//Vector4 reflect = dir.add3( normal.multiply3( -2.0 * dir.dot3(normal) ) ).normalize3();
		
		double c = -2.0 * dir.dot3(normal);
		
		double[] dirm = dir.getArray();
		double[] nm = normal.getArray();

		double rx = dirm[0] + nm[0] * c;
		double ry = dirm[1] + nm[1] * c;
		double rz = dirm[2] + nm[2] * c;
		
		Vector4 reflect = (new Vector4(rx, ry, rz, 0)).normalize3();
		
		return recurse(data, point, reflect, refractiveIndex);
	}

	protected Color recurse(ShadingData data, Vector4 point, Vector4 direction, double refractiveIndex)
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
		//return Color.black();
	}
	
	protected Vector4 halfVector(Vector4 a, Vector4 b)
	{
		double[] ma = a.getArray();
		double[] mb = b.getArray();
		double maga = 1.0/a.magnitude3();
		double magb = 1.0/b.magnitude3();
		
		return (new Vector4(ma[0]*maga + mb[0]*magb,
							ma[1]*maga + mb[1]*magb,
							ma[2]*maga + mb[2]*magb,
							0)).normalize3();
	}
	
	protected Vector4 cosineWeightedSample()
	{
		double s = Math.random();
		double t = Math.random();
		
		double u = 2.0 * Math.PI * s;
		double v = Math.sqrt(1.0 - t);
		
		double x = v * Math.cos(u);
		double y = Math.sqrt(t);
		double z = v * Math.sin(u);
		
		return (new Vector4(x, y, z, 0)).normalize3();
	}
	
	protected Vector4 cosineWeightedSample(Vector4 xa, Vector4 ya, Vector4 za)
	{
		Vector4 s = cosineWeightedSample();
		
		double[] sm = s.getArray();

		double[] xam = xa.getArray();
		double[] yam = ya.getArray();
		double[] zam = za.getArray();
		
		s.set(sm[0] * xam[0] + sm[1] * yam[0] + sm[2] * zam[0],
						   sm[0] * xam[1] + sm[1] * yam[1] + sm[2] * zam[1],
						   sm[0] * xam[2] + sm[1] * yam[2] + sm[2] * zam[2],
						   0);
		
		return s.normalize3();
	}
}
