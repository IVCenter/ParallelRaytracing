package raytrace.light;

import math.Vector3;
import math.ray.Ray;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;

public class SoftDirectionalLight extends Light {
	
	/*
	 * A directional light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 direction;
	protected double softness;
	
	private Vector3 inverseDirection;
	

	/* *********************************************************************************************
	 * Consructor
	 * *********************************************************************************************/
	public SoftDirectionalLight()
	{
		direction = new Vector3();
		softness = 0.1;
		inverseDirection = direction.multiply(-1.0);
	}
	
	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IlluminationData illuminate(ShadingData data, Vector3 point)
	{
		double distance = Double.MAX_VALUE;
		
		Vector3 sampleDirection = Vector3.uniformConeSample(inverseDirection, softness * Math.PI * 0.5);
		
		IntersectionData shadowData = shadowed(data.getRootScene(), 
				new Ray(point, sampleDirection, 0, 0), distance);
		
		double blocked = shadowData == null ? 1.0 : 0.0;

		IlluminationData ildata = new IlluminationData();
		
		ildata.setColor(color.multiply3( intensity * blocked ));
		ildata.setDirection(direction);
		ildata.setDistance(distance);
				
		return ildata;
	}

	
	/* *********************************************************************************************
	 * Gettrs/Setter
	 * *********************************************************************************************/
	public Vector3 getDirection() {
		return direction;
	}

	public void setDirection(Vector3 direction) {
		this.direction = direction.normalizeM();
		this.inverseDirection = direction.multiply(-1.0);
	}

	public double getSoftness() {
		return softness;
	}

	public void setSoftness(double softness) {
		this.softness = softness;
	}
	
}
