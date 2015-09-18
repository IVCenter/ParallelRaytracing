package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;

public class NormalAsColorMaterial extends Material {
	
	/*
	 * An implementation of a material that emits light equal to the surface normal at the intersection point
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
		this.globallyIlluminated = false;
		this.affectedByLightSources = false;
		this.emitsLight = true;
	}
	

	public NormalAsColorMaterial(double multiplier)
	{
		this();
		this.multiplier = multiplier;
	}
	

	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		return null;
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		return Color.black();
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		return Color.black();
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		Vector3 normal = idata.getNormal();
		Color c = (new Color(normal.get(0) * 0.5 + 0.5, normal.get(1) * 0.5 + 0.5, normal.get(2) * 0.5 + 0.5)).multiply3M(multiplier);
		return c;
	}

}
