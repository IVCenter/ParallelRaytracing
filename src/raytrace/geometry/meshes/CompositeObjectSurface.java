package raytrace.geometry.meshes;

import java.util.ArrayList;

import process.logging.Logger;

import file.obj.ObjModelData;
import raytrace.material.Material;
import raytrace.surfaces.CompositeSurface;
import raytrace.surfaces.acceleration.AABVHSurface;

public class CompositeObjectSurface extends CompositeSurface {
	
	/*
	 * A composite collection of objects as loaded from an .obj file
	 */
	/*
	 * For each object, create an ObjectSurface
	 * Create an AABVH for all objects
	 * Add AABVH as this child
	 */
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static final int minSurfacesRequiredForAABVH = 5;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CompositeObjectSurface(ObjModelData data)
	{
		Logger.progress(-1, "Starting creating a Composite OBject Surface...");
		
		ArrayList<ObjectSurface> objects = new ArrayList<ObjectSurface>();
		
		//For each object in the model, create an object surface
		for(ObjModelData.Object obj : data.getObjects())
		{
			objects.add(new ObjectSurface(obj));
		}
		
		//If there are enough surfaces, make an aabvh to wrap them
		if(objects.size() >= minSurfacesRequiredForAABVH)
		{
			AABVHSurface aabvh = AABVHSurface.makeAABVH(objects);
			this.addChild(aabvh);
		}else{
			for(CompositeSurface cs : objects)
				this.addChild(cs);
		}
		
		Logger.progress(-1, "Ending creating a Composite OBject Surface.");
	}
	

	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public void setMaterial(Material material)
	{
		for(CompositeSurface cs : this)
			cs.setMaterial(material);
	}

}
