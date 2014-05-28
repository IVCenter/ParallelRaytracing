package raytrace.material.blend.nary;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.material.Material;
import raytrace.material.blend.Blend;

public abstract class NaryBlend extends Material implements Blend {
	
	/*
	 * A base class for binary blends
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Collection<Material> materials;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public NaryBlend(Collection<Material> materials)
	{
		this.materials = materials;
	}
	
	
	/* *********************************************************************************************
	 * Abstract Blend Method
	 * *********************************************************************************************/
	public abstract Color blend(Collection<Color> colors);


	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		List<Color> colors = new LinkedList<Color>();
		
		//Add the colors for all material evaluation
		for(Material material : materials)
			colors.add(material.shade(data));
		
		return blend(colors);
	}


	/* *********************************************************************************************
	 * Setters/Getters
	 * *********************************************************************************************/	
	public Collection<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Collection<Material> materials) {
		this.materials = materials;
	}
	
	
	/* *********************************************************************************************
	 * Iterator Overtide
	 * *********************************************************************************************/
	@Override
	public Iterator<Material> iterator()
	{
		return materials.iterator();
	}

}
