package raytrace.material;

import math.Ray;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.scene.Scene;

public abstract class Material {
	
	/*
	 * A base class for programmable materials
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected static final double RECURSIVE_EPSILON = 0.0001;
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 10;
	public static final double AIR_REFRACTIVE_INDEX = 1.003;
	

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
		return light.multiply3( dot * -1.0 );
	}
	
	protected IntersectionData shadowed(Scene scene, Ray ray, double distanceToLight)
	{
		RayData rdata = new RayData();
		rdata.setRay(ray);
		rdata.setTStart(RECURSIVE_EPSILON);
		IntersectionData idata = scene.intersects(rdata);
		
		//If no intersection, not shadowed
		if(idata == null)
			return null;
		
		//If the light is infinitely far, and we hit something, in shadow
		if(distanceToLight == Double.MAX_VALUE)
			return idata;
		
		if(idata.getDistance() < distanceToLight)
			return idata;
		
		return null;
	}
	
	protected Color reflect(ShadingData data, Vector4 point, Vector4 normal, double refractiveIndex)
	{	
		Vector4 dir = data.getIntersectionData().getRay().getDirection();
		Vector4 reflect = dir.add3( normal.multiply3( -2.0 * dir.dot3(normal) ) ).normalize3();
		
		return recurse(data, point, reflect, refractiveIndex);
	}

	protected Color recurse(ShadingData data, Vector4 point, Vector4 direction, double refractiveIndex)
	{	
		RayData rdata = new RayData();
		Ray ray = new Ray(point, direction, 0, 0);
		rdata.setRay(ray);
		rdata.setTStart(RECURSIVE_EPSILON);
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		ShadingData sdata = new ShadingData();
		sdata.setRay(rdata.getRay());
		sdata.setRootScene(data.getRootScene());
		sdata.setRecursionDepth(data.getRecursionDepth() + 1);
		sdata.setRefractiveIndex(refractiveIndex);
		
		if(idata != null) {
			sdata.setIntersectionData(idata);
			return idata.getMaterial().shade(sdata);
		}		

		//If there wasn't an intersection, use the sky material
		sdata.setIntersectionData(null);
		return data.getRootScene().getSkyMaterial().shade(sdata);
	}
}
