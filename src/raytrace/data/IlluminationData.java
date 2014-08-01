package raytrace.data;

import math.Vector3;
import raytrace.color.Color;

public class IlluminationData {
	
	/*
	 * A storage class for illumination data
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	protected Vector3 direction;
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

	public Vector3 getDirection() {
		return direction;
	}

	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
