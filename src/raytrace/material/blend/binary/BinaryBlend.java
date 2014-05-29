package raytrace.material.blend.binary;

import java.util.Iterator;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;
import raytrace.material.blend.Blend;

public abstract class BinaryBlend extends Material implements Blend {
	
	/*
	 * A base class for binary blends
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Material firstMaterial;
	protected Material secondMaterial;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public BinaryBlend(Material firstMaterial, Material secondMaterial)
	{
		this.firstMaterial = firstMaterial;
		this.secondMaterial = secondMaterial;
	}
	
	
	/* *********************************************************************************************
	 * Abstract Blend Method
	 * *********************************************************************************************/
	public abstract Color blend(Color firstShade, Color secondShade, ShadingData data);


	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		return blend(firstMaterial.shade(data), secondMaterial.shade(data), data);
	}


	/* *********************************************************************************************
	 * Setters/Getters
	 * *********************************************************************************************/	
	public Material getFirstMaterial() {
		return firstMaterial;
	}

	public void setFirstMaterial(Material firstMaterial) {
		this.firstMaterial = firstMaterial;
	}

	public Material getSecondMaterial() {
		return secondMaterial;
	}

	public void setSecondMaterial(Material secondMaterial) {
		this.secondMaterial = secondMaterial;
	}
	
	
	/* *********************************************************************************************
	 * Iterator Overtide
	 * *********************************************************************************************/
	@Override
	public Iterator<Material> iterator()
	{
		return new MaterialIterator();
	}

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class MaterialIterator implements Iterator<Material>
	{	
		int materialIndex = 0;
		
		public MaterialIterator() { /**/ }
		
		@Override
		public boolean hasNext()
		{
			return materialIndex < 2;
		}

		@Override
		public Material next()
		{
			if(materialIndex == 0) {
				return firstMaterial;
			}else if(materialIndex == 1){
				return secondMaterial;
			}
			
			materialIndex++;
			
			return null;
		}

		@Override
		public void remove()
		{
			//No
		}
		
	}
}
