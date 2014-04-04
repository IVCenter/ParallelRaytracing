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
	protected int pixelX;
	protected int pixelY;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Ray()
	{
		origin = new Vector4();
		direction = new Vector4();
		pixelX = 0;
		pixelY = 0;
	}
	
	//We assume that direction is normalized
	public Ray(Vector4 origin, Vector4 direction, int pixelX, int pixelY)
	{
		this.origin = origin;
		this.direction = direction;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
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

	public int getPixelX() {
		return pixelX;
	}

	public void setPixelX(int pixelX) {
		this.pixelX = pixelX;
	}

	public int getPixelY() {
		return pixelY;
	}

	public void setPixelY(int pixelY) {
		this.pixelY = pixelY;
	}
	
	
	
}
