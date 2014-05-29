package raytrace.map.texture._3D;

import java.util.Random;

import math.noise.WorleyNoise3D;
import raytrace.color.Color;

public class WorleyNoiseTexture3D extends Texture3D {
	
	/*
	 * A worley noise texture that returns an interpolation between two colors
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected long seed;
	protected WorleyNoise3D noiseFunction;
	protected Color firstColor;
	protected Color secondColor;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public WorleyNoiseTexture3D()
	{
		seed = (new Random()).nextLong();
		noiseFunction = new WorleyNoise3D();
		this.firstColor = Color.black();
		this.secondColor = Color.white();
	}
	
	public WorleyNoiseTexture3D(long seed)
	{
		this.seed = seed;
		noiseFunction = new WorleyNoise3D(seed);
		this.firstColor = Color.black();
		this.secondColor = Color.white();
	}
	
	public WorleyNoiseTexture3D(Color firstColor, Color secondColor)
	{
		seed = (new Random()).nextLong();
		noiseFunction = new WorleyNoise3D(seed);
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	
	public WorleyNoiseTexture3D(long seed, Color firstColor, Color secondColor)
	{
		this.seed = seed;
		noiseFunction = new WorleyNoise3D(seed);
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

	public WorleyNoise3D getNoiseFunction() {
		return noiseFunction;
	}

	public void setNoiseFunction(WorleyNoise3D noiseFunction) {
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
