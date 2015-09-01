package raytrace.map.texture._2D;

import raytrace.color.Color;

public class DiscreteTexture2D extends Texture2D {
	
	/*
	 * A base class for 2D textures
	 */
	
	/* *********************************************************************************************
	 * Instance Variables
	 * *********************************************************************************************/
	protected int[][] array;
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public DiscreteTexture2D(int[][] array)
	{
		this.array = array;
	}
	

	/* *********************************************************************************************
	 * Evaluation Methods
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y)
	{
		//Clamp the texture coordinates between 0 and 1
		x = Math.max(0.0, Math.min(1.0, x));
		y = Math.max(0.0, Math.min(1.0, y));
		
		//Get the raw texture coordinate in pixels
		double uRaw = x * array[0].length;
		double vRaw = y * array.length;
		
		//Calculate the 4 surrounding pixel colors
		int u0 = (int)Math.floor(uRaw);
		int u1 = u0 + 1;
		int v0 = (int)Math.floor(vRaw);
		int v1 = v0 + 1;

		//Calculate the sub-pixel offsets
		int uSub = (int)((uRaw - u0) * 256);
		int vSub = (int)((vRaw - v0) * 256);
		
		//Get the interpolated color
		Color c = new Color();
		c.setARGB(Color.interpolate(
				array[v0][u0],
				array[v0][u1],
				array[v1][u0],
				array[v1][u1],
				uSub, 
				vSub));
		
		return c;
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int[][] getArray() {
		return array;
	}

	public void setArray(int[][] array) {
		this.array = array;
	}
	
}
