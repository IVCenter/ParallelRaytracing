package raytrace.light;

import math.Vector3;
import math.ray.Ray;
import raytrace.bounding.BoundingBox;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.UpdateData;
import raytrace.framework.Positionable;
import raytrace.scene.Scene;
import raytrace.surfaces.AbstractSurface;
import system.Constants;

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
	
	protected boolean castsShadows = true;
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract IlluminationData illuminate(Scene scene, Vector3 point);

	
	/* *********************************************************************************************
	 * Helper Methods
	 * *********************************************************************************************/
	protected IntersectionData shadowed(Scene scene, Ray ray, double distanceToLight)
	{
		//If this does not cast shadows, return null
		if(!castsShadows)
			return null;
		
		//TODO: Remove this, and instead use a pre-allocated object
		RayData rdata = new RayData();
		
		rdata.setRay(ray);
		rdata.setTStart(RECURSIVE_EPSILON);
		IntersectionData idata = scene.intersects(rdata);
		Vector3 origin;
		
		//If no intersection, not shadowed
		if(idata == null)
			return null;
		
		//If the object we intersected does not cast a shadow, recursively call from here
		if(!idata.getMaterial().castsShadows())
		{
			origin = ray.getOrigin();
			ray.setOrigin(idata.getPoint().addMultiRight(ray.getDirection(), Constants.RECURSIVE_EPSILON));
			idata = shadowed(scene, ray, distanceToLight - idata.getDistance());
			ray.setOrigin(origin);
			return idata;
		}
		
		//If the light is infinitely far, and we hit something, in shadow
		if(distanceToLight == Double.POSITIVE_INFINITY)
			return idata;
		
		//If the intersected object is closer than the light, in shadow
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

	public boolean castsShadows() {
		return castsShadows;
	}

	public void setCastsShadows(boolean castsShadows) {
		this.castsShadows = castsShadows;
	}
	
}
