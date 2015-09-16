package raytrace.trace.integration;

import java.util.LinkedList;

import raytrace.data.IntegrationData;
import raytrace.data.RayData;
import raytrace.medium.Medium;
import raytrace.scene.Scene;

public abstract class Integrator {
	
	/*
	 * A base class for integrators
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static final int DO_NOT_EXCEED_RECURSION_LEVEL = 100;
	protected static final int SYSTEM_RESURSION_LIMIT = 100;

	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double pathTerminationProbability = 0.0;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	

	/* *********************************************************************************************
	 * Methods
	 * *********************************************************************************************/
	public abstract IntegrationData integrate(Scene scene, LinkedList<Medium> mediums, RayData rayData, int recursionDepth);


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public double getPathTerminationProbability() {
		return pathTerminationProbability;
	}

	public void setPathTerminationProbability(double pathTerminationProbability) {
		this.pathTerminationProbability = pathTerminationProbability;
	}
}
