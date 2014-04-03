package math;

public class Ray {
	
	/*
	 * A class that represents a Ray in 3D space
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 origin;
	protected Vector4 direction;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Ray()
	{
		origin = new Vector4();
		direction = new Vector4();
	}
	
	//We assume that direction is normalized
	public Ray(Vector4 origin, Vector4 direction)
	{
		this.origin = origin;
		this.direction = direction;
	}
	

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public Vector4 evaluateAtTime(double t)
	{
		return new Vector4(origin.addMultiRight3(direction, t));
	}
	
	

	/* *********************************************************************************************
	 * Setters/Getters
	 * *********************************************************************************************/
	public Vector4 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector4 origin) {
		this.origin = origin;
	}

	public Vector4 getDirection() {
		return direction;
	}

	public void setDirection(Vector4 direction) {
		this.direction = direction;
	}
	
	
}
