package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.Texture;

public class FresnelMetalMaterial extends Material {
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture tintTexture;
	double refractiveReal = 2.485;//Steel
	double refractiveImaginary = 3.433;//Steel
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public FresnelMetalMaterial(Texture tintTexture, double refractiveReal, double refractiveImaginary)
	{
		this.tintTexture = tintTexture;
		this.refractiveReal = refractiveReal;
		this.refractiveImaginary = refractiveImaginary;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		//Storage for the result color
		Color shade = new Color(0x000000ff);
		
		//Get the material color from the texture
		Color tint = tintTexture.evaluate(data.getIntersectionData());
		
		Vector4 point = data.getIntersectionData().getPoint();
		Vector4 normal = data.getIntersectionData().getNormal();
		IlluminationData ildata;
		
		//Diffuse lighting and shadows
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		//Calculate percent reflective
		double NdotD = normal.dot3(data.getRay().getDirection());
		double NdotD2 = NdotD * NdotD;
		double riReal2 = refractiveReal * refractiveReal;
		double riImag2 = refractiveImaginary * refractiveImaginary;
		double ri2Sum = (riReal2 + riImag2);
		
		double rpLeft = ri2Sum * NdotD2;
		double rpRight = 2.0 * refractiveReal * NdotD;
		
		double reflParallel = (rpLeft + rpRight + 1.0) / (rpLeft - rpRight + 1.0);
		double reflPerp = (ri2Sum + NdotD2 + rpRight) / (ri2Sum + NdotD2 - rpRight);

		double reflectivePercent = 0.5 * (reflParallel * reflParallel + reflPerp * reflPerp);

		//If reflective, go divin'
		Color rflectColor = new Color();
		if(reflectivePercent != 0.0 && data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			rflectColor = reflect(data, point, normal, data.getRefractiveIndex());
		}
		

		Color reflectiveColor = rflectColor.multiply3(
				tint.mixWithWhite(1.0 - reflectivePercent, reflectivePercent));
		return reflectiveColor;
		
	}

}