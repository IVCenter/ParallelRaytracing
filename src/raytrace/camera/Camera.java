package raytrace.camera;

import raytrace.framework.Positionable;
import math.Ray;
import math.Vector4;

public abstract class Camera implements Iterable<Ray>, Positionable{
	
	/*
	 * A base class for cameras
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector4 position = new Vector4();
	protected Vector4 viewingDirection = new Vector4(0,0,-1,0);
	protected Vector4 up = new Vector4(0,1,0,0);
	protected double fieldOfView = Math.PI/2.0;
	protected double pixelWidth = 512;
	protected double pixelHeight = 512;
	
	protected int raySetID = 0;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public Camera()
	{
		//
	}
	
	public Camera(Vector4 position, Vector4 viewingDirection, Vector4 up, double fieldOfView, double pixelWidth, double pixelHeight)
	{
		this.position = position;
		this.viewingDirection = viewingDirection;
		this.up = up;
		
		this.fieldOfView = fieldOfView;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
	}
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	protected abstract void update();
	protected abstract void wasModified();
	protected abstract Ray getRay(double x, double y);
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//Position
	@Override
	public Vector4 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector4 position) {
		this.position = position;
		wasModified();
	}
	
	//LookAt
	public Vector4 getViewingDirection() {
		return viewingDirection;
	}

	public void setViewingDirection(Vector4 viewingDirection) {
		this.viewingDirection = viewingDirection;
		wasModified();
	}
	
	//Up
	public Vector4 getUp() {
		return up;
	}

	public void setUp(Vector4 up) {
		this.up = up;
		wasModified();
	}

	public double getFieldOfView() {
		return fieldOfView;
	}

	public void setFieldOfView(double fieldOfView) {
		this.fieldOfView = fieldOfView;
		wasModified();
	}

	public double getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(double pixelWidth) {
		this.pixelWidth = pixelWidth;
		wasModified();
	}

	public double getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(double pixelHeight) {
		this.pixelHeight = pixelHeight;
		wasModified();
	}

	public int getRaySetID() {
		return raySetID;
	}
}
