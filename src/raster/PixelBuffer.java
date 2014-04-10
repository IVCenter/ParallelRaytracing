package raster;

public class PixelBuffer {
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	protected int[] buffer;
	protected int width;
	protected int height;

	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PixelBuffer(int width, int height)
	{
		buffer = new int[width * height];
		this.width = width;
		this.height = height;
	}
	

	/* *********************************************************************************************
	 * Getter
	 * *********************************************************************************************/
	public int[] getPixels() { return buffer; }


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

}
