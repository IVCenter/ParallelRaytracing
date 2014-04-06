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
		/*
		for(int i = 0; i < 3; ++i) {
			vertices[i] = new Vertex();
			vertices[i].set(i, 1);
		}
		
		Vector4 normal = vertices[0].subtract3(vertices[1]).cross3(vertices[2].subtract3(vertices[1])).normalize3();
		for(int i = 0; i < 3; ++i) {
			normals[i] = new Vector4(normal);
		}
		*/
	}
	
	public Triangle(Vertex v0, Vertex v1, Vertex v2)
	{
		vertices = new Vertex[3];
		vertices[0] = v0;
		vertices[1] = v1;
		vertices[2] = v2;
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
		
		double[] d = ray.getDirection().getM();

		double[] a = vertices[0].getPosition().getM();
		double[] b = vertices[1].getPosition().getM();
		double[] c = vertices[2].getPosition().getM();

		//double va = a[0] - b[0];
		//double vb = a[1] - b[1];
		//double vc = a[2] - b[2];
		//double vd = a[0] - c[0];
		//double ve = a[1] - c[1];
		//double vf = a[2] - c[2];
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
		if(m == 0.0)
			return null;

		
		double[] e = ray.getOrigin().getM();
		
		//double vj = a[0] - e[0];
		//double vk = a[1] - e[1];
		//double vl = a[2] - e[2];
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
		Vector4 normal = vertices[0].getNormal().multiply3(1.0 - (gamma + beta)).add3(
							vertices[1].getNormal().multiply3(gamma)
						 ).add3(
							vertices[2].getNormal().multiply3(beta)
						 ).normalize3();
		
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(ray.evaluateAtTime(t));
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(ray.getDirection().dot3(normal) <= 0 ? normal : normal.multiply3(-1));
		//idata.setSurface(this);
		idata.setMaterial(material);
		
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
		boundingBox.min.minimize3(vertices[0].getPosition()).minimize3(vertices[1].getPosition()).minimize3(vertices[2].getPosition());
		boundingBox.max.maximize3(vertices[0].getPosition()).maximize3(vertices[1].getPosition()).maximize3(vertices[2].getPosition());
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
	
}
