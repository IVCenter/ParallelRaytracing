package raytrace.map.texture._3D.blend;

import raytrace.color.Color;
import raytrace.map.texture._3D.Texture3D;

public class OneMinusT3DBlend extends Texture3D {
	
	/*
	 * A simplex noise texture that returns gray scale colors on evaluation
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D texture;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public OneMinusT3DBlend()
	{
		this.texture = null;
	}
	
	public OneMinusT3DBlend(Texture3D texture)
	{
		this.texture = texture;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double[] color = texture.evaluate(x, y, z).getChannels();
		return new Color(1.0 - color[0], 1.0 - color[1], 1.0 - color[2], color[3]);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getTexture() {
		return texture;
	}

	public void setTexture(Texture3D texture) {
		this.texture = texture;
	}

}
