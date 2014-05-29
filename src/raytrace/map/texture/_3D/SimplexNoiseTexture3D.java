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
	protected Color firstColor;
	protected Color secondColor;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SimplexNoiseTexture3D()
	{
		seed = (new Random()).nextLong();
		noiseFunction = new SimplexNoise3D(seed);
		this.firstColor = Color.black();
		this.secondColor = Color.white();
	}
	
	public SimplexNoiseTexture3D(long seed)
	{
		this.seed = seed;
		noiseFunction = new SimplexNoise3D(seed);
		this.firstColor = Color.black();
		this.secondColor = Color.white();
	}
	
	public SimplexNoiseTexture3D(Color firstColor, Color secondColor)
	{
		seed = (new Random()).nextLong();
		noiseFunction = new SimplexNoise3D(seed);
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	
	public SimplexNoiseTexture3D(long seed, Color firstColor, Color secondColor)
	{
		this.seed = seed;
		noiseFunction = new SimplexNoise3D(seed);
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		double t = noiseFunction.evaluate(x, y, z);
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
