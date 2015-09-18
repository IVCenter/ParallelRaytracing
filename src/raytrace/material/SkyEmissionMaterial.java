package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class SkyEmissionMaterial extends Material {
	
	/*
	 * An implementation of a light emitting texture material for use with sky boxes
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture colorTexture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SkyEmissionMaterial(Texture colorTexture)
	{
		this.colorTexture = colorTexture;
		
		this.affectedByLightSources = false;
		this.globallyIlluminated = false;
		this.castsShadows = false;
		this.receivesShadows = false;
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
		Vector3 rdir = rdata.getRay().getDirection().normalize();
		
		//If there is no idata supplied (as is the case with most sky intersections)
		if(idata == null)
		{
			idata = new IntersectionData();
			idata.setLocalPoint(
					new Vector3(rdir.add(new Vector3(0, 1, 0)).multiplyM(0, 0.5, 0)));
			idata.setTexcoord(new Vector3(
					0, 
					0.5 * (rdir.dot(Vector3.positiveYAxis) + 1.0), 
					0));
		}
		
		return colorTexture.evaluate(idata);
	}

}