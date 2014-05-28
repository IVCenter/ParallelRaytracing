package raytrace.map.texture._3D;

import java.util.Random;

import math.noise.SimplexNoise3D;
import raytrace.color.Color;

public class SimplexInterpolationTexture3D extends Texture3D {
	
	/*
	 * A test implementation of using a Simplex Noise function for texture generation.
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected long seed;
	protected SimplexNoise3D noiseFunction;
	protected Color firstColor;
	protected Color secondColor;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SimplexInterpolationTexture3D(Color firstColor, Color secondColor)
	{
		this.firstColor = firstColor;
		this.secondColor = secondColor;
		seed = (new Random()).nextLong();
		noiseFunction = new SimplexNoise3D(seed);
	}
	
	public SimplexInterpolationTexture3D(Color firstColor, Color secondColor, long seed)
	{
		this.firstColor = firstColor;
		this.secondColor = secondColor;
		this.seed = seed;
		noiseFunction = new SimplexNoise3D(seed);
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double t = 0.5 * Math.abs(1.0 + noiseFunction.evaluate(x, y, z));
		return firstColor.interpolate(firstColor, secondColor, t);
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public SimplexNoise3D getNoiseFunction() {
		return noiseFunction;
	}

	public void setNoiseFunction(SimplexNoise3D noiseFunction) {
		this.noiseFunction = noiseFunction;
	}

	public Color getFirstColor() {
		return firstColor;
	}

	public void setFirstColor(Color firstColor) {
		this.firstColor = firstColor;
	}

	public Color getSecondColor() {
		return secondColor;
	}

	public void setSecondColor(Color secondColor) {
		this.secondColor = secondColor;
	}

}
