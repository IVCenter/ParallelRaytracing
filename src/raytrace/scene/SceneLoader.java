package raytrace.scene;

import java.util.HashMap;

public class SceneLoader {
	
	/*
	 * A scene to be rendered
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected HashMap<String, SceneConstructor> sceneConstructors;
	

	/* *********************************************************************************************
	 * Cosntructors
	 * *********************************************************************************************/
	public SceneLoader()
	{
		sceneConstructors = new HashMap<String, SceneConstructor>();
		
		sceneConstructors.put("", new SceneConstructor(){
			@Override
			public Scene load() {
				return new Scene();
			}
		});
	}
	
	public Scene load(String key)
	{
		return sceneConstructors.get(key).load();
	}
	
	
	
	
	private abstract class SceneConstructor
	{
		public abstract Scene load();
	}

}
