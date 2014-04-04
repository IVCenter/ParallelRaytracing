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
	public void normalize3()
	{
		//TODO: normalize
	}
	
	public int rgb32()
	{
		double max = Math.max(Math.max(channels[0], channels[1]), channels[2]);
		if(max < 1.0)
			max = 1.0;
		return  (0xff << 24) +
				((int)(channels[0]/max * 256) << 16) +
				((int)(channels[1]/max * 256) << 8) +
				((int)(channels[2]/max * 256));
	}
	
	
	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public double[] getChannels()
	{
		return channels;
	}
	
	public double get(int channel)
	{
		return channels[channel];
	}
	
	//TODO: Add
	//TOOD: Multiply
	//TODO: Other convenient color methods like contrast, saturation, hue shifts, etc.
	

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
