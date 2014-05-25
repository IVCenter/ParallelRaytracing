package raytrace;

import java.io.File;

import process.logging.Logger;
import process.utils.StringUtils;
import process.utils.TimeStamp;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;
import system.ApplicationDelegate;
import system.Configuration;

public class AnimationRenderer implements Renderer {

	/*
	 * A configurable ray tracer that allows 
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected Renderer renderer;
	protected String animationStartTimeStamp;
	protected int frameNumberWidth;
	protected long frameNumber;
	
	protected String outputFolderPath;
	
	protected boolean isRecording;
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public AnimationRenderer()
	{
		frameNumberWidth = 4;
		frameNumber = 0;
		
		outputFolderPath = null;
		isRecording = false;
	}
	
	public AnimationRenderer(Renderer renderer)
	{
		this();
		this.renderer = renderer;
	}

	@Override
	public void initialize()
	{
		//TODO
	}
	
	
	/* *********************************************************************************************
	 * Render Methods
	 * *********************************************************************************************/
	@Override
	public void update(UpdateData data)
	{
		if(renderer != null)
		{
			renderer.update(data);
		}
	}

	@Override
	public void render(RenderData data)
	{
		if(renderer != null)
		{
			renderer.render(data);
		}
		
		//Save pixel buffer to file 
		if(isRecording && outputFolderPath != null && Configuration.canWriteToDisk())
		{
			ApplicationDelegate.inst.getPixelBuffer().writeToFile(
					outputFolderPath + Configuration.getFrameFileNamePrefix() + animationStartTimeStamp + "_"
					+ StringUtils.zeroPad(frameNumber, frameNumberWidth)
				);
		}
		
		//Increment Frame Number
		frameNumber++;
	}

	
	/* *********************************************************************************************
	 * Recording Methods
	 * *********************************************************************************************/
	public void startRecording()
	{
		if(isRecording)
			return;
		
		if(!Configuration.canWriteToDisk())
		{
			Logger.warning(-31, "AnimationRenderer: Unable to start recording, writing to disk is disabled.");
			return;
		}
		
		
		//Setup recording values
		animationStartTimeStamp = TimeStamp.makeForFileName();
		frameNumber = 0;
		
		File animationDirectory = new File(Configuration.getWorkingDirectory() + Configuration.getAnimationSubDirectory());
		File outputFolder = new File(Configuration.getWorkingDirectory() + Configuration.getAnimationSubDirectory()
				+ Configuration.getAnimationFolderNamePrefix() + animationStartTimeStamp + "/");
		
		//Make sure the animation folder exists, and then create the folder for this animation
		if(animationDirectory.exists())
		{
			if(!outputFolder.exists())
			{
				Logger.progress(-31, "AnimationRenderer: Creating folder for the current animation...");
				outputFolderPath = outputFolder.getPath() + "/";
				
				if(!outputFolder.mkdir())
				{
					outputFolderPath = null;
					Logger.error(-31, "AnimationRenderer: Failed to create folder for the current animation. [" + 
							outputFolder.getPath() + "]");
					return;
				}
			}
		}else{
			Logger.error(-31, "AnimationRenderer: The top-level animation folder does not exist. [" +
					animationDirectory.getPath() + "]");
			return;
		}

		//Start recording
		Logger.progress(-31, "AnimationRenderer: Starting recording an animation... [" + outputFolder.getName() + "].");
		isRecording = true;
	}
	
	public void stopRecording()
	{
		if(!isRecording)
			return;

		Logger.progress(-31, "AnimationRenderer: Ending recording an animation.");
		isRecording = false;
	}
	
	public boolean isRecording() { return isRecording; }
	
	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

}