package math.ray;

import java.util.Iterator;

import math.Vector3;

public class CircularRayStencil extends RayStencil {
	
	/*
	 * A composite ray stencil in the shape of a circle (cone)
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static final long serialVersionUID = 8147058486789914173L;

	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected StencilIterator iterator;
	
	protected double radius;
	protected int ringCount;
	protected int outerRingSampleCount;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public CircularRayStencil()
	{
		super();
		iterator = new StencilIterator();
	}
	
	public CircularRayStencil(double radius, int ringCount, int outerRingSampleCount)
	{
		this();
		this.radius = radius;
		this.ringCount = ringCount;
		this.outerRingSampleCount = outerRingSampleCount;
	}
	

	/* *********************************************************************************************
	 * Stencil Override
	 * *********************************************************************************************/
	@Override
	public Iterable<Ray> stencil(Ray primaryRay)
	{
		//iterator.reset(primaryRay);
		//return iterator;
		
		StencilIterator iterator = new StencilIterator();
		iterator.reset(primaryRay);
		return iterator;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getRingCount() {
		return ringCount;
	}

	public void setRingCount(int ringCount) {
		this.ringCount = ringCount;
	}

	public int getOuterRingSampleCount() {
		return outerRingSampleCount;
	}

	public void setOuterRingSampleCount(int outerRingSampleCount) {
		this.outerRingSampleCount = outerRingSampleCount;
	}


	/* *********************************************************************************************
	 *  Stencil Ray Iterator
	 * *********************************************************************************************/
	protected class StencilIterator implements Iterator<Ray>, Iterable<Ray>
	{
		/*
		 * An iterator over a set of rays that represent a stencil
		 */

		/* *********************************************************************************************
		 *  Instance Vars
		 * *********************************************************************************************/
		protected Ray primaryRay;
		protected Vector3 origin;
		protected Vector3 direction;
		
		protected Ray ray;
		protected double[] xAxis;
		protected double[] yAxis;
		protected double[] zAxis;
		
		protected int currentRing;
		protected int currentSample;
		protected double currentSampleAngleDelta;
		

		/* *********************************************************************************************
		 *  Constructor
		 * *********************************************************************************************/
		public StencilIterator()
		{
			super();
			ray = new Ray();
		}


		/* *********************************************************************************************
		 *  Iterator Overrides
		 * *********************************************************************************************/
		@Override
		public boolean hasNext()
		{
			return currentRing > 0;// || currentSample > 0;
		}

		@Override
		public Ray next()
		{
			double r = radius * (((double)currentRing) / ringCount);
			double u = r * Math.cos(currentSample * currentSampleAngleDelta);
			double v = r * Math.sin(currentSample * currentSampleAngleDelta);
			
			ray.getDirection().set(zAxis[0] + xAxis[0] * u + yAxis[0] * v, 
								   zAxis[1] + xAxis[1] * u + yAxis[1] * v, 
								   zAxis[2] + xAxis[2] * u + yAxis[2] * v);
			
			ray.getDirection().normalizeM();
			
			//Update counters
			currentSample--;
			if(currentSample == 0)
			{
				currentRing--;
				currentSample = (int)(outerRingSampleCount * (((double)currentRing)/ringCount));
				currentSampleAngleDelta = 2.0 * Math.PI / currentSample;
			}
			
			return ray;
		}

		@Override
		public void remove()
		{
			//Nope
		}
		
		public void reset(Ray primaryRay)
		{
			this.primaryRay = primaryRay;
			this.origin = primaryRay.getOrigin();
			this.direction = primaryRay.getDirection();
			
			ray.setOrigin(origin);
			
			Vector3 xAxisVector = direction.cross(Vector3.positiveYAxis).normalizeM();
			Vector3 yAxisVector = xAxisVector.cross(direction).normalizeM();
			
			zAxis = direction.getArray();
			yAxis = yAxisVector.getArray();
			xAxis = xAxisVector.getArray();
			
			currentSample = outerRingSampleCount;
			currentRing = ringCount;
			currentSampleAngleDelta = 2.0 * Math.PI / outerRingSampleCount;
		}

		@Override
		public Iterator<Ray> iterator()
		{
			return this;
		}
		
	}
}
