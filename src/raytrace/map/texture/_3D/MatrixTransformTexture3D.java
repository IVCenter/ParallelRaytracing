package raytrace.map.texture._3D;

import math.Matrix4;
import math.Vector4;
import raytrace.color.Color;

public class MatrixTransformTexture3D extends Texture3D {
	
	/*
	 * A matrix transform for texture coordinates
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Texture3D texture;
	protected Matrix4 transform;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public MatrixTransformTexture3D()
	{
		this.texture = null;
		this.transform = new Matrix4();
	}
	
	public MatrixTransformTexture3D(Texture3D texture)
	{
		this.texture = texture;
		this.transform = new Matrix4();
		transform.identity();
	}
	
	public MatrixTransformTexture3D(Texture3D texture, Matrix4 transform)
	{
		this.texture = texture;
		this.transform = transform;
	}
	

	/* *********************************************************************************************
	 * Texture3D Override
	 * *********************************************************************************************/
	@Override
	public Color evaluate(Double x, Double y, Double z)
	{
		Vector4 transformed = transform.multiplyPt(new Vector4(x, y, z, 1));
		double[] coords = transformed.getArray();
		return texture.evaluate(coords[0], coords[1], coords[2]);
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
	
	public Matrix4 getTransform() {
		return transform;
	}

	public void setTransform(Matrix4 transform) {
		this.transform = transform;
	}
	
}