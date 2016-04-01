//package raytrace.material.blend.binary;
//
//import raytrace.color.Color;
//import raytrace.data.ShadingData;
//import raytrace.map.texture.Texture;
//import raytrace.material.Material;
//
//public class PosterMaskBBlend extends BinaryBlend {
//
//	/*
//	 * A binary blend for interpolating between two materials using a texture mask
//	 */
//	
//	/* *********************************************************************************************
//	 * Instance Vars
//	 * *********************************************************************************************/
//	protected Texture mask;
//	protected double level;
//	
//	
//	/* *********************************************************************************************
//	 * Constructor
//	 * *********************************************************************************************/
//	public PosterMaskBBlend(Material firstMaterial, Material secondMaterial)
//	{
//		super(firstMaterial, secondMaterial);
//		this.mask = Color.gray();
//		this.level = 0.5;
//	}
//	
//	public PosterMaskBBlend(Material firstMaterial, Material secondMaterial, Texture mask, double level)
//	{
//		super(firstMaterial, secondMaterial);
//		this.mask = mask;
//		this.level = level;
//	}
//	
//
//	/* *********************************************************************************************
//	 * Material Overrides
//	 * *********************************************************************************************/
//	@Override
//	public Color shade(ShadingData data)
//	{
//		double[] maskColor = mask.evaluate(data.getIntersectionData()).duplicate().clamp3M().getChannels();
//		
//		//If all channels will be from the same material, only evaluate that one.
//		if(maskColor[0] < level && maskColor[1] < level && maskColor[2] < level ||
//				maskColor[0] > level && maskColor[1] > level && maskColor[2] > level)
//		{
//			return maskColor[0] < level ? firstMaterial.shade(data) : secondMaterial.shade(data);
//		}
//		
//		return blend(firstMaterial.shade(data), secondMaterial.shade(data), data);
//	}
//	
//	
//	/* *********************************************************************************************
//	 * Blend Override
//	 * *********************************************************************************************/
//	@Override
//	public Color blend(Color firstShade, Color secondShade, ShadingData data)
//	{
//		double[] firstColor = firstShade.getChannels();
//		double[] secondColor = secondShade.getChannels();
//		double[] maskColor = mask.evaluate(data.getIntersectionData()).duplicate().clamp3M().getChannels();
//		return new Color(maskColor[0] < level ? firstColor[0] : secondColor[0],
//				 		 maskColor[1] < level ? firstColor[1] : secondColor[1],
//				 		 maskColor[2] < level ? firstColor[2] : secondColor[2]);
//	}
//	
//	
//	/* *********************************************************************************************
//	 * Getters/Setters
//	 * *********************************************************************************************/
//	public Texture getMask() {
//		return mask;
//	}
//
//	public void setMask(Texture mask) {
//		this.mask = mask;
//	}
//
//	public double getLevel() {
//		return level;
//	}
//
//	public void setLevel(double level) {
//		this.level = level;
//	}
//}
