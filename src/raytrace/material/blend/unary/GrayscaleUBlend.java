//package raytrace.material.blend.unary;
//
//import raytrace.color.Color;
//import raytrace.data.ShadingData;
//import raytrace.material.Material;
//
//public class GrayscaleUBlend extends UnaryBlend {
//
//	/*
//	 * A unary blend converting a material shade to grayscale.
//	 */
//	
//	/* *********************************************************************************************
//	 * Constructor
//	 * *********************************************************************************************/
//	public GrayscaleUBlend(Material material)
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
//		double avg = (channels[0] + channels[1] + channels[2]) / 3.0;
//		return new Color(avg, avg, avg, channels[3]);
//	}
//
//}
