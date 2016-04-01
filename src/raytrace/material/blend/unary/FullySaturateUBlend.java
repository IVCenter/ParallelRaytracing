//package raytrace.material.blend.unary;
//
//import raytrace.color.Color;
//import raytrace.data.ShadingData;
//import raytrace.material.Material;
//
//public class FullySaturateUBlend extends UnaryBlend {
//
//	/*
//	 * A unary blend converting a material shade to full saturated.
//	 */
//	
//	/* *********************************************************************************************
//	 * Constructor
//	 * *********************************************************************************************/
//	public FullySaturateUBlend(Material material)
//	{
//		super(material);
//	}
//	
//	
//	/* *********************************************************************************************
//	 * Blend Override
//	 * *********************************************************************************************/
//	@Override
//	public Color blend(Color shade, ShadingData data)
//	{
//		double[] channels = shade.getChannels();
//		double min = Math.min(Math.min(channels[0], channels[1]), channels[2]);
//		double max = Math.max(Math.max(channels[0], channels[1]), channels[2]);
//		double scale = 1.0 / (max - min);
//		
//		Color saturatedColor = new Color((channels[0] - min) * scale, 
//										 (channels[1] - min) * scale, 
//										 (channels[2] - min) * scale, 
//										 channels[3]);
//		
//		saturatedColor.multiply3M(max);
//		return saturatedColor;
//	}
//
//}
