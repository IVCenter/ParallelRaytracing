package raytrace.medium;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.RayData;

public abstract class Medium {
	
	/*
	 * A base class for mediums
	 */
	
	/* *********************************************************************************************
	 * Enums
	 * *********************************************************************************************/
	public static enum Type {
		//BLENDING, //For mediums that can blend with other mediums on the stack
		PURE, //For mediums that are closed to any surrounding mediums (for example a glass ball inside of a smokey room)
		NONE //For mediums that do not affect light that passes through them
	};
	
	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	public static final double AIR_REFRACTIVE_INDEX = 1.0003;
		
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected boolean rayMarching = false;
	protected double rayMarchDistance = Double.POSITIVE_INFINITY;
	
	protected Type type = Type.NONE;
	
	protected double refractiveIndex = AIR_REFRACTIVE_INDEX;
	

	/* *********************************************************************************************
	 * Abstract Medium Methods
	 * *********************************************************************************************/
	public abstract Color scatterIn(Vector3 startPoint, Vector3 endPoint, Color light, Vector3 lightDirection);
	
	public abstract Color transmit(Vector3 startPoint, Vector3 endPoint, Color light);

	public abstract RayData sample(Vector3 startPoint, Vector3 endPoint);
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public boolean isRayMarching() {
		return rayMarching;
	}
	
	public void setRayMarching(boolean rayMarching) {
		this.rayMarching = rayMarching;
	}
	
	public double getRayMarchDistance() {
		return rayMarchDistance;
	}
	
	public void setRayMarchDistance(double rayMarchDistance) {
		this.rayMarchDistance = rayMarchDistance;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	public double getRefractiveIndex() {
		return refractiveIndex;
	}

	public void setRefractiveIndex(double refractiveIndex) {
		this.refractiveIndex = refractiveIndex;
	}
}
