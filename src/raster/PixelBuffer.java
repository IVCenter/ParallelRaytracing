package raster;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import process.logging.Logger;

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
	

	/* *********************************************************************************************
	 * Utility Methods
	 * *********************************************************************************************/
	public void writeToFile(String filePath)
	{
		//Try to write this pixel buffer to a bmp formatted image at the given path
		try {
			
			//Fix file path to have a png extension
			if(!filePath.endsWith(".png"))
				filePath += ".png";
			
			//Create an empty file to write to (if it doesnt already exist), and make sure we can write to it
			File file = new File(filePath);
			file.createNewFile();
			if(!file.canWrite()) {
				Logger.error(-1, "Can not write to file [" + filePath + "].");
				return;
			}
			
			//Create a new bufferd image to store the pixel for writing
			BufferedImage bufimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			bufimg.setRGB(0, 0, width, height, buffer, 0, width);

			
			Logger.progress(-1, "Begin saving pixel buffer to file [" + filePath + "].");
			
			//Save the image to disk at filePath
			if(!ImageIO.write(bufimg, "png", file))
				Logger.error(-1, "Can not find an appropriate file writer for the given type.");
			
			Logger.progress(-1, "End saving pixel buffer");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
