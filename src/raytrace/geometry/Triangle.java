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
	protected Vector4[] vertices;
	protected Vector4 normal;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Triangle()
	{
		vertices = new Vector4[3];
		for(int i = 0; i < 3; ++i) {
			vertices[i] = new Vector4();
			vertices[i].set(i, 1);
		}
	}
	
	public Triangle(Vector4 v0, Vector4 v1, Vector4 v2)
	{
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
		double t0 = data.getTStart();
		double t1 = data.getTEnd();
		
		double[] e = ray.getOrigin().getM();
		double[] d = ray.getDirection().getM();

		double[] a = vertices[0].getM();
		double[] b = vertices[1].getM();
		double[] c = vertices[2].getM();

		double va = a[0] - b[0];
		double vb = a[1] - b[1];
		double vc = a[2] - b[2];
		double vd = a[0] - c[0];
		double ve = a[1] - c[1];
		double vf = a[2] - c[2];
		double vg = d[0];
		double vh = d[1];
		double vi = d[2];
		double vj = a[0] - e[0];
		double vk = a[1] - e[1];
		double vl = a[2] - e[2];
		
		double ei_hf = ve * vi - vh * vf;
		double gf_di = vg * vf - vd * vi;
		double dh_eg = vd * vh - ve * vg;
		double ak_jb = va * vk - vj * vb;
		double jc_al = vj * vc - va * vl;
		double bl_kc = vb * vl - vk * vc;
		
		double m = va * ei_hf + vb * gf_di + vc * dh_eg;
		double t = (vf * ak_jb + ve * jc_al + vd * bl_kc) / m;

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
		
		
		//Return data about the intersection
		IntersectionData idata = new IntersectionData();
		idata.setTime(t);
		idata.setRay(ray);
		idata.setPoint(ray.evaluateAtTime(t));
		idata.setDistance(ray.getDirection().magnitude3() * t);
		idata.setNormal(ray.getDirection().dot3(normal) < 0 ? normal.multiply3(-1) : normal);
		idata.setSurface(this);
		
		return idata;
	}
	
	/**
	 * 
	 */
	public void bake(BakeData data)
	{
		//TODO: Bake
		normal = vertices[0].subtract3(vertices[1]).cross3(vertices[2].subtract3(vertices[1])).normalize3();
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public Vector4[] getVertices() {
		return vertices;
	}

	public void setVertices(Vector4[] vertices) {
		this.vertices = vertices;
	}
	
	public Vector4 getVertex(int element) {
		return vertices[element];
	}
	
	public void set(int element, Vector4 v) {
		vertices[element] = v;
	}

	public Vector4 getNormal() {
		return normal;
	}

	public void setNormal(Vector4 normal) {
		this.normal = normal;
	}
	
}
