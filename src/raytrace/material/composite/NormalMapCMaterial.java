package raytrace.material.composite;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.normal.FlatNormalMap;
import raytrace.map.normal.NormalMap;
import raytrace.material.Material;

public class NormalMapCMaterial extends CompositeMaterial {

	/*
	 * A composite material that modifies the normal of an intersection data
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected NormalMap normalMap;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NormalMapCMaterial(Material material, NormalMap normalMap)
	{
		super(material);
		this.normalMap = normalMap;
		
		this.affectedByLightSources = true;
		this.globallyIlluminated = true;
		this.emitsLight = true;
	}
	
	public NormalMapCMaterial(Material material)
	{
		this(material, new FlatNormalMap());
	}
	

	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 oldNormal = modifyNormal(idata);
		
		RayData sample = material.sample(idata, rdata);
		
		restoreNormal(idata, oldNormal);
		
		return sample;
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		Vector3 oldNormal = modifyNormal(idata);
		
		Color result = material.evaluateDirectLight(idata, rdata, light, lightDirection);
		
		restoreNormal(idata, oldNormal);
		
		return result;
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		Vector3 oldNormal = modifyNormal(idata);
		
		Color result = material.evaluateSampledLight(idata, rdata, light, sample);
		
		restoreNormal(idata, oldNormal);
		
		return result;
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		Vector3 oldNormal = modifyNormal(idata);
		
		Color result = material.evaluateEmission(idata, rdata);
		
		restoreNormal(idata, oldNormal);
		
		return result;
	}


	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected Vector3 modifyNormal(IntersectionData idata)
	{
		Vector3 normal = normalMap.evaluate(idata);
		
		//Store the old normal
		Vector3 oldNormal = idata.getNormal().normalizeM();
		
		//Set the normal to the new one
		idata.setNormal(normal);
		
		return oldNormal;
	}
	
	protected void restoreNormal(IntersectionData idata, Vector3 normal)
	{
		idata.setNormal(normal);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public NormalMap getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(NormalMap normalMap) {
		this.normalMap = normalMap;
	}

}
