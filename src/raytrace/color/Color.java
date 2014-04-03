package raytrace.color;

public class Color {
	
	/*
	 * A simple color class
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double[] channels;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Color()
	{
		channels = new double[4];
	}
	
	public Color(double[] channels)
	{
		this();
		this.channels[0] = channels[0];
		this.channels[1] = channels[1];
		this.channels[2] = channels[2];
		this.channels[3] = channels[3];
	}
	
	public Color(double r, double g, double b, double a)
	{
		this();
		this.channels[0] = r;
		this.channels[1] = g;
		this.channels[2] = b;
		this.channels[3] = a;
	}
	
	public Color(double r, double g, double b)
	{
		this(r, g, b, 1);;
	}

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public void normalize()
	{
		//TODO: normalize
	}
	

	/* *********************************************************************************************
	 * Static Helper Methods
	 * *********************************************************************************************/
	public static Color white()
	{
		return new Color(1.0, 1.0, 1.0, 1.0);
	}
	
	public static Color black()
	{
		return new Color(0.0, 0.0, 0.0, 1.0);
	}
	
	public static Color grey()
	{
		return new Color(0.5, 0.5, 0.5, 1.0);
	}
	
	public static Color grey(double ratio)
	{
		return new Color(ratio, ratio, ratio, 1.0);
	}
	
	public static Color red()
	{
		return new Color(1.0, 0.0, 0.0, 1.0);
	}
	
	public static Color random()
	{
		return new Color(Math.random(), Math.random(), Math.random(), 1.0);
	}
}
