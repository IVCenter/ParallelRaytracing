package raytrace.light;

import math.Ray;
import math.Vector4;
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
	protected Vector4 direction;
	

	/* *********************************************************************************************
	 * Consructor
	 * *********************************************************************************************/
	public DirectionalLight()
	{
		direction = new Vector4();
	}
	
	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IlluminationData illuminate(ShadingData data, Vector4 point)
	{
		double distance = Double.MAX_VALUE;
		
		IntersectionData shadowData = shadowed(data.getRootScene(), 
				new Ray(point, direction.multiply3(-1.0), 0, 0), distance);
		
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
	public Vector4 getDirection() {
		return direction;
	}

	public void setDirection(Vector4 direction) {
		this.direction = direction.normalize3();
	}

}
