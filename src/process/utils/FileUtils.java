package process.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import system.Constants;

public class FileUtils {
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static File openDirectoryChooser() throws Exception
	{
		File[] dir = null;
		try {
			dir = FileOpener.open(false, true, new ArrayList<String>(), "Directory");
		} catch (FileNotFoundException e1) {
			Exception e = new Exception("Excepted while attempting to prompt the User for a directory, can not proceed.");
			throw e;
		}

		//Make sure we have a valid directory
		if(dir == null || dir.length == 0 || dir[0] == null) {
			Exception e = new Exception("No directory was selected, can not proceed.");
			throw e;
		}

		return dir[0];
	}

	/**
	 * 
	 * @param validFileExtension
	 * @return
	 */
	public static File openFileChooser(String validFileExtension)
	{		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.addChoosableFileFilter(new SimpleFileFilter(validFileExtension));
		chooser.setDialogTitle("Select a valid " + validFileExtension + " file (eg: myFile." + validFileExtension + ")");
		chooser.setApproveButtonToolTipText("Select " + validFileExtension );	    
		int choice = chooser.showDialog((new JFrame()), "Select " + validFileExtension);
		if (choice != JFileChooser.APPROVE_OPTION) {
			return null;
		}	    
		File file = chooser.getSelectedFile();
		if(file == null)
			return null;

		return file;
	}
	
	
	
	/*******************************************************************************************************************
	 * FILE PROCESSING METHODS
	 *******************************************************************************************************************/
	
	//Returns a list of files, found below the current directory, that have a given extension
	/**
	 * 
	 * @param dir
	 * @param extension
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<File> findAllFilesInDirWithExtension(File dir, String extension) throws Exception
	{
		//If the File object is not a directory then we can not search it for files, so bail out
		if(dir == null || !dir.isDirectory()) {
			Exception e = (new Exception("ERROR!: FileUtils.findAllFilesInDirWithExtension(): " +
					"The given paramter dir was not a directory."));
			throw e;
		}

		//Storage for found files
		ArrayList<File> files = findFilesInDirectory(dir);
		ArrayList<File> filesWithExt = new ArrayList<File>();

		//Filter the file list down to just files with the given extension
		File f;
		for(int i = 0; i < files.size(); i++)
		{
			f = files.get(i);
			if(f.getName().endsWith(extension)) {
				filesWithExt.add(f);

			}
		}

		return filesWithExt;
	}

	//Returns all files below a given directory (recursive)
	/**
	 * 
	 * @param dir
	 * @return
	 */
	private static ArrayList<File> findFilesInDirectory(File dir)
	{
		File[] files = dir.listFiles();
		File[] dirs = getDirectoryList(files);

		ArrayList<File> allFiles = new ArrayList<File>();
		
		//Process the files at this level
		if(files != null)
		{
			for(int i = 0; i < files.length; i++)
			{
				if(files[i].isDirectory())
					continue;
				
				allFiles.add(files[i]);
			}
		}

		//Recursively dive the directory structure
		for(int i = 0; i < dirs.length; i++) 
			allFiles.addAll(findFilesInDirectory(dirs[i]));

		return allFiles;
	} 

	//Gets a listing of all directories from a list of File objects
	/**
	 * 
	 * @param files
	 * @return
	 */
	private static File[] getDirectoryList(File[] files)
	{
		if(files == null)
			return new File[0];

		ArrayList<File> dirs = new ArrayList<File>();
		for(int i = 0; i < files.length; i++)
			if(files[i].isDirectory())
				dirs.add(files[i]);
		
		File[] dirArr = dirs.toArray(new File[dirs.size()]);;
		
		return dirArr;
	} 
	
	
	/* ********************************************************************************************************
	 * Private Classes
	 * *******************************************************************************************************/

	public static class FileOpener
	{
		/* ********************************************************************************************************
		 * Static Utility Methods
		 * *******************************************************************************************************/
		
