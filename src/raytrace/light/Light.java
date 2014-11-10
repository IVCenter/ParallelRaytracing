package raytrace.light;

import math.Vector3;
import math.ray.Ray;
import raytrace.bounding.BoundingBox;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.ShadingData;
import raytrace.data.UpdateData;
import raytrace.framework.Positionable;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;

public abstract class Light extends AbstractSurface implements Positionable {

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
	
	protected Vector3 position = new Vector3();
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract IlluminationData illuminate(ShadingData data, Vector3 point);

	
	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected IntersectionData shadowed(Scene scene, Ray ray, double distanceToLight)
	{
		//TODO: Remove this, and instead use a pre-allocated object
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
	 * Override Methods
	 * *********************************************************************************************/
	@Override
	public IntersectionData intersects(RayData data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bake(BakeData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(UpdateData data) {
		// TODO Auto-generated method stub
	}

	@Override
	public BoundingBox getBoundingBox() {
		// TODO Auto-generated method stub
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

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
}
