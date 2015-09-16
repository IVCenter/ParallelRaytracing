package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.medium.Medium;

public abstract class Material {
	
	/*
	 * A base class for programmable materials
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static final double oneOverPi = 1.0 / Math.PI;
	

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//TODO: Upgrade materials interface
	//Flags!
	//Properties!!
	//Separation from integration logic!!!
	protected boolean castsShadows = true;
	protected boolean receivesShadows = true;
	protected boolean globallyIlluminated = true;
	protected boolean affectedByLightSources = true;
	protected boolean emitsLight = false;
	
	protected Medium medium = null;


	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Abstract Programmable Methods
	 * *********************************************************************************************/
	public abstract RayData sample(IntersectionData idata, RayData rdata);

	public abstract Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection);
	
	//Returns the sampled light if importance sampled, else weights the sampled light
	public abstract Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample);
	
	//Returns a non-zero amount of light if the material itself emits light independent of incoming light
	public abstract Color evaluateEmission(IntersectionData idata, RayData rdata);
	

	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected Color diffuse(Color light, Vector3 normal, Vector3 fromLight)
	{
		double dot = normal.dot(fromLight);
		
		if(fromLight.magnitudeSqrd() == 0.0)
			return light.multiply3(1.0);
		
		if(dot >= 0.0)
			return Color.black();
		
		return light.multiply3( dot * -1.0 * oneOverPi );
	}
	
	protected Vector3 reflect(Vector3 ray, Vector3 point, Vector3 normal)
	{
		double c = -2.0 * ray.dot(normal);
		
		double[] dirm = ray.getArray();
		double[] nm = normal.getArray();

		double rx = dirm[0] + nm[0] * c;
		double ry = dirm[1] + nm[1] * c;
		double rz = dirm[2] + nm[2] * c;
		
		Vector3 reflect = (new Vector3(rx, ry, rz)).normalizeM();
		
		return reflect;
	}

	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Medium getMedium() {
		return medium;
	}
	
	public void setMedium(Medium medium) {
		this.medium = medium;
	}

	public boolean castsShadows() {
		return castsShadows;
	}

	public void setCastsShadows(boolean castsShadows) {
		this.castsShadows = castsShadows;
	}

	public boolean receivesShadows() {
		return receivesShadows;
	}

	public void setReceivesShadows(boolean receivesShadows) {
		this.receivesShadows = receivesShadows;
	}

	public boolean isGloballyIlluminated() {
		return globallyIlluminated;
	}

	public void setGloballyIlluminated(boolean globallyIlluminated) {
		this.globallyIlluminated = globallyIlluminated;
	}

	public boolean isAffectedByLightSources() {
		return affectedByLightSources;
	}

	public void setAffectedByLightSources(boolean affectedByLightSources) {
		this.affectedByLightSources = affectedByLightSources;
	}

	public boolean emitsLight() {
		return emitsLight;
	}

	public void setEmitsLight(boolean emitsLight) {
		this.emitsLight = emitsLight;
	}
	
	/*
	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex, boolean increaseRecurDepth)
	{		
		//If we're past the ABSOLUTE recursive limit, use black
		if(data.getActualRecursionDepth() >= SYSTEM_RESURSION_LIMIT) {
			return Color.black();
		}
		
		RayData rdata = new RayData();
		Ray ray = new Ray(point, direction, 0, 0);
		rdata.setRay(ray);
		//rdata.setRootSurface(data.getRootScene());
		rdata.setTStart(RECURSIVE_EPSILON);
		rdata.setTEnd(Double.MAX_VALUE);
		
		ShadingData sdata = new ShadingData();
		//sdata.setRay(rdata.getRay());
		sdata.setRayData(rdata);
		sdata.setRootScene(data.getRootScene());
		sdata.setActualRecursionDepth(data.getActualRecursionDepth() + 1);
		sdata.setRecursionDepth(data.getRecursionDepth() + (increaseRecurDepth ? 1 : 0) );
		sdata.setRefractiveIndex(refractiveIndex);
		
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		
		if(idata != null) {
			sdata.setIntersectionData(idata);
			return idata.getMaterial().shade(sdata);
		}		

		//If there wasn't an intersection, use the sky material
		sdata.setIntersectionData(null);
		return data.getRootScene().getSkyMaterial().shade(sdata);
	}
	*/
	
	/*
	protected Color recurse(ShadingData data, Vector3 point, Vector3 direction, double refractiveIndex, boolean increaseRecurDepth)
	{		
		//If we're past the ABSOLUTE recursive limit, use black
		if(data.getRecursionDepth() > DO_NOT_EXCEED_RECURSION_LEVEL || data.getActualRecursionDepth() >= SYSTEM_RESURSION_LIMIT) {
			return Color.black();
		}
		
		
		//Store the old values locally
		RayData rdata = data.getRayData();
		Ray ray = rdata.getRay();
		
		//Replace the RayDatas values with the current recursion values
		ray.setOrigin(point);
		ray.setDirection(direction);
		rdata.setTStart(RECURSIVE_EPSILON);
		rdata.setTEnd(Double.MAX_VALUE);
		
		//
		data.setActualRecursionDepth(data.getActualRecursionDepth() + 1);
		data.setRecursionDepth(data.getRecursionDepth() + (increaseRecurDepth ? 1 : 0) );
		data.setRefractiveIndex(refractiveIndex);
		
		
		IntersectionData idata = data.getRootScene().intersects(rdata);
		
		
		if(idata != null) {
			data.setIntersectionData(idata);
			return idata.getMaterial().shade(data);
		}
		
		//If there wasn't an intersection, use the sky material
		data.setIntersectionData(null);
		return data.getRootScene().getSkyMaterial().shade(data);
	}
	*/
	/*
	protected Vector3 halfVector(Vector3 a, Vector3 b)
	{
		return Vector3.halfVector(a, b);
	}
	
	protected Vector3 cosineWeightedSample()
	{
		return Vector3.cosineWeightedSample();
	}
	
	protected Vector3 cosineWeightedSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		return Vector3.cosineWeightedSample(xa, ya, za);
	}
	
	protected Vector3 uniformHemisphereSample()
	{
		return Vector3.uniformHemisphereSample();
	}
	
	protected Vector3 uniformHemisphereSample(Vector3 xa, Vector3 ya, Vector3 za)
	{
		return Vector3.uniformHemisphereSample(xa, ya, za);
	}

	protected Vector3 diskSample(double radius, double weight)
	{
		return Vector3.diskSample(radius, weight);
	}
	
	protected Vector3 uniformSphereSample()
	{
		return Vector3.uniformSphereSample();
	}
	*/
}
