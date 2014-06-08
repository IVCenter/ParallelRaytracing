package raytrace.material;

import math.Vector4;
import raytrace.color.Color;
import raytrace.data.ShadingData;
import raytrace.map.texture.Texture;

public class DielectricPTMaterial extends Material{
	
	/*
	 * A path tracing implementation of a dielectric material
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture beerTexture;
	protected double refractiveIndex = 1.0;
	protected double roughness = 0.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DielectricPTMaterial(Texture beerTexture, double refractiveIndex)
	{
		this.beerTexture = beerTexture;
		this.refractiveIndex = refractiveIndex;
	}
	
	public DielectricPTMaterial(Texture beerTexture, double refractiveIndex, double roughness)
	{
		this.beerTexture = beerTexture;
		this.refractiveIndex = refractiveIndex;
		this.roughness = roughness;
	}


	/* *********************************************************************************************
	 * Material Override
	 * *********************************************************************************************/
	@Override
	public Color shade(ShadingData data)
	{
		//Get color from texture
		Color tint = beerTexture.evaluate(data.getIntersectionData());
		
		//Setup the point of intersection
		Vector4 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		Vector4 normal = data.getIntersectionData().getNormal();
		
		//Ray Direction
		Vector4 rayDir = (new Vector4(data.getRay().getDirection())).normalize3();
		
		
		double DdotN = normal.dot3(rayDir);
		double thisRefractiveIndex = refractiveIndex;
		boolean exiting = false;
		
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			//TODO: Would it be better to use a refractiveIndex stack?
			thisRefractiveIndex = AIR_REFRACTIVE_INDEX;//TODO: Is this the right test, and right place to assume we are exiting the material?
			normal = normal.multiply3(-1.0);
			DdotN *= -1.0;
			
			exiting = true;
		}
		
		
		//Pre-calculate some values for refraction
		double incomingRefractiveIndex = data.getRefractiveIndex();
		double refractiveRatio = incomingRefractiveIndex / thisRefractiveIndex;
		
		//Calculate the refraction direction
		double phiDiscrim = 1.0 - (refractiveRatio * refractiveRatio) *
							(1.0 - (DdotN * DdotN));
		

		//Calculate reflectiveness
		double reflectiveCoeff = 0.0;
		//If there was no refraction, fix reflective coeff
		if(phiDiscrim <= 0.0)
		{
			//Purely reflective
			reflectiveCoeff = 1.0;
		}else{
			//Calculate the reflective coefficient
			double baseReflectiveCoeff = (thisRefractiveIndex - 1.0) / (thisRefractiveIndex + 1.0);
			baseReflectiveCoeff *= baseReflectiveCoeff;
			
			//Schlick Approx
			reflectiveCoeff = baseReflectiveCoeff + (1.0 - baseReflectiveCoeff) * Math.pow(1.0 - Math.abs(DdotN), 5.0);
		}
		
		
		Color recursionColor = null;
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			double recursionDirection = Math.random();
			
			//If direction < refrectance, go reflectin
			if(recursionDirection < reflectiveCoeff)
			{
				
				recursionColor = reflect(data, point, normal, exiting ? refractiveIndex : AIR_REFRACTIVE_INDEX);
					
			}else{
				
				Vector4 thetaSide = rayDir.addMultiRight3M(normal, -DdotN).multiply3M(refractiveRatio);
				Vector4 refracDir = thetaSide.addMultiRight3M(normal, -Math.sqrt(phiDiscrim)).normalize3();
				
				//0.0 is no roughness, 1.0 is lots of roughness
				//TODO: Use something better here......
				if(roughness > 0.0)
				{
					Vector4 roughDir = new Vector4();
					Vector4 offset = new Vector4();
					do
					{
						offset.set(Math.random() * roughness, Math.random() * roughness, Math.random() * roughness, 0);
						roughDir.set(refracDir);
						roughDir.add3M(offset);
					} while(roughDir.dot3(refracDir) <= 0.0);
					
					refracDir = roughDir.normalize3();
				}
				
				recursionColor = recurse(data, point, refracDir, exiting ? AIR_REFRACTIVE_INDEX : refractiveIndex);
				
			}
		}else{
			recursionColor = new Color();
		}
		
					
		//If the ray is exiting the surface, then apply beers law to all light that was collected recursively
		if(exiting)
		{
			double d = data.getIntersectionData().getDistance();
			double[] a = tint.getChannels();
			Color beerColor = new Color(Math.exp(-1.0 * Math.log(a[0]) * d), 
										Math.exp(-1.0 * Math.log(a[1]) * d), 
										Math.exp(-1.0 * Math.log(a[2]) * d));

			recursionColor = recursionColor.multiply3(beerColor);
		}
		
		return recursionColor;
	}

}
