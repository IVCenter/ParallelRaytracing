package raytrace.material;

import math.Ray;
import math.Vector4;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.IntersectionData;
import raytrace.data.ShadingData;
import raytrace.light.Light;

public class DielectricMaterial extends Material{
	
	/*
	 * An implementation of a material that is a diffuse color and affected by light
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Color tint;
	double refractiveIndex = 1.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public DielectricMaterial(Color tint, double refractiveIndex)
	{
		this.tint = tint;
		this.refractiveIndex = refractiveIndex;
	}
	

	@Override
	public Color shade(ShadingData data)
	{
		Color shade = new Color(0x000000ff);
		
		Vector4 point = data.getIntersectionData().getPoint();
		
		//Setup the normal
		//TODO: Find out why these arent normalized to begin with!
		Vector4 normal = data.getIntersectionData().getNormal();//.normalize3();
		double DdotN = normal.dot3(data.getRay().getDirection());
		if(DdotN > 0.0) {
			refractiveIndex = 1.0;//TODO: Is this the right test, and right place to assume we are exiting the material?
			normal = normal.multiply3(-1.0);
			DdotN *= -1.0;
		}
		
		
		
		IlluminationData ildata;
		
		IntersectionData shadowData;
		
		//Diffuse lighting and shadows
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(point);
			
			//Get the shadow data for the current light
			shadowData = shadowed(data.getRootScene(), 
					new Ray(point, ildata.getDirection().multiply3(-1.0), 0, 0), 
					ildata.getDistance());
			
			//If we are not in shadow, add to the shade
			if(shadowData == null)
				shade.add3M(diffuse(ildata.getColor(), normal, ildata.getDirection()));
		}
		
		
		//Pre-calculate some values for refraction
		double incomingRefractiveIndex = data.getRefractiveIndex();
		Vector4 rayDir = data.getRay().getDirection();
		//DdotN = Math.abs(DdotN);
		
		//Calculate the refraction direction
		double refractiveRatio = incomingRefractiveIndex / refractiveIndex;
		Vector4 thetaSide = rayDir.subtract3(normal.multiply3(DdotN)).multiply3(refractiveRatio);
		double phiDiscrim = 1.0 - (refractiveRatio * refractiveRatio) *
							(1.0 - (DdotN * DdotN));
		Vector4 phiSide = normal.multiply3(Math.sqrt(phiDiscrim));
		
		Vector4 refracDir = thetaSide.subtract3(phiSide);
		
		Color refractColor = new Color();
		if(phiDiscrim > 0.0 && data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			refractColor = recurse(data, point, refracDir, refractiveIndex);
			//refractColor.print();
		}
		
		
		
		
		
		//Calculate the reflective coefficient
		double baseReflectiveCoeff = (refractiveIndex - 1.0) / (refractiveIndex + 1.0);
		baseReflectiveCoeff *= baseReflectiveCoeff;
		double reflectiveCoeff = baseReflectiveCoeff + (1.0 - baseReflectiveCoeff) * Math.pow(1.0 - Math.abs(DdotN), 5.0);
		
		//If there was no refraction, fix reflective coeff
		if(phiDiscrim <= 0.0)
			reflectiveCoeff = 1.0;

		//System.out.println("5pow: " + (Math.pow(1.0 - Math.abs(DdotN), 5.0)));
		

		//If reflective, go divin'
		Color rflectColor = new Color();
		if(reflectiveCoeff != 0.0 && data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			rflectColor = reflect(data, point, normal);
			//rflectColor.print();
		}
		
		
		//TODO: How does mix color
		//TODO: Beers law on tint!
		Color refractiveColor = refractColor.multiply3(tint).multiply3(1.0 - reflectiveCoeff);
		Color reflectiveColor = rflectColor.multiply3(reflectiveCoeff);
		
		//System.out.println("RefractColor: ");
		//refractColor.print();
		//System.out.println("RefractCoeff: " + (1.0 - reflectiveCoeff));
		//System.out.println("RefractiveColor: ");
		//refractiveColor.print();
		
		
		//return refractColor;
		return refractiveColor.add3(reflectiveColor);
	}

}
