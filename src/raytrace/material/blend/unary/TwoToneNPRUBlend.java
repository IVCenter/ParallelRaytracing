package raytrace.material.blend.unary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;
import raytrace.material.Material;

public class TwoToneNPRUBlend extends UnaryBlend {

	/*
	 * A unary blend that replaces all colors under a given intensity with the
	 * desired first color, and all above the threshold with the desired second color
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture shadowTexture;
	protected Texture highlightTexture;
	protected double shadowThreshold;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public TwoToneNPRUBlend(Material material)
	{
		super(material);
		shadowTexture = new Color(0x111111ff);
		highlightTexture = new Color(0xeeeeeeff);
		shadowThreshold = 0.2;
	}
	
	public TwoToneNPRUBlend(Material material, Texture shadowTexture, Texture highlightTexture)
	{
		super(material);
		this.shadowTexture = shadowTexture;
		this.highlightTexture = highlightTexture;
		shadowThreshold = 0.2;
	}
	
	public TwoToneNPRUBlend(Material material, Texture shadowTexture, Texture highlightTexture, double shadowThreshold)
	{
		super(material);
		this.shadowTexture = shadowTexture;
		this.highlightTexture = highlightTexture;
		this.shadowThreshold = shadowThreshold;
	}

	
	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color blend(Color shade, ShadingData data)
	{
		//If the intensity is below the threshold, set it to the shadow color
		if(shade.intensity3() < shadowThreshold)
			shade = shadowTexture.evaluate(data.getIntersectionData());
		else
			shade = highlightTexture.evaluate(data.getIntersectionData());
		
		return shade;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture getShadowTexture() {
		return shadowTexture;
	}

	public void setShadowTexture(Texture shadowTexture) {
		this.shadowTexture = shadowTexture;
	}

	public Texture getHighlightTexture() {
		return highlightTexture;
	}

	public void setHighlightTexture(Texture highlightTexture) {
		this.highlightTexture = highlightTexture;
	}

	public double getShadowThreshold() {
		return shadowThreshold;
	}

	public void setShadowThreshold(double shadowThreshold) {
		this.shadowThreshold = shadowThreshold;
	}

}
