package raytrace.material;

import math.Vector3;
import math.ray.Ray;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.surfaces.CompositeSurface;

public abstract class Material {
	
	/*
	 * A base class for programmable materials
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected static final double RECURSIVE_EPSILON = 0.00001;
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 3;
	protected static final int SYSTEM_RESURSION_LIMIT = 100;
	
	public static final double AIR_REFRACTIVE_INDEX = 1.0003;

	protected static final double oneOverPi = 1.0 / Math.PI;


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
		
		if(fromLight.magnitudeSqrd() == 0.0)
			return light.multiply3(1.0);
		
		if(dot >= 0.0)
			return Color.black();
		
		return light.multiply3( dot * -1.0 * oneOverPi );
	}
	
	protected Color reflect(ShadingData data, Vector3 point, Vector3 normal, double refractiveIndex)
	{
		return reflect(data, point, normal, refractiveIndex, true);
	}
	
	protected Color reflect(ShadingData data, Vector3 point, Vector3 normal, double refractiveIndex, boolean increaseRecurDepth)
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
		
		return recurse(data, point, reflect, refractiveIndex, increaseRecurDepth);
	}

	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex)
	{
		return recurse(data, point, direction, refractiveIndex, true);
	}
	
	/*
	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex, boolean increaseRecurDepth)
	{		
		//If we're past the ABSOLUTE recursive limit, use black
		if(data.getActualRecursionDepth() >= SYSTEM_RESURSION_LIMIT) {
			return Color.black();
		}
		
		RayData rdata = new RayData();
		Ray ray = new Ray(point, direction, 0, 0);
		rdata.setRay(ray);
		rdata.setRootSurface(data.getRootScene());
		rdata.setTStart(RECURSIVE_EPSILON);
		rdata.setTEnd(Double.MAX_VALUE);
		
		ShadingData sdata = new ShadingData();
		//sdata.setRay(rdata.getRay());
		sdata.setRayData(rdata);
		sdata.setRootScene(data.getRootScene());
		sdata.setActualRecursionDepth(data.getActualRecursionDepth() + 1);
		sdata.setRecursionDepth(data.getRecursionDepth() + (increaseRecurDepth ? 1 : 0) );
		sdata.setRefractiveIndex(refractiveIndex);
		
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		
		if(idata != null) {
			sdata.setIntersectionData(idata);
			return idata.getMaterial().shade(sdata);
		}		

		//If there wasn't an intersection, use the sky material
		sdata.setIntersectionData(null);
		return data.getRootScene().getSkyMaterial().shade(sdata);
	}*/
	
	
	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex, boolean increaseRecurDepth)
	{		
		//If we're past the ABSOLUTE recursive limit, use black
		if(data.getActualRecursionDepth() >= SYSTEM_RESURSION_LIMIT) {
			return Color.black();
		}
		
		
		//Store the old values locally
		RayData rdata = data.getRayData();
		Ray ray = rdata.getRay();
	 	//Vector3 oldPoint = ray.getOrigin();
	 	//Vector3 oldDirection = ray.getDirection();
		//double oldT0 = rdata.getTStart();
		//double oldT1 = rdata.getTEnd();
		
		//Replace the RayDatas values with the current recursion values
		ray.setOrigin(point);
		ray.setDirection(direction);
		rdata.setTStart(RECURSIVE_EPSILON);
		rdata.setTEnd(Double.MAX_VALUE);
		
		//int oldActualRecursionDepth = data.getActualRecursionDepth();
		//int oldRecursionDepth = data.getRecursionDepth();
		//double oldRefractiveIndex = data.getRefractiveIndex();
		
		data.setActualRecursionDepth(data.getActualRecursionDepth() + 1);
		data.setRecursionDepth(data.getRecursionDepth() + (increaseRecurDepth ? 1 : 0) );
		data.setRefractiveIndex(refractiveIndex);
		
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		
		
		//Color resultColor = null;
		
		if(idata != null) {
			data.setIntersectionData(idata);
			//resultColor = idata.getMaterial().shade(data);
			return idata.getMaterial().shade(data);
		}//else{
			//If there wasn't an intersection, use the sky material
			data.setIntersectionData(null);
			//resultColor = data.getRootScene().getSkyMaterial().shade(data);
			return data.getRootScene().getSkyMaterial().shade(data);
		//}
		
		
		//Undo the changes we made to rdata and sdata
		//data.setActualRecursionDepth(oldActualRecursionDepth);
		//data.setRecursionDepth(oldRecursionDepth);
		//data.setRefractiveIndex(oldRefractiveIndex);
		
		//ray.setOrigin(oldPoint);
		//ray.setDirection(oldDirection);
		//rdata.setTStart(oldT0);
		//rdata.setTEnd(oldT1);
		
		
		//return resultColor;
	}
	
	
	protected Vector3 halfVector(Vector3 a, Vector3 b)
	{
		return Vector3.halfVector(a, b);
	}
	
	protected Vector3 cosineWeightedSample()
	{
		return Vector3.cosineWeightedSample();
	}
	
	protected Vector3 cosineWeightedSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		return Vector3.cosineWeightedSample(xa, ya, za);
	}
	
	protected Vector3 uniformHemisphereSample()
	{
		return Vector3.uniformHemisphereSample();
	}
	
	protected Vector3 uniformHemisphereSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		return Vector3.uniformHemisphereSample(xa, ya, za);
	}

	protected Vector3 diskSample(double radius, double weight)
	{
		return Vector3.diskSample(radius, weight);
	}
	
	protected Vector3 uniformSphereSample()
	{
		return Vector3.uniformSphereSample();
	}
}
