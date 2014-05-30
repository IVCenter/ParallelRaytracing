package raytrace.map.normal._3D;

import math.Vector4;
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
	public Vector4 evaluate(IntersectionData data)
	{
		//Point of intersection
		Vector4 point = data.getLocalPoint();
		
		//TODO: Modify the normal vector
		Vector4 normal = data.getNormal();
		
		//Create u,v tangents
		//Basis
		Vector4 uTangent;
		Vector4 vTangent;
		
		if(Math.abs(normal.dot3(Material.positiveYAxis)) == 1.0)
			uTangent = normal.cross3(Material.positiveXAxis).normalize3();
		else
			uTangent = normal.cross3(Material.positiveYAxis).normalize3();
		vTangent = uTangent.cross3(normal).normalize3();
		
		//Setup a sample point
		Vector4 samplePoint = new Vector4();
		double samplingRadius2 = samplingRadius * 2.0;
		
		//get negative U value
		samplePoint.set(point);
		double negUValue = getTextureValue(samplePoint, uTangent, -samplingRadius);

		//get positive U value
		samplePoint.set(point);
		double posUValue = getTextureValue(samplePoint, uTangent, samplingRadius);
		
		//Get the tangent contribution for u
		double uTanMag = Math.cos(Math.atan( (negUValue - posUValue) / (samplingRadius2) ));
				

		//get negative V value
		samplePoint.set(point);
		double negVValue = getTextureValue(samplePoint, vTangent, -samplingRadius);
		
		//get positive V value
		samplePoint.set(point);
		double posVValue = getTextureValue(samplePoint, vTangent, samplingRadius);

		//Get the tangent contribution for v
		double vTanMag = Math.cos(Math.atan( (negVValue - posVValue) / (samplingRadius2) ));
		
		
		Vector4 newNormal = new Vector4();
		newNormal.addMultiRight3M(uTangent, -(negUValue - posUValue) / (samplingRadius2));
		newNormal.addMultiRight3M(vTangent, -(negVValue - posVValue) / (samplingRadius2));
		newNormal.addMultiRight3M(normal, strength);
		newNormal.normalize3();
		
		return newNormal;
	}
	
	private double getTextureValue(Vector4 samplePoint, Vector4 tangent, double samplingRadius)
	{
		samplePoint.addMultiRight3M(tangent, samplingRadius);
		double[] spArr = samplePoint.getArray();
		return texture.evaluate(spArr[0], spArr[1], spArr[2]).intensity3();
	}
	

	/* *********************************************************************************************
	 * NormalMap3D Override
	 * *********************************************************************************************/
	@Override
	public Vector4 evaluate(Double x, Double y, Double z)
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
