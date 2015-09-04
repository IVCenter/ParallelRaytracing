package raytrace.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import math.Vector3;
import raytrace.camera.Camera;
import raytrace.camera.ProgrammableCamera;
import raytrace.color.Color;
import raytrace.data.BakeData;
import raytrace.data.UpdateData;
import raytrace.framework.Tracer;
import raytrace.light.Light;
import raytrace.light.LightManager;
import raytrace.material.ColorMaterial;
import raytrace.material.DiffuseMaterial;
import raytrace.material.Material;
import raytrace.surfaces.CompositeSurface;
import raytrace.trace.RayTracer;

public abstract class Scene extends CompositeSurface {
	
	/*
	 * A scene to be rendered
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Camera activeCamera;
	protected LightManager lightManager;
	protected Material skyMaterial;
	protected String sceneKey;
	protected List<Tracer> tracers;
	protected Material defaultMaterial = new DiffuseMaterial(Color.white());
	
	protected boolean useDefaultMaterial = false;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public Scene()
	{
		lightManager = new LightManager();
		configure();
		initialize();
	}
	

	/* *********************************************************************************************
	 * Configuration
	 * *********************************************************************************************/
	private void configure()
	{
		this.tracers = configureTracers();
		this.activeCamera = configureCamera();
		skyMaterial = configureSkyMaterial();
		configureWorld();
	}
	
	protected List<Tracer> configureTracers()
	{
		ArrayList<Tracer> tracers = new ArrayList<Tracer>(1);
		tracers.add(new RayTracer());
		return tracers;
	}
	
	protected Camera configureCamera()
	{
		ProgrammableCamera camera = new ProgrammableCamera();
		camera.setStratifiedSampling(false);
		camera.setSuperSamplingLevel(1);
		camera.setPosition(new Vector3(0,2.5,5));
		camera.setViewingDirection(new Vector3(0.0, -0.15, -1));
		camera.setUp(new Vector3(0,1,0));
		camera.setFieldOfView(Math.PI/2.0);
		
		return camera;
	}
	
	protected Material configureSkyMaterial()
	{
		return new ColorMaterial(Color.black());
	}
	
	protected void configureWorld()
	{
		//TODO: Add a light
		//TODO: Add a spinning cube
	}
	

	/* *********************************************************************************************
	 * Abstract Methods
	 * *********************************************************************************************/
	@Deprecated
	//Old initialization sequence
	//Use the configuration methods instead
	protected /*abstract*/ void initialize(){ /**/ };
	
	
	/* *********************************************************************************************
	 * Surface Overrides
	 * *********************************************************************************************/
	@Override
	public void bake(BakeData data)
	{
		super.bake(null);
	}
	
	@Override
	public void update(UpdateData data)
	{
		//TODO: Spin the cube
	}
	

	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public Camera getActiveCamera() {
		return activeCamera;
	}

	public void setActiveCamera(Camera activeCamera) {
		this.activeCamera = activeCamera;
	}

	public LightManager getLightManager() {
		return lightManager;
	}

	public void setLightManager(LightManager lightManager) {
		this.lightManager = lightManager;
	}
	
	public Iterator<Light> lights()
	{
		return lightManager.iterator();
	}

	public Material getSkyMaterial() {
		return skyMaterial;
	}

	public void setSkyMaterial(Material skyMaterial) {
		this.skyMaterial = skyMaterial;
	}

	public String getSceneKey() {
		return sceneKey;
	}

	public void setSceneKey(String sceneKey) {
		this.sceneKey = sceneKey;
	}

	public List<Tracer> getTracers() {
		return tracers;
	}

	public void setTracers(List<Tracer> tracers) {
		this.tracers = tracers;
	}

	public Material getDefaultMaterial() {
		return defaultMaterial;
	}

	public void setDefaultMaterial(Material defaultMaterial) {
		this.defaultMaterial = defaultMaterial;
	}

	public boolean isUseDefaultMaterial() {
		return useDefaultMaterial;
	}

	public void setUseDefaultMaterial(boolean useDefaultMaterial) {
		this.useDefaultMaterial = useDefaultMaterial;
	}

}
