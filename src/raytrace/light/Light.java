package raytrace.light;

import math.Vector4;
import raytrace.color.Color;
import raytrace.framework.Positionable;
import raytrace.surfaces.TerminalSurface;

public abstract class Light extends TerminalSurface implements Positionable {

	/*
	 * A base class for lights
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double constantAttenuation = 1.0;
	protected double linearAttenuation = 0.0;
	protected double quadraticAttenuation = 1.0;
	
	protected Color color = Color.white();;
	protected double intensity = 1.0;
	
	protected Vector4 position = new Vector4();
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	public abstract Color illuminate();


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
