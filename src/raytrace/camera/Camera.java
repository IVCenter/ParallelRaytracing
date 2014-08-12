package raytrace.camera;

import java.io.Serializable;
import java.util.Collection;

import raytrace.framework.Positionable;
import math.Vector3;
import math.ray.Ray;

public abstract class Camera implements Iterable<Ray>, Positionable, Serializable {
	
	/*
	 * A base class for cameras
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Vector3 position = new Vector3();
	protected Vector3 viewingDirection = new Vector3(0, 0, -1);
	protected Vector3 up = new Vector3(0, 1, 0);
	protected double fieldOfView = Math.PI/2.0;
	protected double pixelWidth = 512;
	protected double pixelHeight = 512;
	
	protected boolean dirty = false;
	
	//protected boolean progressive = false;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public Camera()
	{
		//
	}
	
	public Camera(Vector3 position, Vector3 viewingDirection, Vector3 up, double fieldOfView, double pixelWidth, double pixelHeight)
	{
		this.position = position;
		this.viewingDirection = viewingDirection;
		this.up = up;
		
		this.fieldOfView = fieldOfView;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		
		this.dirty = false;
	}
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	protected abstract void update();
	protected abstract void handleModified();
	protected abstract Ray getRay(double x, double y);
	public abstract Collection<Camera> decompose(int count);
	public abstract Camera duplicate();
	

	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	protected void wasModified()
	{
		dirty = true;
		handleModified();
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	//Position
	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3 position) {
		this.position = position;
		wasModified();
	}
	
	//LookAt
	public Vector3 getViewingDirection() {
		return viewingDirection;
	}

	public void setViewingDirection(Vector3 viewingDirection) {
		this.viewingDirection = viewingDirection;
		this.viewingDirection.normalizeM();
		wasModified();
	}
	
	//Up
	public Vector3 getUp() {
		return up;
	}

	public void setUp(Vector3 up) {
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
	/*
	public boolean isProgressive() {
		return progressive;
	}

	public void setProgressive(boolean progressive) {
		this.progressive = progressive;
	}
	*/

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
