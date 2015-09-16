package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class ColorMaterial extends Material {
	
	/*
	 * An implementation of a material that is a diffuse color
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture colorTexture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ColorMaterial(Texture colorTexture)
	{
		this.colorTexture = colorTexture;
		this.globallyIlluminated = false;
		this.affectedByLightSources = false;
		this.emitsLight = true;
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
		return colorTexture.evaluate(idata);
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture getColorTexture() {
		return colorTexture;
	}

	public void setColorTexture(Texture colorTexture) {
		this.colorTexture = colorTexture;
	}

}
