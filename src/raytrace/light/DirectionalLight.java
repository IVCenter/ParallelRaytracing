package raytrace.light;

import math.Vector4;
import raytrace.data.IlluminationData;

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
	public IlluminationData illuminate(Vector4 point)
	{
		IlluminationData ildata = new IlluminationData();
		
		ildata.setColor(color.multiply3( intensity ));
		ildata.setDirection(direction);
		ildata.setDistance(Double.MAX_VALUE);
		
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
