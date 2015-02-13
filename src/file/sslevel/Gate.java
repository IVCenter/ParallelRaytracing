package file.sslevel;

import math.Vector3;

public class Gate {
	
	/*
	 * A gate for a space slalom level
	 */
	
	/* *********************************************************************************************
	 * Instnace Vars
	 * *********************************************************************************************/
	protected Vector3 center;
	protected Vector3 right;
	protected Vector3 up;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Gate()
	{
		//
	}
	
	public Gate(Vector3 center, Vector3 right, Vector3 up)
	{
		this.center = center;
		this.right = right;
		this.up = up;
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Vector3 getCenter() {
		return center;
	}

	public void setCenter(Vector3 center) {
		this.center = center;
	}

	public Vector3 getRight() {
		return right;
	}

	public void setRight(Vector3 right) {
		this.right = right;
	}

	public Vector3 getUp() {
		return up;
	}

	public void setUp(Vector3 up) {
		this.up = up;
	}
	
}
