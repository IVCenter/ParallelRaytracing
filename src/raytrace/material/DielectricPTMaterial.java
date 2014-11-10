package raytrace.material;

import math.Vector3;
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
		Vector3 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		Vector3 normal = data.getIntersectionData().getNormal();
		
		//Ray Direction
		Vector3 rayDir = (new Vector3(data.getRay().getDirection())).normalizeM();
		
		//Distance
		double distance = data.getIntersectionData().getDistance();
		
		double DdotN = normal.dot(rayDir);
		double thisRefractiveIndex = refractiveIndex;
		boolean exiting = false;
		
		//If the ray direction and the normal have an angle of less than Pi/2, then the ray is exiting the material
		if(DdotN > 0.0) {
			//TODO: Would it be better to use a refractiveIndex stack?
			thisRefractiveIndex = AIR_REFRACTIVE_INDEX;//TODO: Is this the right test, and right place to assume we are exiting the material?
			normal = normal.multiply(-1.0);
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
				
				recursionColor = reflect(data, point, normal, exiting ? refractiveIndex : AIR_REFRACTIVE_INDEX, false);
					
			}else{
				
				Vector3 thetaSide = rayDir.addMultiRightM(normal, -DdotN).multiplyM(refractiveRatio);
				Vector3 refracDir = thetaSide.addMultiRightM(normal, -Math.sqrt(phiDiscrim)).normalizeM();
				
				//0.0 is no roughness, 1.0 is lots of roughness
				//TODO: Use something better here......
				if(roughness > 0.0)
				{
					Vector3 roughDir = new Vector3();
					Vector3 offset = new Vector3();
					do
					{
						offset.set(Math.random() * roughness, Math.random() * roughness, Math.random() * roughness);
						roughDir.set(refracDir);
						roughDir.addM(offset);
					} while(roughDir.dot(refracDir) <= 0.0);
					
					refracDir = roughDir.normalizeM();
				}
				
				recursionColor = recurse(data, point, refracDir, exiting ? AIR_REFRACTIVE_INDEX : refractiveIndex, false);
				
			}
		}else{
			recursionColor = new Color();
		}
		
					
		//If the ray is exiting the surface, then apply beers law to all light that was collected recursively
		if(exiting)
		{
			double[] a = tint.getChannels();
			Color beerColor = new Color(Math.exp(-1.0 * Math.log(a[0]) * distance), 
										Math.exp(-1.0 * Math.log(a[1]) * distance), 
										Math.exp(-1.0 * Math.log(a[2]) * distance));

			recursionColor = recursionColor.multiply3(beerColor);
		}
		
		return recursionColor;
	}

}
