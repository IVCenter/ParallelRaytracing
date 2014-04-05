package raytrace.data;

import math.Vector4;
import raytrace.color.Color;

public class IlluminationData {
	
	/*
	 * A storage class for illumination data
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected Vector4 direction;
	protected double distance;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public IlluminationData()
	{
		//
	}
	

	/* *********************************************************************************************
	 * Getters/Setter
	 * *********************************************************************************************/
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Vector4 getDirection() {
		return direction;
	}

	public void setDirection(Vector4 direction) {
		this.direction = direction;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
