package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;
import raytrace.map.texture._3D.Texture3D;

public class SubSurfaceDiffusePTTestMaterial extends Material{
	
	/*
	 * A test material
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Material surfaceMaterial;
	protected Texture3D volumeTexture;
	protected double scatterCoeff = 0.5;
	protected double refractiveIndex = 1.0;
	protected double roughness = 0.0;
	protected int scatterSampleCount = 1;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public SubSurfaceDiffusePTTestMaterial(Material surfaceMaterial, Texture3D volumeTexture, double scatterCoeff,
			double refractiveIndex, double roughness, int scatterSampleCount)
	{
		this.surfaceMaterial = surfaceMaterial;
		this.volumeTexture = volumeTexture;
		this.scatterCoeff = scatterCoeff;
		this.refractiveIndex = refractiveIndex;
		this.roughness = roughness;
		this.scatterSampleCount = scatterSampleCount;
	}


	/* *********************************************************************************************
	 * Material Override
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{	
		//Setup the normal
		Vector4 normal = data.getIntersectionData().getNormal();
		
		//Ray Direction
		Vector4 rayDir = (new Vector4(data.getRay().getDirection())).normalize3();
		
		
		
		
		/*
		 * On entrance:
		 * 		Roll for surface reflection or subsurface lighting
		 * 		If surface:
		 * 			shade using given material
		 *		if sub surface:
		 * 			bend ray using index of refrection
		 * 			scatter via roughness parameter
		 * 			recurse for lighting
		 * 
		 * On exit:
		 * 		roll for surface reflection or sub surface pass through
		 * 		if surface:
		 * 			return black (no light made it in)
		 * 		if sub surface:
		 * 			collect all light for that point (direct and indirect)
		 * 				do we use a evenly weighted hemisphere for indirect?
		 * 			using the subsurface sampling parameter, 
		 * 				sample volume texture along the light ray
		 * 				for each sample segment, apply beers law to the light
		 * 				return the attenuated light
		 * 			
		 * 		
		 * 
		 */
		
		double DdotN = normal.dot3(rayDir);
		
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		Color recursionColor = null;
		if(DdotN > 0.0) 
		{
			recursionColor = exiting(data);
			
		}else{
		
			recursionColor = entering(data);
		}
		
		return recursionColor;
	}

	
	
	
	
	
	
	
	private Color entering(ShadingData data)
	{
		//Caluclate the reflected light 
		Color recursionColor = null;
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			double recursionDirection = Math.random();
			
			//If direction < refrectance, go reflectin
			if(recursionDirection > scatterCoeff)
			{
				//If no scatter, Sample surface material
				recursionColor = surfaceMaterial.shade(data);
					
			}else{
				//If scatter, get recursing
				//Setup the point of intersection
				Vector4 point = data.getIntersectionData().getPoint();
				
				//Setup the normal
				Vector4 normal = data.getIntersectionData().getNormal();
				
				//Ray Direction
				Vector4 rayDir = (new Vector4(data.getRay().getDirection())).normalize3();
				
				
				double DdotN = normal.dot3(rayDir);
				double thisRefractiveIndex = refractiveIndex;
				
				
				//Pre-calculate some values for refraction
				double incomingRefractiveIndex = data.getRefractiveIndex();
				double refractiveRatio = incomingRefractiveIndex / thisRefractiveIndex;
				
				//Calculate the refraction direction
				double phiDiscrim = 1.0 - (refractiveRatio * refractiveRatio) *
									(1.0 - (DdotN * DdotN));
				
				if(phiDiscrim >= 0)
				{
				
					Vector4 thetaSide = rayDir.addMultiRight3M(normal, -DdotN).multiply3M(refractiveRatio);
					Vector4 refracDir = thetaSide.addMultiRight3M(normal, -Math.sqrt(phiDiscrim)).normalize3();
					
					//0.0 is no roughness, 1.0 is lots of roughness
					//TODO: Use something better here......
					if(roughness > 0.0 && refracDir.dot3(normal) < 0.0)
					{
						Vector4 roughDir = new Vector4();
						Vector4 offset = new Vector4();
						do
						{
							offset = uniformSphereSample().multiply3M(roughness);
							roughDir.set(refracDir);
							roughDir.add3M(offset);
						} while(roughDir.dot3(normal) > 0.0);
						
						refracDir = roughDir.normalize3();
					}
					
					recursionColor = recurse(data, point, refracDir, refractiveIndex);
				}else{
					recursionColor = new Color();
				}
				
			}
		}else{
			recursionColor = new Color();
		}
		
		return recursionColor;
	}
	
	
	
	
	
	private Color exiting(ShadingData data)
	{

		//Setup the point of intersection
		Vector4 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		Vector4 normal = data.getIntersectionData().getNormal();
		
		
		Color recursionColor = null;
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			double recursionDirection = Math.random();
			
			//If direction < refrectance, go reflectin
			if(recursionDirection > scatterCoeff)
			{
				//If no scatter, Sample surface material
				recursionColor = new Color();
					
			}else{
				
				//Direct Illumination (diffuse)
				IlluminationData ildata;
				Color shade = new Color();
				Color white = Color.white();
				for(Light light : data.getRootScene().getLightManager())
				{
					//Get illumination data for the current light
					ildata = light.illuminate(data, point);
					
					shade.add3M(diffuse(white, normal, ildata.getDirection()));
				}
				
				
				//Indirect Illumination
				//Sampling
				if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
				{
					//Basis
					Vector4 uTangent;
					Vector4 vTangent;
					
					if(Math.abs(normal.dot3(positiveYAxis)) == 1.0)
						uTangent = normal.cross3(cosineWeightedSample()).normalize3();
					else
						uTangent = normal.cross3(positiveYAxis).normalize3();
					vTangent = uTangent.cross3(normal).normalize3();
					
					//Sample a random point
					Vector4 sampleDir = uniformHemisphereSample(uTangent, normal, vTangent);
					
					//Add the direct shading and samples shading together
					shade.add3M(recurse(data, point, sampleDir, 1.0));
				}
				
				
				double segmentLength = data.getIntersectionData().getDistance() / (double)scatterSampleCount;
				Vector4 origin = data.getRay().getOrigin();
				Vector4 path = point.subtract3(origin);
				
				Vector4 samplePoint = new Vector4();
				Color beerColor = new Color();
				for(int i = 0; i < scatterSampleCount; i++)
				{
					//Get an internal sample point along the path
					samplePoint.set(origin);
					samplePoint.addMultiRight3M(path, ((double)(scatterSampleCount-i)) / (double) scatterSampleCount);
					double[] a = volumeTexture.evaluate(samplePoint.get(0), samplePoint.get(0), samplePoint.get(0)).getChannels();
					
					//Brew up some beer color
					beerColor.set(Math.exp(-1.0 * Math.log(a[0]) * segmentLength), 
												Math.exp(-1.0 * Math.log(a[1]) * segmentLength), 
												Math.exp(-1.0 * Math.log(a[2]) * segmentLength));
					
					//Get faded
					shade = shade.multiply3M(beerColor);
				}
				
				recursionColor = shade;
			}
			
		}else{
			recursionColor = new Color();
		}
		
		
		return recursionColor;
	}
}
