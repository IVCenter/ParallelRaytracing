package raytrace.map.texture._3D;

import java.util.Random;

import math.noise.SimplexNoise3D;
import raytrace.color.Color;

public class SimplexNoiseTexture3D extends Texture3D {
	
	/*
	 * A simplex noise texture that returns gray scale colors on evaluation
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected long seed;
	protected SimplexNoise3D noiseFunction;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SimplexNoiseTexture3D()
	{
		seed = (new Random()).nextLong();
		noiseFunction = new SimplexNoise3D(seed);
	}
	
	public SimplexNoiseTexture3D(long seed)
	{
		this.seed = seed;
		noiseFunction = new SimplexNoise3D(seed);
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double t = noiseFunction.evaluate(x, y, z);
		return Color.gray(t);
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

}
