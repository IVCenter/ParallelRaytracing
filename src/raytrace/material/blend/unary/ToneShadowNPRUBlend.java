package raytrace.material.blend.unary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;
import raytrace.material.Material;

public class ToneShadowNPRUBlend extends UnaryBlend {

	/*
	 * A unary blend that replaces all colors under a given intensity with the
	 * desired color
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture texture;
	protected double shadowThreshold;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ToneShadowNPRUBlend(Material material)
	{
		super(material);
		texture = new Color(0x111111ff);
		shadowThreshold = 0.2;
	}
	
	public ToneShadowNPRUBlend(Material material, Texture texture)
	{
		super(material);
		this.texture = texture;
		shadowThreshold = 0.2;
	}
	
	public ToneShadowNPRUBlend(Material material, Texture texture, double shadowThreshold)
	{
		super(material);
		this.texture = texture;
		this.shadowThreshold = shadowThreshold;
	}

	
	/* *********************************************************************************************
	 * Unary Blend Overrides
	 * *********************************************************************************************/
	@Override
	public Color blend(Color shade, ShadingData data)
	{
		//If the intensity is below the threshold, set it to the shadow color
		if(shade.intensity3() < shadowThreshold)
			shade = texture.evaluate(data.getIntersectionData());;
		
		return shade;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public double getShadowThreshold() {
		return shadowThreshold;
	}

	public void setShadowThreshold(double shadowThreshold) {
		this.shadowThreshold = shadowThreshold;
	}

}
