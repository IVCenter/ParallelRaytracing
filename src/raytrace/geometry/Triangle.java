package raytrace.geometry;

import math.Ray;
import math.Vector4;
import raytrace.data.BakeData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.surfaces.TerminalSurface;

public class Triangle extends TerminalSurface {

	/*
	 * A simple triangle surface
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vertex[] vertices;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Triangle()
	{
		vertices = new Vertex[3];
	}
	
	public Triangle(Vertex v0, Vertex v1, Vertex v2)
	{
		vertices = new Vertex[3];
		vertices[0] = v0;
		vertices[1] = v1;
		vertices[2] = v2;
		updateBoundingBox();
		dynamic = false;
	}
	

	/* *********************************************************************************************
	 * Surface Methods
	 * *********************************************************************************************/
	/**
	 * 
	 */
	public IntersectionData intersects(RayData data)
	{
		Ray ray = data.getRay();
		
		double[] d = ray.getDirection().getArray();

		double[] a = vertices[0].getPosition().getArray();
		double[] b = vertices[1].getPosition().getArray();
		double[] c = vertices[2].getPosition().getArray();

		double va = b[0] - a[0];
		double vb = b[1] - a[1];
		double vc = b[2] - a[2];
		double vd = c[0] - a[0];
		double ve = c[1] - a[1];
		double vf = c[2] - a[2];
		double vg = d[0];
		double vh = d[1];
		double vi = d[2];
		
		double ei_hf = ve * vi - vh * vf;
		double gf_di = vg * vf - vd * vi;
		double dh_eg = vd * vh - ve * vg;
		
		double m = va * ei_hf + vb * gf_di + vc * dh_eg;
		
		//If the determinant m is 0, then the ray is parallel
		if(m == 0.0 || m != m)
			return null;

		
		double[] e = ray.getOrigin().getArray();
		
		double vj = e[0] - a[0];
		double vk = e[1] - a[1];
		double vl = e[2] - a[2];
		
		double ak_jb = va * vk - vj * vb;
		double jc_al = vj * vc - va * vl;
		double bl_kc = vb * vl - vk * vc;
		
		
		double t = (vf * ak_jb + ve * jc_al + vd * bl_kc) / m;
		double t0 = data.getTStart();
		double t1 = data.getTEnd();

		//Test if t is in the given time range
		if(t <= t0 || t > t1)
			return null;
		
		double gamma = (vi * ak_jb + vh * jc_al + vg * bl_kc) / m;
		
		//If the gamma coord is not in the triangle, no intersection
		if(gamma < 0 || gamma > 1)
			return null;
		
		double beta = (vj * ei_hf + vk * gf_di + vl * dh_eg) / m;

		//If beta is outside of the triangle, or exceeds the allowable difference of 1 and gamma, no intersection
		if(beta < 0 || beta > 1 - gamma)
			return null;
		
		
		//Interpolate the normals
		double[] n0 = vertices[0].getNormal().getArray();
		double[] n1 = vertices[1].getNormal().getArray();
		double[] n2 = vertices[2].getNormal().getArray();
		
		double alpha = 1.0 - (gamma + beta);
		
		double nx = n0[0] * alpha + n1[0] * beta + n2[0] * gamma;
		double ny = n0[1] * alpha + n1[1] * beta + n2[1] * gamma;
		double nz = n0[2] * alpha + n1[2] * beta + n2[2] * gamma;
		
		Vector4 normal = new Vector4(nx, ny, nz, 0);
		normal.normalize3();
		
		
		//Point
		Vector4 point = ray.evaluateAtTime(t);
		
		
		//Interpolate the TexCoords
		double[] tex0 = vertices[0].getTexCoord().getArray();
		double[] tex1 = vertices[1].getTexCoord().getArray();
		double[] tex2 = vertices[2].getTexCoord().getArray();
		
		double uCoord = tex0[0] * alpha + tex1[0] * beta + tex2[0] * gamma;
		double vCoord = tex0[1] * alpha + tex1[1] * beta + tex2[1] * gamma;
		
		Vector4 texcoord = new Vector4(uCoord, vCoord, 0, 0);
		
		
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(point);
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(normal);
		idata.setMaterial(material);

		idata.setSurface(this);
		idata.setTexcoord(texcoord);
		idata.setLocalPoint(new Vector4(point));
		
		return idata;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
		//normal = vertices[0].subtract3(vertices[1]).cross3(vertices[2].subtract3(vertices[1])).normalize3();
	}
	
	@Override
	/**
	 * 
	 */
	public void updateBoundingBox()
	{
		boundingBox.clear();
		boundingBox.min.minimize3M(vertices[0].getPosition()).minimize3M(vertices[1].getPosition()).minimize3M(vertices[2].getPosition());
		boundingBox.max.maximize3M(vertices[0].getPosition()).maximize3M(vertices[1].getPosition()).maximize3M(vertices[2].getPosition());
	}
	
	public void generateFaceNormal()
	{
		Vector4 normal = vertices[0].position.subtract3(vertices[1].position).cross3(
				vertices[2].position.subtract3(vertices[1].position)).normalize3();
		for(Vertex v : vertices)
			v.normal = normal;
	}

	
	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	public void print()
	{
		System.out.println("Triangle[" + this.toString() + "]");
		for(Vertex v : vertices)
		{
			System.out.print("\tPosition: ");
			v.getPosition().print();
			System.out.print("\tNormal: ");
			v.getNormal().print();
			System.out.print("\tTexCoord: ");
			v.getTexCoord().print();
		}
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}
	
	public Vertex getVertex(int element) {
		return vertices[element];
	}
	
	public void setVertex(int element, Vertex v) {
		vertices[element] = v;
	}
	
	public double getArea()
	{
		Vector4 a = vertices[0].getPosition().subtract3(vertices[1].getPosition());
		Vector4 b = vertices[2].getPosition().subtract3(vertices[1].getPosition());
		
		return a.cross3(b).magnitude3()/2.0;
	}
	
}
