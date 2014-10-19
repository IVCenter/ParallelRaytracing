package raster.pixel;

import java.util.ArrayList;

import math.Spline;

import raytrace.color.Color;

public class SplineLevelsPT extends PixelTransform{
	
	/*
	 * Adjust the pixels level via a series of splines such that [0,1] -> [0,1]
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	ArrayList<Spline> splines;

	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	//
	
	
	/* *********************************************************************************************
	 * Transform Override
	 * *********************************************************************************************/
	@Override
	public Color transform(Color pixel)
	{
		return pixel.multiply3M(-1.0).add3M(1.0);
	}


	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public ArrayList<Spline> getSplines() {
		return splines;
	}

	public void setSplines(ArrayList<Spline> splines) {
		this.splines = splines;
	}
	
	public void addSpline(Spline spline) {
		this.splines.add(spline);
	}
}
