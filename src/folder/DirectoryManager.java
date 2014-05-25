package folder;

import java.io.File;

import process.logging.Logger;

import system.Configuration;



public class DirectoryManager {
	
	/*
	 * A directory manager for creating directories, constructing paths, etc.
	 */

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	//
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		//
	}
	

	/* *********************************************************************************************
	 * Static Access Methods
	 * *********************************************************************************************/	
	/**
	 * Attempts to create the folder structure specified by:
	 * 
	 * 	-Working Directory:
	 * 		-Models Dir
	 * 		-Screenshots Dir
	 * 		-Animations Dir
	 * 
	 * *All folder names can be specified in Configuration (directly or via a config file)
	 */
	public static void createFolderStructure()
	{
		String workDir = Configuration.getWorkingDirectory();
		
		//If working dir is missing a ending slash, add one
		//TODO: Does this work on windows as epected?
		workDir = addTrailingSlash(workDir);
		Configuration.setWorkingDirectory(workDir);
		
		//Create a file object
		File workDirFile = new File(workDir);
		
		
		//Log the working dir
		Logger.progress(-33, "DirectoryManager: Attemtping to use working directory [" + workDir + "].");
		
		
		//If work dir doesn't exist, see if its parent exists
		if(!workDirFile.exists())
		{
			//Attempt to create the directory
			if(!workDirFile.mkdirs())
			{
				Logger.error(-33, "DirectoryManager: Failed to create working directory: " +
						"Write permission denied.  File writing has been disabled. [" + workDirFile.getPath() + "]");
				Configuration.setCanWriteToDisk(false);
				return;
			}
		}
		
		//Make sure once more that working directory exists
		if(!workDirFile.exists())
		{
			Logger.error(-33, "DirectoryManager: The working directory does not exist. " +
					"[" + workDirFile.getPath() + "]");
			Configuration.setCanWriteToDisk(false);
			return;
		}
		
		
		//Make sure once more that we can write to the working directory
		if(!workDirFile.canWrite())
		{
			Logger.error(-33, "DirectoryManager: Failed to create working directory: " +
					"Write permission denied. [" + workDirFile.getPath() + "]");
			Configuration.setCanWriteToDisk(false);
			return;
		}
		
		
		//Now create the sub folders
		//Animation subfolder
		Configuration.setAnimationSubDirectory(addTrailingSlash(Configuration.getAnimationSubDirectory()));
		createSubDirectory(workDir, Configuration.getAnimationSubDirectory(), "animations");
		
		//Screenshots subfolder
		Configuration.setScreenshotSubDirectory(addTrailingSlash(Configuration.getScreenshotSubDirectory()));
		createSubDirectory(workDir, Configuration.getScreenshotSubDirectory(), "screenshots");
		
		//Models subfolder
		Configuration.setModelsSubDirectory(addTrailingSlash(Configuration.getModelsSubDirectory()));
		createSubDirectory(workDir, Configuration.getModelsSubDirectory(), "models");
		
		//Textures subfolder
		Configuration.setTextureSubDirectory(addTrailingSlash(Configuration.getTextureSubDirectory()));
		createSubDirectory(workDir, Configuration.getTextureSubDirectory(), "textures");
	}
	
	public static String workingDirectory()
	{
		return Configuration.getWorkingDirectory();
	}
	
	public static String screenshotsDirectory()
	{
		return Configuration.getWorkingDirectory() + Configuration.getScreenshotSubDirectory();
	}
	
	public static String animationDirectory()
	{
		return Configuration.getWorkingDirectory() + Configuration.getAnimationSubDirectory();
	}
	
	public static String modelsDirectory()
	{
		return Configuration.getWorkingDirectory() + Configuration.getModelsSubDirectory();
	}
	
	public static String texturesDirectory()
	{
		return Configuration.getWorkingDirectory() + Configuration.getTextureSubDirectory();
	}
	

	/* *********************************************************************************************
	 * Private Static Helper Methods
	 * *********************************************************************************************/
	private static String addTrailingSlash(String path)
	{
		if(path.endsWith("/"))
			return path;
		return path + "/";
	}
	
	private static void createSubDirectory(String workingDir, String subDir, String readableDirName)
	{
		File dir = new File(workingDir + subDir);
		if(!dir.exists())
		{
			if(!dir.mkdir())
			{
				Logger.error(-33, "DirectoryManager: Failed to create " + readableDirName + " sub-directory: " +
						" [" + dir.getPath() + "]");
				Configuration.setCanWriteToDisk(false);
			}
		}
	}

}
