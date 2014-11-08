package raytrace.material.library;

import java.util.ArrayList;
import java.util.HashMap;

import raytrace.color.Color;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffusePTMaterial;
import raytrace.material.Material;

public class MaterialLibrary {
	
	/*
	 * A library of pre-made materials
	 */
	
	/* *********************************************************************************************
	 * Static Constants
	 * *********************************************************************************************/
	protected static final String ERROR = "ERROR";
	
	protected static final KeySet keys = new KeySet();
	
	public static final String MATTE_WHITE = keys.addKey("MATTE_WHITE");
	
	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static HashMap<String, MaterialConstructor> materials;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		materials = new HashMap<String, MaterialConstructor>();
		
		materials.put(ERROR, new MaterialConstructor() {
			public Material create() {
				return new ColorMaterial(Color.red());
			}
		});
		
		materials.put(MATTE_WHITE, new MaterialConstructor() { 
			public Material create() {
				return new DiffusePTMaterial(Color.white());
			}
		});
		
		//
	}
	

	/* *********************************************************************************************
	 * Static Material Methods
	 * *********************************************************************************************/
	public static Material random()
	{
		return lookup(keys.get((int)(Math.random() * keys.size())));
	}
	
	public static Material lookup(String materialName)
	{
		MaterialConstructor constructor = materials.get(materialName);
		if(constructor == null)
			constructor = materials.get(ERROR);
		return constructor.create();
	}
	
	public static DiffusePTMaterial matteWhite()
	{
		return (DiffusePTMaterial)lookup(MATTE_WHITE);
	}

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	protected abstract static class MaterialConstructor
	{
		public abstract Material create();
	}

	@SuppressWarnings("serial")
	protected static class KeySet extends ArrayList<String>
	{
		public String addKey(String key)
		{
			super.add(key);
			return key;
		}
	}
}
