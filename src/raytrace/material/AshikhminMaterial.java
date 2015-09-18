package raytrace.material;

import math.Vector3;
import raytrace.color.Color;
import raytrace.data.IntersectionData;
import raytrace.data.RayData;
import raytrace.map.texture.Texture;

public class AshikhminMaterial extends Material {
	
	/*
	 * An implementation of the Ashikhmin BRDF
	 */
	
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static final int SPECULAR = 0;
	protected static final int DIFFUSE = 1;
	
	
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
	public AshikhminMaterial(Texture diffuseTexture, double diffuseReflectance, Texture specularTexture,
			double specularReflectance, double uExponent, double vExponent)
	{
		this.diffuseTexture = diffuseTexture;
		this.specularTexture = specularTexture;
		this.specularReflectance = specularReflectance;
		this.diffuseReflectance = diffuseReflectance;
		this.uExponent = uExponent;
		this.vExponent = vExponent;
	}
	

	/* *********************************************************************************************
	 * Material Overrides
	 * *********************************************************************************************/
	@Override
	public RayData sample(IntersectionData idata, RayData rdata)
	{
		Vector3 point = idata.getPoint();
		Vector3 normal = idata.getNormal().normalizeM();
		Vector3 rayDir = rdata.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(rayDir);
		//If the normal is facing the wrong direction, flip it
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(rayDir);
		}

		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(Vector3.positiveYAxis)) == 1.0)
			uTangent = normal.cross(Vector3.cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(Vector3.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		Vector3 halfSample;
		Vector3 sample;
		RayData newRData = new RayData(point, null, RayData.Type.REFLECT);
		
		//Roll a random to trace either specular reflections or diffuse reflections
		if(Math.random() < specularReflectance)
		{
			//Specular Sample
			halfSample = createHalfSample(normal, uTangent, vTangent);
			sample = createSample(halfSample, rayDir);
			
			if(sample.dot(normal) > 0.0)
			{
				newRData.getRay().setDirection(sample);
				newRData.setSubType(SPECULAR);
				return newRData;
			}
		
		}else{
			
			//Diffuse Sample
			sample = Vector3.cosineWeightedSample(uTangent, normal, vTangent);
			newRData.getRay().setDirection(sample);
			newRData.setSubType(DIFFUSE);
			return newRData;
		}
		
		return null;
	}

	@Override
	public Color evaluateDirectLight(IntersectionData idata, RayData rdata, Color light, Vector3 lightDirection)
	{
		Color diffuseTint = diffuseTexture.evaluate(idata);
		Color specularTint = specularTexture.evaluate(idata);
		
		Vector3 normal = idata.getNormal().normalizeM();
		Vector3 rayDir = rdata.getRay().getDirection().multiply(-1.0).normalizeM();
		
		double DdotN = normal.dot(rayDir);
		//If the normal is facing the wrong direction, flip it
		if(DdotN < 0.0) {
			normal = normal.multiply(-1.0);
			DdotN = normal.dot(rayDir);
		}

		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(Vector3.positiveYAxis)) == 1.0)
			uTangent = normal.cross(Vector3.cosineWeightedSample()).normalizeM();
		else
			uTangent = normal.cross(Vector3.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		Color result = new Color();
		Vector3 lightDir = lightDirection.multiply(-1.0).normalizeM();
		Vector3 half = Vector3.halfVector(rayDir, lightDir);

		result.add3M(ashikminSpecular(light, specularTint, rayDir, lightDir, normal, uTangent, vTangent, half));
		result.add3M(ashikminDiffuse(light, diffuseTint, rayDir, lightDir, normal, uTangent, vTangent, half));
		
		return result;
	}

	@Override
	public Color evaluateSampledLight(IntersectionData idata, RayData rdata, Color light, RayData sample)
	{
		if(sample.getSubType() == SPECULAR)
		{
			return light.multiply3(specularTexture.evaluate(idata));
		}else{
			return light.multiply3(diffuseTexture.evaluate(idata));
		}
	}

	@Override
	public Color evaluateEmission(IntersectionData idata, RayData rdata)
	{
		return Color.black();
	}
	

	/* *********************************************************************************************
	 * Light Calculation Helper Methods
	 * *********************************************************************************************/
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
		
		//If middle spec is over 1.0 then the specular reflection /seems to be/ generating energy. So clamp it
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