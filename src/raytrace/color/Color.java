package raytrace.color;

import process.utils.StringUtils;
import raytrace.data.IntersectionData;
import raytrace.map.texture.Texture;

public class Color implements Texture {
	
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
	
	public Color(Color color)
	{
		this();
		this.channels[0] = color.channels[0];
		this.channels[1] = color.channels[1];
		this.channels[2] = color.channels[2];
		this.channels[3] = color.channels[3];
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
		
		set(color);
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
	
	public void set(Color c)
	{
		this.set(c.channels[0], c.channels[1], c.channels[2], 1.0);
	}
	
	public void set(double r, double g, double b)
	{
		this.set(r, g, b, 1.0);
	}
	
	public void set(double r, double g, double b, double a)
	{
		channels[0] = r;
		channels[1] = g;
		channels[2] = b;
		channels[3] = a;
	}
	
	public void set(int color)
	{
		//construct a bit mask 0xff000000
	    int mask = 0xff000000;
	    int u8bit = 0x000000ff;
	    
	    //Unpack each 8bit segment into a float, and normalize such that 255 ~= 1.0
	    for(int i = 0; mask != 0 && i < 4; ++i, mask = mask >>> 8) {
	        channels[i] = ((double) ((color & mask) >>> ((3-i)*8) ) ) / u8bit;
	    }
	}
	
	public void setARGB(int color)
	{
		set((color << 8) | (color >>> 24));
	}

	/* *********************************************************************************************
	 * Calculation Methods
	 * *********************************************************************************************/
	public void normalize3()
	{
		//TODO: normalize
	}
	
	public int argb24()
	{
		double r = Math.max(0.0, Math.min(1.0, channels[0]));
		double g = Math.max(0.0, Math.min(1.0, channels[1]));
		double b = Math.max(0.0, Math.min(1.0, channels[2]));

		return  (0x000000ff << 24) +
				(((int)(r * 255)) << 16) +
				(((int)(g * 255)) << 8) +
				(((int)(b * 255)));
	}
	
	public int argb32()
	{
		double a = Math.max(0.0, Math.min(1.0, channels[3]));
		
		return (((int)(a * 255)) << 24) | (0x00FFFFFF & argb24());
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
	
	public Color add3(double d)
	{
		return new Color(channels[0] + d, channels[1] + d, channels[2] + d, channels[3]);
	}
	
	public Color add3M(double d)
	{
		channels[0] += d;
		channels[1] += d;
		channels[2] += d;
		return this;
	}
	
	public Color subtract3(Color c)
	{
		return new Color(channels[0] - c.channels[0], channels[1] - c.channels[1], channels[2] - c.channels[2], channels[3]);
	}
	
	public Color subtract3M(Color c)
	{
		channels[0] -= c.channels[0];
		channels[1] -= c.channels[1];
		channels[2] -= c.channels[2];
		return this;
	}
	
	public Color subtract3(double d)
	{
		return new Color(channels[0] - d, channels[1] - d, channels[2] - d, channels[3]);
	}
	
	public Color subtract3M(double d)
	{
		channels[0] -= d;
		channels[1] -= d;
		channels[2] -= d;
		return this;
	}
	
	public Color add3AfterMultiply3M(Color c, Color d)
	{
		channels[0] += c.channels[0] * d.channels[0];
		channels[1] += c.channels[1] * d.channels[1];
		channels[2] += c.channels[2] * d.channels[2];
		return this;
	}
	
	public Color add3AfterMultiply3M(Color c, double d)
	{
		channels[0] += c.channels[0] * d;
		channels[1] += c.channels[1] * d;
		channels[2] += c.channels[2] * d;
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
	
	public Color multiply3(Color c, double d)
	{
		return new Color(channels[0] * c.channels[0] * d, 
						 channels[1] * c.channels[1] * d, 
						 channels[2] * c.channels[2] * d, 
						 channels[3]);
	}
	
	public Color multiply3M(Color c)
	{
		channels[0] *= c.channels[0];
		channels[1] *= c.channels[1];
		channels[2] *= c.channels[2];
		return this;
	}

	public Color multiply3M(double d)
	{
		channels[0] *= d;
		channels[1] *= d;
		channels[2] *= d;
		return this;
	}
	
	public Color mixWithWhite(double d, double e)
	{
		return new Color(channels[0] * d + e, 
						 channels[1] * d + e, 
						 channels[2] * d + e, 
						 channels[3]);
	}
	
	public Color duplicate()
	{
		return new Color(this);
	}
	
	public Color clamp3M()
	{
		channels[0] = Math.min(1.0, Math.max(0.0, channels[0]));
		channels[1] = Math.min(1.0, Math.max(0.0, channels[1]));
		channels[2] = Math.min(1.0, Math.max(0.0, channels[2]));
		return this;
	}
	
	public double intensity3()
	{
		return (channels[0] + channels[1] + channels[2]) / 3.0;
	}

	
	/* *********************************************************************************************
	 * Texture Override Methods
	 * *********************************************************************************************/
	@Override
	public Color evaluate(IntersectionData data)
	{
		return this;
	}
	

	/* *********************************************************************************************
	 * Print Methods
	 * *********************************************************************************************/
	@Override
	public String toString()
	{
		return "[" + StringUtils.column(""+channels[0], 8) + ", " + 
				 	 StringUtils.column(""+channels[1], 8) + ", " +
				 	 StringUtils.column(""+channels[2], 8) + ", " +
				 	 StringUtils.column(""+channels[3], 8) + "]";
	}
	
	public void print()
	{
		System.out.println(this.toString());
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
	
	public static Color gray()
	{
		return new Color(0.5, 0.5, 0.5, 1.0);
	}
	
	public static Color gray(double ratio)
	{
		return new Color(ratio, ratio, ratio, 1.0);
	}
	
	public static Color red()
	{
		return new Color(1.0, 0.0, 0.0, 1.0);
	}
	
	public static Color darkGreen()
	{
		return new Color(0x23ff27ff);
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
	
	public static Color interpolate(Color color0, Color color1, double t)
	{
		t = Math.min(1.0, Math.max(0.0, t));
		double[] c0 = color0.getChannels();
		double[] c1 = color1.getChannels();
		
	    return new Color((1.0 - t) * c0[0] + t * c1[0],
	                 	 (1.0 - t) * c0[1] + t * c1[1],
	                 	 (1.0 - t) * c0[2] + t * c1[2],
	                 	 (1.0 - t) * c0[3] + t * c1[3]);
	}
	
    /**
     * Sourced from web: http://www.java-gaming.org/index.php?topic=22121.0
     *
     * @param c1 value/color 1 (upper left value/RGB pixel)
     * @param c2 value/color 2 (upper right value/RGB pixel)
     * @param c3 value/color 3 (lower left value/RGB pixel)
     * @param c4 value/color 4 (lower right value/RGB pixel)
     * @param bX x interpolation factor (range 0-256)
     * @param bY y interpolation factor (range 0-256)
     *
     * @return interpolated value(packed RGB pixel) of c1,c2,c3,c4 for given factors bX & bY as three packed unsigned bytes
     *
     * @author Bruno Augier http://dzzd.net/
     */

    public static int interpolate(int c1, int c2, int c3, int c4, int bX, int bY)
    {
       int f24=(bX*bY)>>8;
       int f23=bX-f24;
       int f14=bY-f24;
       int f13=((256-bX)*(256-bY))>>8; // this one can be computed faster
       
       return ((((c1&0xFF00FF)*f13+(c2&0xFF00FF)*f23+(c3&0xFF00FF)*f14+(c4&0xFF00FF)*f24)&0xFF00FF00)|
               (((c1&0x00FF00)*f13+(c2&0x00FF00)*f23+(c3&0x00FF00)*f14+(c4&0x00FF00)*f24)&0x00FF0000))>>>8;
    }
    
    public static int interpolate(Color c1, Color c2, Color c3, Color c4, double bX, double bY)
    {;
    	int usp = (int)(bX * 256);
		int vsp = (int)(bY * 256);
    	return  0xFF000000 | (0x00FFFFFF & interpolate(c1.argb24(), c2.argb24(), c3.argb24(), c4.argb24(), usp, vsp));
    }
	
}
