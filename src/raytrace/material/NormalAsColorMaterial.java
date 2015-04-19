package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.ShadingData;

public class NormalAsColorMaterial extends Material {
	
	/*
	 * An implementation of a material that is a diffuse color
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double multiplier;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NormalAsColorMaterial()
	{
		this.multiplier = 1.0;
	}
	

	public NormalAsColorMaterial(double multiplier)
	{
		this.multiplier = multiplier;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Vector3 normal = data.getIntersectionData().getNormal();
		Color c = (new Color(normal.get(0) * 0.5 + 0.5, normal.get(1) * 0.5 + 0.5, normal.get(2) * 0.5 + 0.5)).multiply3M(multiplier);
		return c;
	}

}
