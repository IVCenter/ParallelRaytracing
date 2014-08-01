package raytrace.light;

import math.Ray;
import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;

public class FourPointAreaLight extends Light {
	
	/*
	 * A basic four point quad area light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//Winds counter-clockwise
	protected Vector3[] points = new Vector3[4];
	
	protected int sampleCount = 10;
	
	private Vector3 interpVect = new Vector3();
	
	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/

	@Override
	public IlluminationData illuminate(ShadingData data, Vector3 point)
	{
		IlluminationData ildata = new IlluminationData();
		Vector3 toPoint = point.subtract(points[0]);
		Color totalLight = new Color();
		
		double toPointMagSqrd = toPoint.magnitudeSqrd();
		double distance = Math.sqrt(toPointMagSqrd);
		double attenuation = constantAttenuation +
				 			 linearAttenuation * distance +
				 			 quadraticAttenuation * toPointMagSqrd;
		
		IntersectionData shadowData = null;
		Ray newRay = new Ray();
		
		for(int i = 0; i < sampleCount; i++)
		{
			interpolate(Math.random(), Math.random());
			toPoint = point.subtract(interpVect);

			toPointMagSqrd = toPoint.magnitudeSqrd();
			distance = Math.sqrt(toPointMagSqrd);
			
			newRay.setDirection(toPoint.multiply(-1.0));
			newRay.setOrigin(point);
			shadowData = shadowed(data.getRootScene(), newRay, distance);
			
			if(shadowData == null)
				continue;
			
			attenuation = constantAttenuation +
								 linearAttenuation * distance +
								 quadraticAttenuation * toPointMagSqrd;
			
			totalLight.add3M(color.multiply3( intensity * ( 1.0 / attenuation)));
		}
		
		totalLight.multiply3M(1.0 / (double)sampleCount);
		
		
		ildata.setColor(totalLight.multiply3M(1.0 / (double)sampleCount));
		ildata.setDirection(toPoint.normalizeM());
		ildata.setDistance(distance);
		
		return ildata;
	}

	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected void interpolate(double x, double y)
	{
		double[] v0 = points[0].getArray();
		double[] v1 = points[1].getArray();
		double[] v2 = points[2].getArray();
		double[] v3 = points[3].getArray();
		
		double[] a0 = {v0[0] + (v3[0] - v0[0]) * x,
					   v0[1] + (v3[1] - v0[1]) * x,
					   v0[2] + (v3[2] - v0[2]) * x};
		
		double[] a1 = {v1[0] + (v2[0] - v1[0]) * x,
				   	   v1[1] + (v2[1] - v1[1]) * x,
				   	   v1[2] + (v2[2] - v1[2]) * x};
		
		interpVect.set(a0[0] + (a1[0] - a0[0]) * y,
					   a0[1] + (a1[1] - a0[1]) * y,
					   a0[2] + (a1[2] - a0[2]) * y);
	}
	

	/* *********************************************************************************************
	 * Getters/Setter
	 * *********************************************************************************************/
	public Vector3[] getPoints() {
		return points;
	}

	public void setPoints(Vector3[] points) {
		this.points = points;
	}

	public int getSampleCount() {
		return sampleCount;
	}

	public void setSampleCount(int sampleCount) {
		this.sampleCount = sampleCount;
	}
}