		public static File open(ArrayList<String> extensionFilter, String prettyFileType) throws FileNotFoundException
		{
			File[] files = open(false, extensionFilter, prettyFileType);
			if(files != null && files.length > 0)
				return files[0];
			return null;
		}

		public static File[] open(Boolean multiFile, ArrayList<String> extensionFilter, String prettyFileType) throws FileNotFoundException
		{
			return open(multiFile, false, extensionFilter, prettyFileType);
		}
		
		public static File[] open(Boolean multiFile, Boolean openDir, ArrayList<String> extensionFilter, String prettyFileType) throws FileNotFoundException
		{	
			return open(Constants.workingDirectory, multiFile, openDir, extensionFilter, prettyFileType);
		}

		public static File openDir() throws FileNotFoundException
		{
			File[] files = open(false, true, new ArrayList<String>(), "Directory");
			if(files != null && files.length > 0)
				return files[0];
			return null;
		}

		/**
		 * multiFile specifies whether a user may select multiple files or not
		 * extnsionFilter is a list of Strings that represent allowable extensions (Ex: .exe .php .htm etc)
		 * prettyFileType is used to tell the user what file type is being opened.  It is visible in the header of the 
		 * FileChooser
		 * @param multiFile
		 * @param extensionFilter
		 * @param prettyFileType
		 * @return
		 * @throws FileNotFoundException
		 */
		public static File[] open(String directoryToOpen, Boolean multiFile, Boolean openDir, ArrayList<String> extensionFilter, String prettyFileType) throws FileNotFoundException
		{	
		    JFileChooser chooser = new JFileChooser(directoryToOpen);
		    
		    if(openDir)
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    
		    if(extensionFilter != null && !extensionFilter.isEmpty())
		    	chooser.addChoosableFileFilter(new SimpleFileFilter(extensionFilter));
		    
		    chooser.setMultiSelectionEnabled(multiFile);
		    chooser.setDialogTitle("Select a valid " + prettyFileType);
		    chooser.setApproveButtonToolTipText("Select");	    
		    
		    int choice = chooser.showDialog((new JFrame()), "Open");
		    if (choice != JFileChooser.APPROVE_OPTION) {
		      return null;
		    }	    
		    
		    File[] files = chooser.getSelectedFiles();
		    if(!multiFile){
		    	files = new File[1];
		    	files[0] = chooser.getSelectedFile();
		    }
		    
		    if(files != null && files.length > 0)
		    {  
			    for(int i = 0; i < files.length; i++)
			    {
			    	File file = files[i];
				    if (!file.exists() ){
				      throw new FileNotFoundException("File " + file.getPath() + " does not exist.");
				    }else if(!file.canRead() ){
				      throw new FileNotFoundException("File " + file.getPath() + " is not readable.");
				    }	
			    }
			    return files;
		    }
		    return null;
		}

	}
	
	
	//SimpleFileFilter
	@SuppressWarnings("serial")
	public static class SimpleFileFilter extends FileFilter implements Serializable
	{
		String[] extensions;
		String description;

		public SimpleFileFilter(String ext)
		{
			this (new String[] {ext}, null);
		}

		public SimpleFileFilter(ArrayList<String> exts)
		{
			this (exts.toArray(new String[exts.size()]), null);
		}

		public SimpleFileFilter(String[] exts, String descr)
		{
			extensions = new String[exts.length];
			for (int i = exts.length - 1; i >= 0; i--) {
				extensions[i] = exts[i].toLowerCase();
			}
			
			description = (descr == null ? exts[0] + " files" : descr);
		}

		public boolean accept(File f)
		{
			//We always allow directories, regardless of their extension
			if (f.isDirectory()) { return true; }

			//Check the extension
			String fname = f.getName().toLowerCase();
			for (int i = extensions.length - 1; i >= 0; i--) {
				if (fname.endsWith(extensions[i])) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() { return description; }
	}

	
	
	
}
