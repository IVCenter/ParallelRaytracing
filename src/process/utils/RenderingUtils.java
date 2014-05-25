package process.utils;

import java.util.ArrayList;

import math.Ray;
import process.logging.Logger;
import raytrace.camera.Camera;

public class RenderingUtils {

	/* ********************************************************************************************************
	 * Rendering Utility Methods
	 * *******************************************************************************************************/
	
	/**
	 * 
	 * @param buffer
	 * @param camera
	 * @return
	 */
	public static void unpackPixels(int[] buffer, int[] pixels, Camera camera)
	{
		int index = 0;
		
		try {
			
			for(Ray ray : camera)
				buffer[(int)(ray.getPixelY() * camera.getPixelWidth() + ray.getPixelX())] = pixels[index++];
			
		} catch(Exception e)
		{
			Logger.error(-29, "RenderingUtils: Encountered an error while unpacking pixels.");
			Logger.error(-29, StringUtils.stackTraceToString(e));
		}
	}
	
	/**
	 * 
	 * @param buffer
	 * @param camera
	 * @return
	 */
	public static int[] packPixels(int[] buffer, Camera camera)
	{
		ArrayList<Integer> pixels = new ArrayList<Integer>();
		
		try{
			
			for(Ray ray : camera)
			{
				pixels.add(buffer[(int)(ray.getPixelY() * camera.getPixelWidth() + ray.getPixelX())]);
			}
			
			//Pack the pixels into an array
			int[] arr = new int[pixels.size()];
			for(int i = 0; i < pixels.size(); ++i)
			{
				arr[i] = pixels.get(i);
			}
			
			return arr;
			
		} catch(Exception e)
		{
			Logger.error(-30, "RenderingUtils: Encountered an error while packing pixels.");
			Logger.error(-30, StringUtils.stackTraceToString(e));
		}
		
		return new int[0];
	}

}
