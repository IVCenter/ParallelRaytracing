package process.logging;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;

import process.utils.StringUtils;
import process.utils.TimeStamp;

public class Logger {
	
	/*
	 * Manages logging messages to various outputs
	 */
	

	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	protected static String logDirectory;
	protected static String logFilePrefix;
	
	protected static String logServerBaseURL;
	
	protected static enum Level {None, Debug, Progress, Warning, Error};
	protected static String defaultPrefix = "X";
	
	

	protected static Thread logWriterThread;
	
	protected static Deque<Message> logQueue;
	protected static long queueSizeBeforePause;
	protected static long queueSizeBeforeResume;
	protected static int queueSleepInterval;
	
	private static long messageID = 0;
	

	/* *********************************************************************************************
	 * Static Constructor
	 * *********************************************************************************************/
	static
	{
		logDirectory = "/";
		logFilePrefix = "AutomationLog_";
		
		logServerBaseURL = "http://127.0.0.1/";
		
		logQueue = new ArrayDeque<Message>();
		queueSizeBeforePause = 10000;
		queueSizeBeforeResume = 10;
		queueSleepInterval = 10;
		
		//DO THIS LAST
		//Launch worker thread(s)
		logWriterThread = new Thread(new LogWriter());
		logWriterThread.start();
	}
	

	/* *********************************************************************************************
	 * Static Methods
	 * *********************************************************************************************/
	/*
	 * Shorthand static logging methods
	 */
	public static void debug(long jobID, String message)
	{
		debug(jobID, defaultPrefix, message);
	}
	
	public static void message(long jobID, String message)
	{
		message(jobID, defaultPrefix, message);
	}
	
	public static void warning(long jobID, String message)
	{
		warning(jobID, defaultPrefix, message);
	}
	
	public static void error(long jobID, String message)
	{
		error(jobID, defaultPrefix, message);
	}
	
	/*
	 * Full static logging
	 */
	public static void debug(long jobID, String prefix, String message)
	{
		handleMessage(jobID, prefix, message, Level.Debug);
	}
	
	public static void message(long jobID, String prefix, String message)
	{
		handleMessage(jobID, prefix, message, Level.Progress);
	}
	
	public static void warning(long jobID, String prefix, String message)
	{
		handleMessage(jobID, prefix, message, Level.Warning);
	}
	
	public static void error(long jobID, String prefix, String message)
	{
		handleMessage(jobID, prefix, message, Level.Error);
	}

	/*
	 * Terminate the logger
	 */
	public static void terminate()
	{
		LogWriter.terminate();
	}
	

	/* *********************************************************************************************
	 * Utility Methods
	 * *********************************************************************************************/
	private static synchronized long getNextMessageID() { return messageID++; };
	
	private static void handleMessage(long jobID, String idPrefix, String message, Level level)
	{
		//Prep message variables
		long msgID = getNextMessageID();
		String timeStamp = TimeStamp.makeFromLong((new Date()).getTime());
		
		//Create a new message
		Message m = new Message(msgID, jobID, idPrefix, timeStamp, level, message);
		
		//Add the message to the queue
		accessQueue(m, true);
		
		//if the queue is too long, pause the calling thread until its back under a given amount
		if(logQueue.size() > queueSizeBeforePause)
		{
			String overflowStr = "Logger: Log Queue is currently at " + logQueue.size() + ", which is over its max limit of " 
					+ queueSizeBeforePause + ".  Sleeping calling thread until the queue is below the resume threshold of "
					+ queueSizeBeforeResume;
			
			Message overflowMessage = new Message(getNextMessageID(), jobID, "L", timeStamp, Level.Warning, overflowStr);
			
			accessQueue(overflowMessage, false);
			
			while(logQueue.size() > queueSizeBeforeResume)
			{
				try {
					Thread.sleep(queueSleepInterval);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}
	
	//Synchronizes all operations to the logQueue
	private static synchronized Message accessQueue(Message message, boolean addBack)
	{
		if(message == null)
			return logQueue.removeFirst();
		if(addBack)
			logQueue.addLast(message);
		else
			logQueue.addFirst(message);
		return null;
	}
	

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	/*
	 * Message
	 */
	private static class Message 
	{
		public long messageID = -1;
		public long jobID = -1;
		public String idPrefix = "";
		public String timeStamp = TimeStamp.makeEmpty();
		public Level level = Level.None;
		public String message = "";
		
		public Message(long messageID, long jobID, String idPrefix, String timeStamp, Level level, String message)
		{
			this.messageID = messageID;
			this.idPrefix = idPrefix;
			this.jobID = jobID;
			this.timeStamp = timeStamp;
			this.level = level;
			this.message = message;
		}
	}
	
	/*
	 * LogWriter
	 */
	public static class LogWriter implements Runnable
	{
		private static boolean running = true;

		@Override
		public void run()
		{	
			Logger.message(-0, "Now running worker thread [" + this + "]");
			
			//Keep watching the log queue of the logger until terminated
			while(running || logQueue.size() > 0)
			{
				while(logQueue.size() > 0)
				{
					//Get the first message in the queue
					Message m = accessQueue(null, true);
					
					//Do something with the message
					String consoleString = formatMessageForConsole(m);
					toConsole(m.level, consoleString);
					//TODO: Write to file
				}
				
				try {
					Thread.sleep(queueSleepInterval);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			
			System.out.println("Now shutting down worker thread [" + this + "]");
		}
		
		public static void terminate() { running = false; }
		
		/*
		 * Formatting Methods ****************************************************************
		 */
		private static String formatMessageForConsole(Message m)
		{
			StringBuilder sb = new StringBuilder();

			sb.append(StringUtils.spacePadBack(m.level.name(), 8) + ":");
			sb.append("[MID:" + StringUtils.zeroPad(m.messageID, 8) + "]");
			sb.append("[" + m.idPrefix + ":" + StringUtils.zeroPad(m.jobID, 8) + "]");
			sb.append(m.timeStamp);
			sb.append(" - ");
			sb.append(m.message);
			
			return sb.toString();
		}
		
		
		

		/*
		 * Output** Methods ******************************************************************
		 */
		private static void toConsole(Level level, String consoleString)
		{
			if(level.compareTo(Level.Progress) > 0) {
				System.err.println(consoleString);
			}else{
				System.out.println(consoleString);	
			}
		}
		
	}
}
