package raytrace.scene;

import java.util.HashMap;

import system.Constants;
import tests.CSE168_Project1_Scene;
import tests.TestScene1;
import tests.TestScene2;
import tests.TestScene3;
import tests.TestScene4;

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
		
		sceneConstructors.put(Constants.SceneKeys.CSE168_Project1, new SceneConstructor(){
			@Override
			public Scene load() {
				return new CSE168_Project1_Scene();
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST2, new SceneConstructor(){
			@Override
			public Scene load() {
				return new TestScene2();
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST3, new SceneConstructor(){
			@Override
			public Scene load() {
				return new TestScene3();
			}
		});
		
		sceneConstructors.put(Constants.SceneKeys.TEST4, new SceneConstructor(){
			@Override
			public Scene load() {
				return new TestScene4();
			}
		});
	}
	
	public Scene load(String key)
	{
		return sceneConstructors.get(key).load();
	}
	
	
	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private abstract class SceneConstructor
	{
		public abstract Scene load();
	}

}
