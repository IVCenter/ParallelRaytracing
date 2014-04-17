package raytrace.geometry.meshes;

import file.obj.ObjModelData;

public class ObjectMeshSurface extends MeshSurface {
	
	/*
	 * A simple mesh initializer for obj file data
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ObjectMeshSurface(ObjModelData data)
	{
		super();
		initialize(data);
	}

	
	/* *********************************************************************************************
	 * Init Triangles
	 * *********************************************************************************************/
	public void initialize(ObjModelData data)
	{
		//TODO: Load object model data into this mesh
	}

}
