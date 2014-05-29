package raytrace.map.texture._3D.blend;

import raytrace.color.Color;
import raytrace.map.texture._3D.Texture3D;

public class MaskT3DBlend extends Texture3D {
	
	/*
	 * A texture blend for using a texture to mask two other textures
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D firstTexture;
	protected Texture3D secondTexture;
	protected Texture3D mask;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MaskT3DBlend()
	{
		this.firstTexture = null;
		this.secondTexture = null;
		this.mask = null;
	}
	
	public MaskT3DBlend(Texture3D firstTexture, Texture3D secondTexture, Texture3D mask)
	{
		this.firstTexture = firstTexture;
		this.secondTexture = secondTexture;
		this.mask = mask;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double[] firstColor = firstTexture.evaluate(x, y, z).getChannels();
		double[] secondColor = secondTexture.evaluate(x, y, z).getChannels();
		double[] maskColor = mask.evaluate(x, y, z).duplicate().clamp3M().getChannels();
		return new Color(firstColor[0] * maskColor[0] + secondColor[0] * (1.0 - maskColor[0]),
						 firstColor[1] * maskColor[1] + secondColor[1] * (1.0 - maskColor[1]),
						 firstColor[2] * maskColor[2] + secondColor[2] * (1.0 - maskColor[2]));
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

	public Texture3D getMask() {
		return mask;
	}

	public void setMask(Texture3D mask) {
		this.mask = mask;
	}

}
