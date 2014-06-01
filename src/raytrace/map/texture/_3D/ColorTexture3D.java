package raytrace.map.texture._3D;

import raytrace.color.Color;

public class ColorTexture3D extends Texture3D {
	
	/*
	 * A wrapper for converting colors to 3D textures
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color color;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ColorTexture3D()
	{
		this.color = Color.black();
	}
	
	public ColorTexture3D(Color color)
	{
		this.color = color;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		return color;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
