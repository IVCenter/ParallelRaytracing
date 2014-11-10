package raytrace.geometry.meshes;

import java.util.ArrayList;

import process.logging.Logger;

import file.obj.ObjModelData;
import raytrace.surfaces.AbstractSurface;
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
	protected ArrayList<ObjectSurface> objectSurfaces;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public CompositeObjectSurface(ObjModelData data)
	{
		Logger.progress(-1, "Starting creating a Composite OBject Surface...");
		
		objectSurfaces = new ArrayList<ObjectSurface>();
		
		//For each object in the model, create an object surface
		for(ObjModelData.Object obj : data.getObjects())
		{
			objectSurfaces.add(new ObjectSurface(obj));
		}
		
		//If there are enough surfaces, make an aabvh to wrap them
		if(objectSurfaces.size() >= minSurfacesRequiredForAABVH)
		{
			AABVHSurface aabvh = AABVHSurface.makeAABVH(objectSurfaces);
			this.addChild(aabvh);
		}else{
			for(AbstractSurface cs : objectSurfaces)
				this.addChild(cs);
		}
		
		//this.updateBoundingBox();
		
		Logger.progress(-1, "Ending creating a Composite OBject Surface.");
	}

}
