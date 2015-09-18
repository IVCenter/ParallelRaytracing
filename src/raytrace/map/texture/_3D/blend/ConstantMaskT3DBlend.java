package raytrace.map.texture._3D.blend;

import raytrace.color.Color;
import raytrace.map.texture._3D.Texture3D;

public class ConstantMaskT3DBlend extends Texture3D {
	
	/*
	 * A texture blend for using a texture to mask two other textures
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D firstTexture;
	protected Texture3D secondTexture;
	protected double mask;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ConstantMaskT3DBlend()
	{
		this.firstTexture = null;
		this.secondTexture = null;
		this.mask = 0.5;
	}
	
	public ConstantMaskT3DBlend(Texture3D firstTexture, Texture3D secondTexture, double mask)
	{
		this.firstTexture = firstTexture;
		this.secondTexture = secondTexture;
		this.mask = Math.min(1.0, Math.max(0.0, mask));
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		Color firstColor = firstTexture.evaluate(x, y, z);
		Color secondColor = secondTexture.evaluate(x, y, z);
		return Color.interpolate(firstColor, secondColor, mask);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getFirstTexture() {
		return firstTexture;
	}

	public void setFirstTexture(Texture3D firstTexture) {
		this.firstTexture = firstTexture;
	}

	public Texture3D getSecondTexture() {
		return secondTexture;
	}

	public void setSecondTexture(Texture3D secondTexture) {
		this.secondTexture = secondTexture;
	}

	public double getMask() {
		return mask;
	}

	public void setMask(double mask) {
		this.mask = Math.min(1.0, Math.max(0.0, mask));
	}

}
