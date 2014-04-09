package raytrace.color;

import process.utils.StringUtils;

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
	
	public Color(int color)
	{
		this();
		
		//construct a bit mask 0xff000000
	    int mask = 0xff000000;
	    int u8bit = 0x000000ff;
	    
	    //Unpack each 8bit segment into a float, and normalize such that 255 ~= 1.0
	    for(int i = 0; mask != 0 && i < 4; ++i, mask = mask >>> 8) {
	        channels[i] = ((double) ((color & mask) >>> ((3-i)*8) ) ) / u8bit;
	    }
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
		if(max >= 0.0 && max < 1.0)
			max = 1.0;
		return  (0x000000ff << 24) +
				((int)((channels[0]/max) * 255) << 16) +
				((int)((channels[1]/max) * 255) << 8) +
				((int)((channels[2]/max) * 255));
	}
	
	public Color add3(Color c)
	{
		return new Color(channels[0] + c.channels[0], channels[1] + c.channels[1], channels[2] + c.channels[2], channels[3]);
	}
	
	public Color add3M(Color c)
	{
		channels[0] += c.channels[0];
		channels[1] += c.channels[1];
		channels[2] += c.channels[2];
		return this;
	}
	
	public Color multiply3(double d)
	{
		return new Color(channels[0] * d, channels[1] * d, channels[2] * d, channels[3]);
	}
	
	public Color multiply3(Color c)
	{
		return new Color(channels[0] * c.channels[0], channels[1] * c.channels[1], channels[2] * c.channels[2], channels[3]);
	}
	
	public Color interpolate(Color color0, Color color1, double t)
	{
		t = Math.min(1.0, Math.max(0.0, t));
		double[] c0 = color0.getChannels();
		double[] c1 = color1.getChannels();
		
	    return new Color((1.0 - t) * c0[0] + t * c1[0],
	                 	 (1.0 - t) * c0[1] + t * c1[1],
	                 	 (1.0 - t) * c0[2] + t * c1[2],
	                 	 (1.0 - t) * c0[3] + t * c1[3]);
	}
	

	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	public void print()
	{
		System.out.println("[" + StringUtils.column(""+channels[0], 8) + ", " + 
								 StringUtils.column(""+channels[1], 8) + ", " +
								 StringUtils.column(""+channels[2], 8) + ", " +
								 StringUtils.column(""+channels[3], 8) + "]");
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setter Methods
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
	
	public static Color random(double min)
	{
		min = Math.min(1.0, Math.max(min, 0.0));
		return new Color(Math.random() * (1.0-min) + min, 
						 Math.random() * (1.0-min) + min, 
						 Math.random() * (1.0-min) + min, 
						 1.0);
	}
}
