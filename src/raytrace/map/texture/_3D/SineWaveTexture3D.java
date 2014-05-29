package raytrace.map.texture._3D;

import raytrace.color.Color;

public class SineWaveTexture3D extends Texture3D {
	
	/*
	 * A sine wave texture that returns gray scale colors on evaluation
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color firstColor;
	protected Color secondColor;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SineWaveTexture3D()
	{
		this.firstColor = Color.black();
		this.secondColor = Color.white();
	}
	
	public SineWaveTexture3D(Color firstColor, Color secondColor)
	{
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double t = 0.5 * (Math.sin(x) + 1.0);
		return firstColor.interpolate(firstColor, secondColor, t);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Color getFirstColor() {
		return firstColor;
	}

	public void setFirstColor(Color firstColor) {
		this.firstColor = firstColor;
	}

	public Color getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(Color secondColor) {
		this.secondColor = secondColor;
	}

}
