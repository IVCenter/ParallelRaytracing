package raytrace.medium;

import math.Vector3;
import math.ray.Ray;
import math.volume.IsotropicPhaseFunction;
import math.volume.PhaseFunction;
import raytrace.color.Color;
import raytrace.data.RayData;
import raytrace.map.texture._3D.ColorTexture3D;
import raytrace.map.texture._3D.Texture3D;
import system.Constants;

public class ParticipatingMedium extends Medium {

	/*
	 * An implementation of participating media
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D absorption = new ColorTexture3D(Color.black());
	protected Texture3D scatterOut = new ColorTexture3D(Color.black());
	protected Texture3D scatterIn = new ColorTexture3D(Color.black());
	protected PhaseFunction scatterInFunction = new IsotropicPhaseFunction();
	protected Texture3D emission = new ColorTexture3D(Color.black());
	
	
	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public ParticipatingMedium()
	{
		this.type = Type.PURE;
	}

	
	/* *********************************************************************************************
	 * Medium Overrides
	 * *********************************************************************************************/
	@Override
	public Color scatterIn(Vector3 startPoint, Vector3 endPoint, Color light, Vector3 lightDirection)
	{
		Vector3 exitDirection = endPoint.subtract(startPoint);
		double d = endPoint.subtract(startPoint).magnitude();

		Color s = scatterIn.evaluate(startPoint);
		Color phase = scatterInFunction.evaluate(startPoint, exitDirection.normalize(), lightDirection.normalize());

		//return phase.multiply3(exitDirection.magnitude()).multiply3M(light);
		return phase.multiply3(s).multiply3M(light).multiply3M(d);
	}

	@Override
	public Color transmit(Vector3 startPoint, Vector3 endPoint, Color light)
	{
		double d = endPoint.subtract(startPoint).magnitude();
		double[] a = absorption.evaluate(startPoint).getChannels();
		double[] s = scatterOut.evaluate(startPoint).getChannels();
		
		Color t = new Color();
		double[] tArr = t.getChannels();
		int len = tArr.length;
		
		for(int i = 0; i < len; ++i)
		{
			//tArr[i] = Math.exp(-1.0 * (a[i] + s[i]) * d);
			tArr[i] = 1.0 - ((a[i] + s[i]) * d);
		}
		
		Color e = emission.evaluate(startPoint);
		
		return t.multiply3M(light).add3AfterMultiply3M(e, d);
	}

	@Override
	public RayData sample(Vector3 startPoint, Vector3 endPoint)
	{
		Vector3 direction = startPoint.subtract(endPoint).normalizeM();
		
		RayData rdata = new RayData(new Ray(startPoint, direction, 0, 0), Constants.RECURSIVE_EPSILON, Double.POSITIVE_INFINITY);
		
		return rdata;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getAbsorption() {
		return absorption;
	}

	public void setAbsorption(Texture3D absorption) {
		this.absorption = absorption;
	}

	public Texture3D getScatterOut() {
		return scatterOut;
	}

	public void setScatterOut(Texture3D scatterOut) {
		this.scatterOut = scatterOut;
	}

	public PhaseFunction getScatterInFunction() {
		return scatterInFunction;
	}

	public void setScatterInFunction(PhaseFunction scatterInFunction) {
		this.scatterInFunction = scatterInFunction;
	}

	public Texture3D getEmission() {
		return emission;
	}

	public void setEmission(Texture3D emission) {
		this.emission = emission;
	}

	public Texture3D getScatterIn() {
		return scatterIn;
	}

	public void setScatterIn(Texture3D scatterIn) {
		this.scatterIn = scatterIn;
	}

}
