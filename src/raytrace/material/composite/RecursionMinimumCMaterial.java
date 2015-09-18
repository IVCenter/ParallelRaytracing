package raytrace.material.composite;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.material.ColorEmissionMaterial;
import raytrace.material.Material;

@Deprecated //Is not yet supported by the new Materials interface
public class RecursionMinimumCMaterial extends CompositeMaterial {

	/*
	 * A composite material that returns black if the recursion level is below the given minimum
	 * 
	 * Useful for having global lighting without showing the sky box
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected int recursionMinimum;
	protected Material belowMinimumMaterial;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public RecursionMinimumCMaterial(Material material)
	{
		super(material);
		this.recursionMinimum = 0;
		this.belowMinimumMaterial = new ColorEmissionMaterial(Color.black());
		
		this.affectedByLightSources = true;
		this.globallyIlluminated = true;
		this.emitsLight = true;
	}
	
	public RecursionMinimumCMaterial(Material material, int recursionMinimum)
	{
		this(material);
		this.recursionMinimum = recursionMinimum;
	}
	
	public RecursionMinimumCMaterial(Material material, Material belowMinimumMaterial, int recursionMinimum)
	{
		this(material, recursionMinimum);
		this.belowMinimumMaterial = belowMinimumMaterial;
	}


	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		//RecursionDepth is not yet available
		//if(recursionDepth < recursionMinimum)
		//	return belowMinimumMaterial.sample(idata, rdata);
		
		return material.sample(idata, rdata);
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		//RecursionDepth is not yet available
		//if(recursionDepth < recursionMinimum)
		//	return belowMinimumMaterial.evaluateDirectLight(idata, rdata, light, lightDirection);
		
		return material.evaluateDirectLight(idata, rdata, light, lightDirection);
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		//RecursionDepth is not yet available
		//if(recursionDepth < recursionMinimum)
		//	return belowMinimumMaterial.evaluateSampledLight(idata, rdata, light, sample);
		
		return material.evaluateSampledLight(idata, rdata, light, sample);
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		//RecursionDepth is not yet available
		//if(recursionDepth < recursionMinimum)
		//	return belowMinimumMaterial.evaluateEmission(idata, rdata);
		
		return material.evaluateEmission(idata, rdata);
	}

	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int getRecursionMinimum() {
		return recursionMinimum;
	}

	public void setRecursionMinimum(int recursionMinimum) {
		this.recursionMinimum = recursionMinimum;
	}

}
