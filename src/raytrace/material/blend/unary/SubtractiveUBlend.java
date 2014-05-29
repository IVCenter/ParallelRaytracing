package raytrace.material.blend.unary;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;
import raytrace.material.Material;

public class SubtractiveUBlend extends UnaryBlend {

	/*
	 * A unary blend that subtracts a materials shade color with a texture color
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture texture;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SubtractiveUBlend(Material material)
	{
		super(material);
		texture = Color.white();
	}
	
	public SubtractiveUBlend(Material material, Texture texture)
	{
		super(material);
		this.texture = texture;
	}
	
	
	/* *********************************************************************************************
	 * Blend Override
	 * *********************************************************************************************/
	@Override
	public Color blend(Color shade, ShadingData data)
	{
		Color texColor = texture.evaluate(data.getIntersectionData());
		return shade.subtract3(texColor);
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

}
