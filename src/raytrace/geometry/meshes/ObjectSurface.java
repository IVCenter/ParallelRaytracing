package raytrace.geometry.meshes;

import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.surfaces.acceleration.AABVHSurface;
import file.obj.ObjModelData;

public class ObjectSurface extends MeshSurface {
	
	/*
	 * A simple mesh initializer for obj file data
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected AABVHSurface aabvh;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ObjectSurface(ObjModelData.Object obj)
	{
		super(obj.getFaces().size());
		initialize(obj);
	}

	
	/* *********************************************************************************************
	 * Init Triangles
	 * *********************************************************************************************/
	public void initialize(ObjModelData.Object obj)
	{
		//TODO: Load object model data into this mesh
		//Dont forget to populate the triangles list in super.
		
		/*
		 * Create triangles
		 * Create aabvh from triangles
		 * set aabvh var
		 */
		
		for(ObjModelData.Face face : obj.getFaces())
		{
			
		}
		
		
		
		aabvh = AABVHSurface.makeAABVH(triangles);
	}

	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		return aabvh.intersects(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		aabvh.bake(data);
	}
	
	@Override
	public void updateBoundingBox()
	{
		aabvh.updateBoundingBox();
	}

}
