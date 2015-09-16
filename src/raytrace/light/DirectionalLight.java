package raytrace.light;

import math.Vector3;
import math.ray.Ray;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.scene.Scene;

public class DirectionalLight extends Light {
	
	/*
	 * A directional light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 direction;
	private Vector3 inverseDirection;
	

	/* *********************************************************************************************
	 * Consructor
	 * *********************************************************************************************/
	public DirectionalLight()
	{
		direction = new Vector3();
		inverseDirection = direction.multiply(-1.0);
	}
	
	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IlluminationData illuminate(Scene scene, Vector3 point)
	{
		double distance = Double.POSITIVE_INFINITY;
		
		IntersectionData shadowData = shadowed(scene, 
				new Ray(point, inverseDirection, 0, 0), distance);
		
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

}
