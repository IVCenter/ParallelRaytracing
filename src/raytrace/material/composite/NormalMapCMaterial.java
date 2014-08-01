package raytrace.material.composite;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.ShadingData;
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
	public NormalMapCMaterial(Material material)
	{
		super(material);
		normalMap = new FlatNormalMap();
	}
	
	public NormalMapCMaterial(Material material, NormalMap normalMap)
	{
		super(material);
		this.normalMap = normalMap;
	}

	
	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		Vector4 normal = normalMap.evaluate(data.getIntersectionData());
		
		//Store the old normal
		Vector4 oldNormal = data.getIntersectionData().getNormal().normalize3M();
		
		//Set the normal to the new one
		data.getIntersectionData().setNormal(normal);
		
		//Shade
		Color shade = material.shade(data);
		
		//Put the old normal back
		data.getIntersectionData().setNormal(oldNormal);
		return shade;
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
