//package raytrace.material.blend.binary;
//
//import raytrace.color.Color;
//import raytrace.data.ShadingData;
//import raytrace.map.texture.Texture;
//import raytrace.material.Material;
//
//public class FastIntensityMaskBBlend extends BinaryBlend {
//
//	/*
//	 * A (fast) binary blend for interpolating between two materials using a texture mask
//	 */
//	
//	/* *********************************************************************************************
//	 * Instance Vars
//	 * *********************************************************************************************/
//	protected Texture mask;
//	
//	
//	/* *********************************************************************************************
//	 * Constructor
//	 * *********************************************************************************************/
//	public FastIntensityMaskBBlend(Material firstMaterial, Material secondMaterial, Texture mask)
//	{
//		super(firstMaterial, secondMaterial);
//		this.mask = mask;
//	}
//
//	
//	/* *********************************************************************************************
//	 * Material Overrides
//	 * *********************************************************************************************/
//	@Override
//	public Color shade(ShadingData data)
//	{
//		double rand = Math.random();
//		double maskValue = mask.evaluate(data.getIntersectionData()).intensity3();
//		if(rand < maskValue) {
//			return secondMaterial.shade(data);
//		}else{
//			return firstMaterial.shade(data);
//		}
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
//		double maskValue = mask.evaluate(data.getIntersectionData()).intensity3();
//		return new Color(secondColor[0] * maskValue + firstColor[0] * (1.0 - maskValue),
//						 secondColor[1] * maskValue + firstColor[1] * (1.0 - maskValue),
//						 secondColor[2] * maskValue + firstColor[2] * (1.0 - maskValue));
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
//}
