package raytrace.material.blend.binary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;
import raytrace.material.Material;

public class TextureMaskBBlend extends BinaryBlend {

	/*
	 * A binary blend for interpolating between two materials using a texture mask
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture mask;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public TextureMaskBBlend(Material firstMaterial, Material secondMaterial, Texture mask)
	{
		super(firstMaterial, secondMaterial);
		this.mask = mask;
	}
	
	
	/* *********************************************************************************************
	 * Blend Override
	 * *********************************************************************************************/
	@Override
	public Color blend(Color firstShade, Color secondShade, ShadingData data)
	{
		double[] firstColor = firstShade.getChannels();
		double[] secondColor = secondShade.getChannels();
		double[] maskColor = mask.evaluate(data.getIntersectionData()).duplicate().clamp3M().getChannels();
		return new Color(secondColor[0] * maskColor[0] + firstColor[0] * (1.0 - maskColor[0]),
						 secondColor[1] * maskColor[1] + firstColor[1] * (1.0 - maskColor[1]),
						 secondColor[2] * maskColor[2] + firstColor[2] * (1.0 - maskColor[2]));
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture getMask() {
		return mask;
	}

	public void setMask(Texture mask) {
		this.mask = mask;
	}
}
