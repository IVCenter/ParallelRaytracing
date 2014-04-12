package raytrace.camera;

import java.util.ArrayList;
import java.util.Iterator;

import process.logging.Logger;

import math.CompositeRay;
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
	
	protected ArrayList<Ray> precalculatedRays;
	
	protected int superSamplingLevel = 1;
	protected boolean stratifiedSampling = false;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public PinholeCamera()
	{
		super();
		precalculatedRays = new ArrayList<Ray>();
		//update();
	}
	
	public PinholeCamera(Vector4 position, Vector4 viewingDirection, Vector4 up, double fieldOfView, double pixelWidth, double pixelHeight)
	{
		super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		precalculatedRays = new ArrayList<Ray>();
		//update();
	}
	
	public PinholeCamera(Vector4 position, Vector4 viewingDirection, Vector4 up, double fieldOfView, double pixelWidth, double pixelHeight, int superSamplingLevel)
	{
		super(position, viewingDirection, up, fieldOfView, pixelWidth, pixelHeight);
		this.superSamplingLevel = superSamplingLevel;
		precalculatedRays = new ArrayList<Ray>();
		//update();
	}
	

	/* *********************************************************************************************
	 * Getters/Setter
	 * *********************************************************************************************/
	public int getSuperSamplingLevel() {
		return superSamplingLevel;
	}

	public void setSuperSamplingLevel(int superSamplingLevel) {
		this.superSamplingLevel = superSamplingLevel;
	}

	public boolean isStratifiedSampling() {
		return stratifiedSampling;
	}

	public void setStratifiedSampling(boolean stratifiedSampling) {
		this.stratifiedSampling = stratifiedSampling;
	}

	/* *********************************************************************************************
	 * Iteration
	 * *********************************************************************************************/
	@Override
	public Iterator<Ray> iterator()
	{
		//Create and return a ray iterator
		if(precalculatedRays.isEmpty())
			return new RayIterator();
		else
			return precalculatedRays.iterator();
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
		
		//Buffer rays
		precalculatedRays.clear();
		for(Ray ray : this)
			precalculatedRays.add(ray);
		
		//Since the rays are new change the set ID
		raySetID++;
	}
	
	@Override
	protected void wasModified()
	{
		
	}
	
	//TODO: This is not exactly good design.....
	public void forceUpdate()
	{
		Logger.progress(-8, "Forcing Update");
		update();
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	@Override
	protected Ray getRay(double x, double y)
	{
		//Create the origin vector
		Vector4 orig = new Vector4(position);
		
		//Create a composite ray with enough space for the sampling rays
		CompositeRay cray = null;
		if(superSamplingLevel != 1) {
			cray = new CompositeRay(superSamplingLevel * superSamplingLevel);
			cray.setOrigin(orig);
			cray.setPixelX((int)x);
			cray.setPixelY((int)y);
		}
		
		double samplingDelta = 1.0/(double)superSamplingLevel;
		
		double pw;
		double ph;
		double woffset, hoffset;
		Vector4 dir;
		
		for(int u = 0; u < superSamplingLevel; ++u)
		{
			for(int v = 0; v < superSamplingLevel; ++v)
			{
				//Calculate sampling offsets
				woffset = u * samplingDelta + (stratifiedSampling ? Math.random()*samplingDelta : samplingDelta/2.0);
				hoffset = v * samplingDelta + (stratifiedSampling ? Math.random()*samplingDelta : samplingDelta/2.0);
				
				
				//Pre-calculate the axis weights
				pw = (((x+woffset)/pixelWidth) - 0.5) * imagePlaneWidth;
				ph = (((y+hoffset)/pixelHeight) - 0.5) * imagePlaneHeight;
				
				//Create the direction vector
				//TODO: Might be slow
				dir = viewingDirection.add3(cameraX.multiply3(pw)).add3(cameraY.multiply3(ph)).normalize3();
				
				if(superSamplingLevel == 1)
					return new Ray(orig, dir, (int)x, (int)y);
				else
					cray.addRay(new Ray(orig, dir, (int)x, (int)y));
			}
		}
		
		
		return cray;
	}
	
	public void setVerticalFieldOfView(double fov)
	{
		Logger.progress(-8, "PreFOV[" + fieldOfView + "]");
		fieldOfView = 2.0 * Math.atan((imagePlaneWidth/imagePlaneHeight) * Math.tan(fov/2.0));
		Logger.progress(-8, "PreFOV[" + fieldOfView + "]");
		wasModified();
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
