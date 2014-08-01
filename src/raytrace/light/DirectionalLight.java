package raytrace.light;

import math.Ray;
import math.Vector3;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;

public class DirectionalLight extends Light {
	
	/*
	 * A directional light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 direction;
	

	/* *********************************************************************************************
	 * Consructor
	 * *********************************************************************************************/
	public DirectionalLight()
	{
		direction = new Vector3();
	}
	
	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IlluminationData illuminate(ShadingData data, Vector3 point)
	{
		double distance = Double.MAX_VALUE;
		
		IntersectionData shadowData = shadowed(data.getRootScene(), 
				new Ray(point, direction.multiply(-1.0), 0, 0), distance);
		
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
	}

}
