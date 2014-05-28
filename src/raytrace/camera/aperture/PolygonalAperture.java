package raytrace.camera.aperture;

import math.Vector4;

public class PolygonalAperture implements Aperture {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * A simple polygonal aperture
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected double radius;
	protected int elements;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public PolygonalAperture()
	{
		radius = 0.0;
		elements = 6;
	}
	
	public PolygonalAperture(double radius, int elements)
	{
		super();
		this.radius = radius;
		this.elements = elements;
	}

	/* *********************************************************************************************
	 * Interface Overrides
	 * *********************************************************************************************/
	@Override
	public Vector4 sample()
	{
		return sample(new Vector4());
	}
	
	@Override
	public Vector4 sample(Vector4 sample)
	{
		//TODO: Equations to distribute among triangles of polygon
		int element = (int)(Math.floor(Math.random() * elements) + 0.5);
		double elementAngle = (2.0 * Math.PI) / (double)elements;
		
		double r1 = Math.random();
		double r2 = Math.random();

		double sx = radius * Math.cos(element * elementAngle);
		double sy = radius * Math.sin(element * elementAngle);
		double ex = radius * Math.cos((element + 1) * elementAngle);
		double ey = radius * Math.sin((element + 1) * elementAngle);
		
		sample.set((Math.sqrt(r1) * (1 - r2)) * sx + (Math.sqrt(r1) * r2) * ex, 
				   (Math.sqrt(r1) * (1 - r2)) * sy + (Math.sqrt(r1) * r2) * ey, 
				   0, 
				   0);
		
		return sample;
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getElements() {
		return elements;
	}

	public void setElements(int elements) {
		this.elements = elements;
	}

}
