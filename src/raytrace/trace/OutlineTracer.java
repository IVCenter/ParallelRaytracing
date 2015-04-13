package raytrace.trace;

import math.Vector3;
import math.ray.CircularRayStencil;
import math.ray.Ray;
import math.ray.RayStencil;
import process.logging.Logger;
import raster.Pixel;
import raster.PixelBuffer;
import raster.RenderBuffer;
import raytrace.camera.Camera;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.data.RenderData;
import raytrace.framework.Tracer;
import raytrace.map.texture.Texture;
import raytrace.scene.Scene;

public class OutlineTracer implements Tracer {
	
	/*
	 * A tracer for outlines, creases, intersections, etc.
	 * Based on this paper: http://www.sci.utah.edu/~roni/research/projects/NPR-lines/NPR-lines.NPAR09.pdf
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected RayStencil stencil;
	
	protected Texture silhouetteTexture;
	protected Texture occlusionTexture;
	protected Texture creaseTexture;
	
	protected double depthThreshold;
	protected double normalThreshold;

	protected double silhouetteExponent;
	protected double occlusionExponent;
	protected double creaseExponent;
	
	protected double silhouetteOpacity;
	protected double occlusionOpacity;
	protected double creaseOpacity;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public OutlineTracer()
	{
		super();
		
		stencil = new CircularRayStencil(0.01, 2, 16);
		
		silhouetteTexture = new Color(0x111111ff);//0x111111ff
		occlusionTexture = new Color(0x111111ff);
		creaseTexture = new Color(0x111111ff);
		
		depthThreshold = 0.05;
		normalThreshold = 0.2;
		
		silhouetteExponent = 3.0;
		occlusionExponent = 1.0;
		creaseExponent = 0.5;
		
		silhouetteOpacity = 1.0;
		occlusionOpacity = 1.0;
		creaseOpacity = 0.6;
	}
	

	/* *********************************************************************************************
	 * Configuration
	 * *********************************************************************************************/
	@Override
	public void trace(RenderData data)
	{
		//Pixels
		PixelBuffer pixelBuffer = data.getPixelBuffer();
		RenderBuffer inputRenderBuffer = data.getInputRenderBuffer();
		RenderBuffer outputRenderBuffer = data.getOutputRenderBuffer();
		int[] pb_Pixels = pixelBuffer.getPixels();
		Pixel[] orb_Pixels = outputRenderBuffer.getPixels();
		Pixel[] irb_Pixels = inputRenderBuffer.getPixels();
		Camera camera = data.getCamera();
		Scene scene = data.getScene();
		
		//Trace each ray in the camera
		IntersectionData idata;
		//IntersectionData idata = new IntersectionData();//Allocate once
		
		//Build a ray data object
		RayData rdata = new RayData();
		
		//ray count and color storage for super sampling support
		double sampleCount = 0;
		Color color = new Color();
		Pixel iPixel, oPixel;
		int pixelIndex;
		
		//Primary ray data
		int primarySurfaceID = 0;
		double primaryDepth = 0.0;
		Vector3 primaryNormal;
		
		//Counters
		double samplesWithDifferentSurfaceID = 0.0;
		double samplesWithSufficientlDepthDifference = 0.0;
		double samplesWithSufficientlNormalDifference = 0.0;
		
		//Intersection Data object for querying textures
		IntersectionData textureQueryIData = null;
		
		//Reusable texcoord vector
		Vector3 texcoord = new Vector3();

		//Depth of the closest hit, used for selecting an idata to use for texturing
		double clostestHitDepth = Double.POSITIVE_INFINITY;
		
		//For each ray, calculate the pixel color
		for(Ray rays : camera)
		{
			//reset ray count and color
			sampleCount = 0.0;
			pixelIndex = rays.getPixelX() + rays.getPixelY() * pixelBuffer.getWidth();
			iPixel = irb_Pixels[pixelIndex];
			oPixel = orb_Pixels[pixelIndex];
			color.set(iPixel.getColor());
			
			//Set the current primary ray
			rays.setRandomValue(Math.random());
			rdata.setRay(rays);
			//sdata.setRay(rays);

			//Set the UV texcoords for the screen space
			texcoord.set(
					((double)rays.getPixelX()) / camera.getPixelWidth(), 
					((double)rays.getPixelX()) / camera.getPixelHeight(), 
					0);
			
			textureQueryIData = null;
			clostestHitDepth = Double.POSITIVE_INFINITY;
			
			
			//Get the ray-scene intersection data
			idata = scene.intersects(rdata);
			
			
			//If there was an intersection, do shading
			if(idata != null)
			{
				primarySurfaceID = idata.getSurfaceID();
				primaryDepth = idata.getDistance();
				primaryNormal = idata.getNormal();
				
				clostestHitDepth = idata.getDistance();
				textureQueryIData = idata;
				
			//If there wasn't an intersection, use the sky intersection
			}else{
				
				primarySurfaceID = 0;
				primaryDepth = Double.POSITIVE_INFINITY;
				primaryNormal = rays.getDirection().multiply(-1.0);
			}
			
			
			/*
			 * Shoot primary ray
			 * Collect data
			 * 
			 * Shoot stencil rays
			 * Collect data
			 * Determine edge type
			 * 
			 * Determine edge strength
			 * 
			 * Interpolate edge color with existing pixel color
			 */
			
			/*
			 * 1. m = Number of samples with a different surfaceID than the primary
			 * 2. md = Number of samples with a distance from the primary > some threshold (function?)
			 * 3. mg = Gradient from sampling normals
			 * 
			 * 
			 * if m > 0, use silhouette or intersection edge (separate silhouette and intersection into two line types?)
			 * if m == 0 and mg > threshold, crease
			 * if m == 0 and md > 0, self-occlusion (use a function? Thicker if distance larger?)
			 */
			
			samplesWithDifferentSurfaceID = 0.0;
			samplesWithSufficientlDepthDifference = 0.0;
			samplesWithSufficientlNormalDifference = 0.0;
			
			//Iterate the stencil
			for(Ray ray : stencil.stencil(rays))
			{
				sampleCount += 1.0;
				rdata.setRay(ray);
				
				//Get the ray-scene intersection data
				idata = scene.intersects(rdata);
				
				//If there was an intersection, do shading
				if(idata != null)
				{
					if(primarySurfaceID != idata.getSurfaceID())
						samplesWithDifferentSurfaceID += 1.0;

					if(Math.abs(primaryDepth-idata.getDistance()) > 
						(depthThreshold * (idata.getDistance() / (-1.0 * rays.getDirection().dot(idata.getNormal())))) )
						samplesWithSufficientlDepthDifference += 1.0;
					
					if(1.0 - primaryNormal.dot(idata.getNormal()) > normalThreshold)
						samplesWithSufficientlNormalDifference += 1.0;
					
					if(textureQueryIData == null || idata.getDistance() < clostestHitDepth)
					{
						clostestHitDepth = idata.getDistance();
						textureQueryIData = idata;
					}
					
				//If there wasn't an intersection, ray hit the infinite bounds
				}else{

					if(primarySurfaceID != 0)
						samplesWithDifferentSurfaceID += 1.0;

					samplesWithSufficientlDepthDifference += 1.0;
					
					samplesWithSufficientlNormalDifference += 1.0;
				}
			}
			
			
			//If the camera is dirty stop rendering
			if(camera.isDirty()) {
				Logger.progress(-1, "RayTracer: Detected a dirty camera, cancelling rendering.");
				break;
			}
			
			
			//If we can't query the texture (no rays hit an object)
			//Then leave the pixel as it is
			if(textureQueryIData == null)
				continue;
			
			textureQueryIData.setTexcoord(texcoord);
			

			//From paper
			//Determine the darkness value of the silhouette, self-occlusion, and crease edge types
			double silhouetteDarkness = 
					Math.pow(Math.abs(samplesWithDifferentSurfaceID - 0.5*sampleCount) / (0.5*sampleCount), silhouetteExponent);
			
			double occlusionDarkness = 
					Math.pow(Math.abs(samplesWithSufficientlDepthDifference - 0.5*sampleCount) / (0.5*sampleCount), occlusionExponent);
			
			double creaseDarkness =
					Math.pow(Math.abs(samplesWithSufficientlNormalDifference - 0.5*sampleCount) / (0.5*sampleCount), creaseExponent);

			
			//Weight the individual line darkness values with their opacity multiplier
			silhouetteDarkness = (1.0 - silhouetteDarkness) * silhouetteOpacity;
			occlusionDarkness = (1.0 - occlusionDarkness) * occlusionOpacity;
			creaseDarkness = (1.0 - creaseDarkness) * creaseOpacity;
			
			
			//Sum the three line darkness values
			double darknessSum = silhouetteDarkness + occlusionDarkness + creaseDarkness;
			
			//If the darkness is 0.0, then the original color is left unchanged
			darknessSum = Math.abs(darknessSum);
			if(darknessSum == 0.0)
				continue;
			
			//Based on individual darkness values, interpolate the line color
			Color lineColor = silhouetteTexture.evaluate(textureQueryIData).multiply3(silhouetteDarkness);
			lineColor.add3AfterMultiply3M(occlusionTexture.evaluate(textureQueryIData), occlusionDarkness);
			lineColor.add3AfterMultiply3M(creaseTexture.evaluate(textureQueryIData), creaseDarkness);
			lineColor.multiply3M(1.0 / darknessSum);
			

			//Invert dakrness value for use in interpolation with the existing pixel color
			silhouetteDarkness = 1.0 - silhouetteDarkness;
			occlusionDarkness = 1.0 - occlusionDarkness;
			creaseDarkness = 1.0 - creaseDarkness;
			
			//Use the darkest line dakrness value as the one for interpolation with the existing pixel color
			double darkness = Math.min(Math.min(silhouetteDarkness, occlusionDarkness), creaseDarkness);
			
			//Interpolate the line and pixel colors
			color.multiply3M(darkness).add3AfterMultiply3M(lineColor, 1.0 - darkness);
			
			//Update the buffers
			oPixel.getColor().set(color);
			pb_Pixels[pixelIndex] = color.rgb32();
		}
		
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public RayStencil getStencil() {
		return stencil;
	}

	public void setStencil(RayStencil stencil) {
		this.stencil = stencil;
	}

	public Texture getSilhouetteTexture() {
		return silhouetteTexture;
	}

	public void setSilhouetteTexture(Texture silhouetteTexture) {
		this.silhouetteTexture = silhouetteTexture;
	}

	public Texture getOcclusionTexture() {
		return occlusionTexture;
	}

	public void setOcclusionTexture(Texture occlusionTexture) {
		this.occlusionTexture = occlusionTexture;
	}

	public Texture getCreaseTexture() {
		return creaseTexture;
	}

	public void setCreaseTexture(Texture creaseTexture) {
		this.creaseTexture = creaseTexture;
	}

	public double getDepthThreshold() {
		return depthThreshold;
	}

	public void setDepthThreshold(double depthThreshold) {
		this.depthThreshold = depthThreshold;
	}

	public double getNormalThreshold() {
		return normalThreshold;
	}

	public void setNormalThreshold(double normalThreshold) {
		this.normalThreshold = normalThreshold;
	}

	public double getSilhouetteExponent() {
		return silhouetteExponent;
	}

	public void setSilhouetteExponent(double silhouetteExponent) {
		this.silhouetteExponent = silhouetteExponent;
	}

	public double getOcclusionExponent() {
		return occlusionExponent;
	}

	public void setOcclusionExponent(double occlusionExponent) {
		this.occlusionExponent = occlusionExponent;
	}

	public double getCreaseExponent() {
		return creaseExponent;
	}

	public void setCreaseExponent(double creaseExponent) {
		this.creaseExponent = creaseExponent;
	}

	public double getSilhouetteOpacity() {
		return silhouetteOpacity;
	}

	public void setSilhouetteOpacity(double silhouetteOpacity) {
		this.silhouetteOpacity = silhouetteOpacity;
	}

	public double getOcclusionOpacity() {
		return occlusionOpacity;
	}

	public void setOcclusionOpacity(double occlusionOpacity) {
		this.occlusionOpacity = occlusionOpacity;
	}

	public double getCreaseOpacity() {
		return creaseOpacity;
	}

	public void setCreaseOpacity(double creaseOpacity) {
		this.creaseOpacity = creaseOpacity;
	}

}