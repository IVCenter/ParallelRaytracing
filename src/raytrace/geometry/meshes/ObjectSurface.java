package raytrace.geometry.meshes;

import process.logging.Logger;
import raytrace.bounding.BoundingBox;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.geometry.Triangle;
import raytrace.geometry.Vertex;
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
		Logger.message(-14, "Starting creating an Object Surface....");
		Logger.message(-14, "Vertices: [" + obj.getVertices().size() + "].");
		Logger.message(-14, "Normals: [" + obj.getNormals().size() + "].");
		Logger.message(-14, "TexCoords: [" + obj.getTexCoords().size() + "].");
		Logger.message(-14, "Faces: [" + obj.getFaces().size() + "].");
		
		//int tempCount = 0;
		
		int[][] verts;
		Triangle triangle;
		double area;
		for(ObjModelData.Face face : obj.getFaces())
		{
			verts = face.getVertices();
			
			//TODO: If normals are at index 0, create from face
			
			triangle = new Triangle(new Vertex(obj.getVertex(verts[0][0]), obj.getNormal(verts[0][2]), obj.getTexCoord(verts[0][1])),
									new Vertex(obj.getVertex(verts[1][0]), obj.getNormal(verts[1][2]), obj.getTexCoord(verts[1][1])),
									new Vertex(obj.getVertex(verts[2][0]), obj.getNormal(verts[2][2]), obj.getTexCoord(verts[2][1])));

			area = triangle.getArea();
			if(area <= 0.0 || Double.isNaN(area)) {
				Logger.warning(-14, "ObjectSurface: Encountered Triangle with an area of [" + triangle.getArea() + "].");
				continue;
			}
			
			triangles.add(triangle);
		}
		
		//Push the surface id to all children before accelerating
		this.pushSurfaceIDToChildren();
		
		//Create and profile aabvh
		long startTime = System.currentTimeMillis();
		Logger.message(-1, "Starting creating a BVH for root surface... [" + triangles.size() + " subsurfaces].");
		aabvh = AABVHSurface.makeAABVH(triangles, 1, 4);
		Logger.message(-1, "Ending AABVH creation... (" + (System.currentTimeMillis() - startTime) + "ms).");
	}

	
	/* *********************************************************************************************
	 * Overrides
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data)
	{
		IntersectionData idata = aabvh.intersects(data);
		if(idata != null)
		{
			idata.setMaterial(this.material);
			idata.setSurfaceID(surfaceID);
		}
		
		return idata;
		//return aabvh.intersects(data);
	}
	
	@Override
	public void bake(BakeData data)
	{
		aabvh.bake(data);
	}
	
	@Override
	public BoundingBox getBoundingBox()
	{
		return aabvh.getBoundingBox();
	}

	/*
	@Override
	public void setMaterial(Material material)
	{
		for(Triangle tri : triangles)
			tri.setMaterial(material);
	}
	*/

}
