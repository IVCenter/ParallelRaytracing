package raytrace.material.blend.ternary;

import java.util.Iterator;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;
import raytrace.material.blend.Blend;

public abstract class TernaryBlend extends Material implements Blend {
	
	/*
	 * A base class for binary blends
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Material firstMaterial;
	protected Material secondMaterial;
	protected Material thirdMaterial;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public TernaryBlend(Material firstMaterial, Material secondMaterial, Material thirdMaterial)
	{
		this.firstMaterial = firstMaterial;
		this.secondMaterial = secondMaterial;
		this.thirdMaterial = thirdMaterial;
	}
	
	
	/* *********************************************************************************************
	 * Abstract Blend Method
	 * *********************************************************************************************/
	public abstract Color blend(Color firstShade, Color secondShade, Color thirdShade, ShadingData data);


	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		return blend(firstMaterial.shade(data), secondMaterial.shade(data), thirdMaterial.shade(data), data);
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
	
	public Material getThirdMaterial() {
		return thirdMaterial;
	}

	public void setThirdMaterial(Material thirdMaterial) {
		this.thirdMaterial = thirdMaterial;
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
			return materialIndex < 3;
		}

		@Override
		public Material next()
		{
			if(materialIndex == 0) {
				return firstMaterial;
			}else if(materialIndex == 1){
				return secondMaterial;
			}else if(materialIndex == 2){
				return thirdMaterial;
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
