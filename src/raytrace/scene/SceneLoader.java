package raytrace.scene;

import java.util.HashMap;

import system.Constants;
import tests.TestScene1;

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
				return new EmptyScene();
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST1, new SceneConstructor(){
			@Override
			public Scene load() {
				return new TestScene1();
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
