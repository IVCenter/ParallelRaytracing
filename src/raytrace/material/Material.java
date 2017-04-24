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
	//sample points towards where the light is coming from
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
	
}
