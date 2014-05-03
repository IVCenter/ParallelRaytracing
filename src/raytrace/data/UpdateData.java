package raytrace.data;

import raytrace.scene.Scene;

public class UpdateData {
	
	/*
	 * A storage class for data used by render calls
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Scene scene;
	protected double dt;
	

	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public UpdateData()
	{
		dt = 0.0;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public double getDt() {
		return dt;
	}

	public void setDt(double dt) {
		this.dt = dt;
	}

}
