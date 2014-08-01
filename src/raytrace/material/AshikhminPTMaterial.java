package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IlluminationData;
import raytrace.data.ShadingData;
import raytrace.light.Light;
import raytrace.map.texture.Texture;

public class AshikhminPTMaterial extends Material {
	
	/*
	 * An implementation of the Ashikhmin BRDF
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture diffuseTexture;// = new Color();
	protected Texture specularTexture;// = new Color();

	protected double specularReflectance = 0.5;
	protected double diffuseReflectance = 0.5;
	protected double uExponent = 0.0;
	protected double vExponent = 0.0;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public AshikhminPTMaterial(Texture diffuseTexture, Texture specularTexture, double specularReflectance,
			double diffuseReflectance, double uExponent, double vExponent)
	{
		this.diffuseTexture = diffuseTexture;
		this.specularTexture = specularTexture;
		this.specularReflectance = specularReflectance;
		this.diffuseReflectance = diffuseReflectance;
		this.uExponent = uExponent;
		this.vExponent = vExponent;
	}
	

	@Override
	public Color shade(ShadingData data)
	{	
		Color diffuseTint = diffuseTexture.evaluate(data.getIntersectionData());
		Color specularTint = specularTexture.evaluate(data.getIntersectionData());
		
		
		Color shade = new Color(0x000000ff);
		
		Vector3 point = data.getIntersectionData().getPoint();
		Vector3 normal = data.getIntersectionData().getNormal().normalizeM();
		Vector3 rayDir = data.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(rayDir);
		//If the normal is facing the wrong direction, flip it
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(rayDir);
		}
		
		
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(positiveYAxis)) == 1.0)
			uTangent = normal.cross(cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		
		//Direct Illumination
		IlluminationData ildata;
		Vector3 lightDir;
		Vector3 half;
		for(Light light : data.getRootScene().getLightManager())
		{
			//Get illumination data for the current light
			ildata = light.illuminate(data, point);
			
			lightDir = ildata.getDirection().multiply(-1.0).normalizeM();
			half = halfVector(rayDir, lightDir);

			shade.add3M(ashikminSpecular(ildata.getColor(), specularTint, rayDir, lightDir, normal, uTangent, vTangent, half));
			shade.add3M(ashikminDiffuse(ildata.getColor(), diffuseTint, rayDir, lightDir, normal, uTangent, vTangent, half));
		}
		
		
		//Sampling
		if(data.getRecursionDepth() < DO_NOT_EXCEED_RECURSION_LEVEL)
		{
			Vector3 halfSample;
			Vector3 sample;
			//Roll a random to trace either specular reflections or diffuse reflections
			if(Math.random() < specularReflectance)
			{

				//Specular Sample
				halfSample = createHalfSample(normal, uTangent, vTangent);
				sample = createSample(halfSample, rayDir);
				
				if(sample.dot(normal) > 0.0)
				{
					shade.add3AfterMultiply3M(recurse(data, point, sample, 1.0), specularTint);	
				}
			
			}else{
				
				//Diffuse Sample
				sample = cosineWeightedSample(uTangent, normal, vTangent);

				shade.add3AfterMultiply3M(recurse(data, point, sample, 1.0), diffuseTint);	
			}
		}
		
		return shade;
	}
	
	private Color ashikminDiffuse(Color light, Color diffuseTint, Vector3 k1/*rayDir*/, Vector3 k2/*lightDir*/, Vector3 n, Vector3 u, Vector3 v, Vector3 h)
	{	
		//Diffuse component
		double leadingCoeffDiff = ((28.0 * diffuseReflectance) / (23.0 * Math.PI)) * (1.0 - specularReflectance);
		
		double diffuseCoeff = leadingCoeffDiff * 
				(1.0 - Math.pow( (1.0 - 0.5 * Math.abs(n.dot(k1))) , 5.0 )) * 
				(1.0 - Math.pow( (1.0 - 0.5 * Math.abs(n.dot(k2))) , 5.0 ));
		
		return diffuseTint.multiply3(light).multiply3M(diffuseCoeff);
	}
	
	private Color ashikminSpecular(Color light, Color specularTint, Vector3 k1/*rayDir*/, Vector3 k2/*lightDir*/, Vector3 n, Vector3 u, Vector3 v, Vector3 h)
	{	
		//Specular component
		double leadingCoeffSpec = (1.0 / (8.0 * Math.PI)) * Math.sqrt( (uExponent+ 1.0) * (vExponent + 1.0) );

		//Fresnel component
		double fspec = specularReflectance + (1.0 - specularReflectance) * Math.pow(1.0 - h.dot(k2), 5.0);
		
		double topSpec = Math.pow( h.dot(n) , ( 
				(uExponent * Math.pow(h.dot(u), 2.0) + vExponent * Math.pow(h.dot(v), 2.0)) / (1.0 - Math.pow(h.dot(n), 2.0)) 
				) );
		
		double bottomSpec = Math.abs(h.dot(k2)) * Math.max(Math.abs(n.dot(k1)), Math.abs(n.dot(k2)));
		
		double middleSpec = topSpec / bottomSpec;
		
		//If middle spec is over 1.0 then the specular reflection is generating energy! So clamp it
		if(middleSpec > 1.0)
			middleSpec = 1.0;
		
		double specularCoeff = leadingCoeffSpec * middleSpec * fspec;
		
		if(specularCoeff != specularCoeff || specularCoeff < 0.0)
			specularCoeff = 0.0;
		
		Color spec = specularTint.multiply3(light).multiply3M(specularCoeff);
		
		return spec;
	}
	
	private Vector3 createHalfSample(Vector3 normal, Vector3 uTangent, Vector3 vTangent)
	{
		double epsilon1 = Math.random();
		double epsilon2 = Math.random();
		
		double phi = Math.atan(Math.sqrt( (uExponent + 1.0) / (vExponent + 1.0) ) * Math.tan(0.5 * Math.PI * epsilon1));
		
		double cosPhi = Math.cos(phi);
		double sinPhi = Math.sin(phi);
		
		double theta = Math.acos(Math.pow( (1.0 - epsilon2), ( 1.0 / (uExponent * cosPhi * cosPhi + vExponent * sinPhi * sinPhi + 1.0) ) ));

		double sinTheta = Math.sin(theta);
		
		double xCoeff = sinTheta * cosPhi;
		double yCoeff = sinTheta * sinPhi;
		double zCoeff = Math.cos(theta);
		
		Vector3 half = new Vector3();
		
		double quad = Math.random();
		double quad2 = quad < 0.5 ? -1.0 : 1.0;
		double quad1 = quad < 0.25 || quad >= 0.75 ? 1.0 : -1.0;
		
		half = half.addMultiRightM(normal, zCoeff).addMultiRightM(vTangent, yCoeff * quad2).addMultiRightM(uTangent, xCoeff * quad1);
		
		return half.normalizeM();
	}
	
	private Vector3 createSample(Vector3 half, Vector3 k1/*rayDir*/)
	{
		return k1.multiply(-1.0).addM(half.multiply(half.dot(k1) * 2.0)).normalizeM();
	}

}