//package raytrace.material.blend.unary;
//
//import java.util.Iterator;
//
//import raytrace.color.Color;
//import raytrace.data.ShadingData;
//import raytrace.material.Material;
//import raytrace.material.blend.Blend;
//
//public abstract class UnaryBlend extends Material implements Blend {
//	
//	/*
//	 * A base class for unary blends
//	 */
//	
//	/* *********************************************************************************************
//	 * Instance Vars
//	 * *********************************************************************************************/
//	protected Material material;
//	
//
//	/* *********************************************************************************************
//	 * Cosntructors
//	 * *********************************************************************************************/
//	public UnaryBlend(Material material)
//	{
//		this.material = material;
//	}
//	
//	
//	/* *********************************************************************************************
//	 * Abstract Blend Method
//	 * *********************************************************************************************/
//	public abstract Color blend(Color shade, ShadingData data);
//
//	
//	/* *********************************************************************************************
//	 * Material Overrides
//	 * *********************************************************************************************/
//	@Override
//	public Color shade(ShadingData data)
//	{
//		return blend(material.shade(data), data);
//	}
//
//
//	/* *********************************************************************************************
//	 * Setters/Getters
//	 * *********************************************************************************************/
//	public Material getMaterial() {
//		return material;
//	}
//
//	public void setMaterial(Material material) {
//		this.material = material;
//	}
//	
//	
//	/* *********************************************************************************************
//	 * Iterator Overtide
//	 * *********************************************************************************************/
//	@Override
//	public Iterator<Material> iterator()
//	{
//		return new MaterialIterator();
//	}
//
//	/* *********************************************************************************************
//	 * Private Classes
//	 * *********************************************************************************************/
//	private class MaterialIterator implements Iterator<Material>
//	{	
//		boolean notDone = true;
//		
//		public MaterialIterator() { /**/ }
//		
//		@Override
//		public boolean hasNext()
//		{
//			return notDone;
//		}
//
//		@Override
//		public Material next()
//		{
//			if(notDone) {
//				notDone = false;
//				return material;
//			}
//			return null;
//		}
//
//		@Override
//		public void remove()
//		{
//			//No
//		}
//		
//	}
//}
