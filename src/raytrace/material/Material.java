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
	protected static final double RECURSIVE_EPSILON = 0.001;
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 2000;
	

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
}
