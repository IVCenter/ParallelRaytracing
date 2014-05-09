package math;

import java.io.Serializable;
import java.util.Iterator;

public class Ray implements Iterable<Ray>, Serializable{
	
	/*
	 * A class that represents a Ray in 3D space
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 origin;
	protected Vector4 direction;
	protected int pixelX;
	protected int pixelY;
	
	protected RayIterator rayIterator;
	
	protected double randomValue;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Ray()
	{
		origin = new Vector4();
		direction = new Vector4();
		pixelX = 0;
		pixelY = 0;
		
		rayIterator = new RayIterator(this);
	}
	
	//We assume that direction is normalized
	public Ray(Vector4 origin, Vector4 direction, int pixelX, int pixelY)
	{
		this.origin = origin;
		this.direction = direction;
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		
		rayIterator = new RayIterator(this);
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
	
	public double getRandomValue() {
		return randomValue;
	}

	public void setRandomValue(double randomValue) {
		this.randomValue = randomValue;
	}

	/* *********************************************************************************************
	 * Iteration Overrides
	 * *********************************************************************************************/
	@Override
	public Iterator<Ray> iterator()
	{
		rayIterator.reset();
		return rayIterator;
	}

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class RayIterator implements Iterator<Ray>, Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		boolean notDone = true;
		Ray ray;
		
		public RayIterator(Ray ray)
		{
			this.ray = ray;
		}
		
		@Override
		public boolean hasNext()
		{
			return notDone;
		}

		@Override
		public Ray next()
		{
			if(notDone) {
				notDone = false;
				return ray;
			}
			return null;
		}

		@Override
		public void remove()
		{
			//No
		}
		
		public void reset()
		{
			notDone = true;
		}
		
	}
	
}
