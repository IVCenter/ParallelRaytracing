package raytrace.map.normal._3D;

import math.Vector3;
import raytrace.data.IntersectionData;
import raytrace.map.texture._3D.Texture3D;
import raytrace.material.Material;

public class TextureGradientNormalMap3D extends NormalMap3D {
	
	/*
	 * A 3D normal map that is calculated from sampling points near the intersection
	 * and taking the gradient
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D texture;
	protected double samplingRadius;
	protected double strength;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public TextureGradientNormalMap3D()
	{
		this.texture = null;//TODO: Needs a default texture
		this.samplingRadius = 0.0001;
		this.strength = 1.0;
	}
	
	public TextureGradientNormalMap3D(Texture3D texture)
	{
		this.texture = texture;
		this.samplingRadius = 0.0001;
		this.strength = 1.0;
	}
	
	public TextureGradientNormalMap3D(Texture3D texture, double samplingRadius)
	{
		this.texture = texture;
		this.samplingRadius = samplingRadius;
		this.strength = 1.0;
	}
	
	public TextureGradientNormalMap3D(Texture3D texture, double samplingRadius, double strength)
	{
		this.texture = texture;
		this.samplingRadius = samplingRadius;
		this.strength = 1.0;
	}

	
	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/
	@Override
	public Vector3 evaluate(IntersectionData data)
	{
		//Point of intersection
		Vector3 point = data.getLocalPoint();
		
		//TODO: Modify the normal vector
		Vector3 normal = data.getNormal();
		
		//Create u,v tangents
		//Basis
		Vector3 uTangent;
		Vector3 vTangent;
		
		if(Math.abs(normal.dot(Material.positiveYAxis)) == 1.0)
			uTangent = normal.cross(Material.positiveXAxis).normalizeM();
		else
			uTangent = normal.cross(Material.positiveYAxis).normalizeM();
		vTangent = uTangent.cross(normal).normalizeM();
		
		//Setup a sample point
		Vector3 samplePoint = new Vector3();
		double samplingRadius2 = samplingRadius * 2.0;
		
		//get negative U value
		samplePoint.set(point);
		double negUValue = getTextureValue(samplePoint, uTangent, -samplingRadius);

		//get positive U value
		samplePoint.set(point);
		double posUValue = getTextureValue(samplePoint, uTangent, samplingRadius);
		
		//Get the tangent contribution for u
		//double uTanMag = Math.cos(Math.atan( (negUValue - posUValue) / (samplingRadius2) ));
				

		//get negative V value
		samplePoint.set(point);
		double negVValue = getTextureValue(samplePoint, vTangent, -samplingRadius);
		
		//get positive V value
		samplePoint.set(point);
		double posVValue = getTextureValue(samplePoint, vTangent, samplingRadius);

		//Get the tangent contribution for v
		//double vTanMag = Math.cos(Math.atan( (negVValue - posVValue) / (samplingRadius2) ));
		
		
		Vector3 newNormal = new Vector3();
		newNormal.addMultiRightM(uTangent, -(negUValue - posUValue) / (samplingRadius2));
		newNormal.addMultiRightM(vTangent, -(negVValue - posVValue) / (samplingRadius2));
		newNormal.addMultiRightM(normal, strength);
		newNormal.normalizeM();
		
		return newNormal;
	}
	
	private double getTextureValue(Vector3 samplePoint, Vector3 tangent, double samplingRadius)
	{
		samplePoint.addMultiRightM(tangent, samplingRadius);
		double[] spArr = samplePoint.getArray();
		return texture.evaluate(spArr[0], spArr[1], spArr[2]).intensity3();
	}
	

	/* *********************************************************************************************
	 * NormalMap3D Override
	 * *********************************************************************************************/
	@Override
	public Vector3 evaluate(Double x, Double y, Double z)
	{
		//Is this evaluatable without a normal?
		return null;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Texture3D getTexture() {
		return texture;
	}

	public void setTexture(Texture3D texture) {
		this.texture = texture;
	}

	public double getSamplingRadius() {
		return samplingRadius;
	}

	public void setSamplingRadius(double samplingRadius) {
		this.samplingRadius = samplingRadius;
	}

	public double getStrength() {
		return strength;
	}

	public void setStrength(double strength) {
		this.strength = strength;
	}

}
