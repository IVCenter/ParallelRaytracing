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
		return new RayIterator();
	}

	
	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	@Override
	protected void update()
	{
		//Update camera axes
		cameraX = viewingDirection.cross3(up).normalize3();
		cameraY = viewingDirection.cross3(cameraX).normalize3();
		
		//Update image plane ratio
		imagePlaneRatio = pixelWidth / pixelHeight;
		
		//Update the image plane values
		imagePlaneWidth = 2*Math.tan(fieldOfView/2.0);
		imagePlaneHeight = imagePlaneWidth / imagePlaneRatio;
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	protected Ray getRay(double x, double y)
	{
		//Create the origin vector
		Vector4 orig = new Vector4(position);
		
		//Pre-calculate the axis weights
		double pw = ((x/pixelWidth) - 0.5) * imagePlaneWidth;
		double ph = ((y/pixelHeight) - 0.5) * imagePlaneHeight;
		
		//Create the direction vector
		Vector4 dir = new Vector4(viewingDirection.x + pw*cameraX.x + ph*cameraY.x,
								  viewingDirection.y + pw*cameraX.y + ph*cameraY.y,
								  viewingDirection.z + pw*cameraX.z + ph*cameraY.z,
								  0.0);
		
		return new Ray(orig, dir);
	}
	
	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class RayIterator implements Iterator<Ray>
	{

		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		private double currentPixelX;
		private double currentPixelY;

		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public RayIterator()
		{
			currentPixelX = 0;
			currentPixelY = 0;
		}

		/* *********************************************************************************************
		 * Iterator Methods
		 * *********************************************************************************************/

		@Override
		public boolean hasNext()
		{
			return currentPixelX < pixelWidth && currentPixelY < pixelHeight;
		}

		@Override
		public Ray next()
		{
			//Get the next ray
			Ray r = getRay(currentPixelX, currentPixelY);
			
			//Increment the counters
			++currentPixelX;
			if(currentPixelX >= pixelWidth) {
				currentPixelX = 0;
				++currentPixelY;
			}
			
			return r;
		}

		@Override
		public void remove()
		{
			//No
		}
		
	}
}
