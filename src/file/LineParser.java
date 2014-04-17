package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class LineParser implements Iterable<String>{
	
	/*
	 * A simple line parser that evaluates lazily to allow for reading of massive files
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected File file;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public LineParser(File file)
	{
		this.file = file;
	}

	/* *********************************************************************************************
	 * Iterable Override
	 * *********************************************************************************************/
	@Override
	public Iterator<String> iterator()
	{
		return new LineIterator();
	}
	

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class LineIterator implements Iterator<String>
	{
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		private BufferedReader inReader;
		private String currentLine = null;
		private boolean closed = false;

		
		/* *********************************************************************************************
		 * Constructors
		 * *********************************************************************************************/
		public LineIterator()
		{
			try 
			{
				InputStream is =  new FileInputStream(file);
				inReader = new BufferedReader(new InputStreamReader(is));
				currentLine = inReader.readLine();
			} 
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to read file [" + file.getName() + "]");
			}
		}

		
		/* *********************************************************************************************
		 * Iterator Overrides
		 * *********************************************************************************************/
		@Override
		public boolean hasNext()
		{
			boolean hasNext = inReader != null && currentLine != null;
			
			//Fast return if we have a next
			if(hasNext)
				return true;
			
			//If we're out of lines, and have not yet closed the stream, close it
			if(!closed && inReader != null) {
				try {
					inReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				closed = true;
			}
			
			return false;
		}

		@Override
		public String next()
		{
			//If we don't have a valid reader, or are out of lines, return null
			if(inReader == null || currentLine == null)
				return null;
			
			String next = currentLine;
			try {
				currentLine = inReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				currentLine = null;
			}
			
			return next;
		}

		@Override
		public void remove()
		{
			//Nope
		}
		
	}

}
