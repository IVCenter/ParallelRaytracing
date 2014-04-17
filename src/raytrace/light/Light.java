package raytrace.light;

import math.Ray;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.framework.Positionable;
import raytrace.scene.Scene;
import raytrace.surfaces.TerminalSurface;

public abstract class Light extends TerminalSurface implements Positionable {

	/*
	 * A base class for lights
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected static final double RECURSIVE_EPSILON = 0.0001;
	
	protected double constantAttenuation = 1.0;
	protected double linearAttenuation = 0.0;
	protected double quadraticAttenuation = 1.0;
	
	protected Color color = Color.white();
	protected double intensity = 1.0;
	
	protected Vector4 position = new Vector4();
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract IlluminationData illuminate(ShadingData data, Vector4 point);

	
	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected IntersectionData shadowed(Scene scene, Ray ray, double distanceToLight)
	{
		RayData rdata = new RayData();
		rdata.setRay(ray);
		rdata.setTStart(RECURSIVE_EPSILON);
		IntersectionData idata = scene.intersects(rdata);
		
		//If no intersection, not shadowed
		if(idata == null)
			return null;
		
		//If the light is infinitely far, and we hit something, in shadow
		if(distanceToLight == Double.MAX_VALUE)
			return idata;
		
		if(idata.getDistance() < distanceToLight)
			return idata;
		
		return null;
	}


	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public double getConstantAttenuation() {
		return constantAttenuation;
	}

	public void setConstantAttenuation(double constantAttenuation) {
		this.constantAttenuation = constantAttenuation;
	}

	public double getLinearAttenuation() {
		return linearAttenuation;
	}

	public void setLinearAttenuation(double linearAttenuation) {
		this.linearAttenuation = linearAttenuation;
	}

	public double getQuadraticAttenuation() {
		return quadraticAttenuation;
	}

	public void setQuadraticAttenuation(double quadraticAttenuation) {
		this.quadraticAttenuation = quadraticAttenuation;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public Vector4 getPosition() {
		return position;
	}

	public void setPosition(Vector4 position) {
		this.position = position;
	}
	
}
