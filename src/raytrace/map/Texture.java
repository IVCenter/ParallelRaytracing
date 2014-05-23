package raytrace.map;

import raytrace.color.Color;
import raytrace.data.IntersectionData;

public interface Texture {
	
	/*
	 * A base class for textures
	 */

	/* *********************************************************************************************
	 * Interface Methods
	 * *********************************************************************************************/
	public abstract Color evaluate(IntersectionData data);

}
