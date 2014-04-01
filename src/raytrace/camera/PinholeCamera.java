package raytrace.camera;

import java.util.Iterator;

import math.Ray;
import math.Vector4;

public class PinholeCamera extends Camera {


	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 cameraX;
	protected Vector4 cameraY;
	
	protected double imagePlaneWidth;
	protected double imagePlaneHeight;
	
	protected double imagePlaneRatio;
	
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public PinholeCamera()
	{
		//
	}


	/* *********************************************************************************************
	 * Iteration
	 * *********************************************************************************************/
	@Override
	public Iterator<Ray> iterator()
	{
		//Create and return a ray iterator
		return null;
	}

	
	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	@Override
	protected void update()
	{
		//Update pre-calculated values
		cameraX = viewingDirection.cross(up).normalize;
		cameraY = viewingDirection.cross(cameraX).normalize;
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	protected Vector4 getRay(double x, double y)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class RayIterator implements Iterator<Ray>
	{

		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		

		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public RayIterator()
		{
			
		}

		/* *********************************************************************************************
		 * Iterator Methods
		 * *********************************************************************************************/

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Ray next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
