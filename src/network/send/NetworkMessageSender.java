package network.send;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import process.logging.Logger;
import system.ApplicationDelegate;

import network.Message;

public class NetworkMessageSender extends MessageSender implements Runnable {
	
	/*
	 * 
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected final int port;
	protected final ExecutorService pool;
	protected final Thread thisThread;
	
	protected final ConcurrentLinkedQueue<SendableMessage> messageQueue;
	
	//Threading related
	protected AtomicInteger wasNotified;
	protected Object sendLock;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NetworkMessageSender(int port, int poolSize) throws IOException
	{
		super();
		this.port = port;
		pool = Executors.newFixedThreadPool(poolSize);
		
		messageQueue = new ConcurrentLinkedQueue<SendableMessage>();
		
		wasNotified = new AtomicInteger(0);
		sendLock = new Object();
		
		thisThread = new Thread(this);
		thisThread.start();
	}

	
	/* *********************************************************************************************
	 * Runnable Overrides
	 * *********************************************************************************************/
	public void run()
	{
		Logger.progress(-22, "NetworkMessageSender: Starting...");
		try{
			//While spidey face holds true, serve the socket
			for(;;)
			{	
				//Spin Lock
				synchronized(sendLock)
				{
					while(wasNotified.get() == 0 && messageQueue.isEmpty())
					{
						try {
							
							sendLock.wait();
								
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				//While there are message to send, keep sending
				while(!messageQueue.isEmpty())
				{
					try {
						pool.execute(messageQueue.remove());
					}catch(Exception e) { /*Do nothing*/ }
				}
				
				//Decrement the notification counter
				synchronized(wasNotified) {
					if(wasNotified.get() > 0)
						wasNotified.decrementAndGet();
				}
			}
			
		} catch (Exception ex) {
			pool.shutdown();
		}
	}
	
	//Based on the shutdown procedure used in the Oracle documentation
	//http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html
	public void stop()
	{
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS))
			{
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
	
	
	/* *********************************************************************************************
	 * Sending Methods
	 * *********************************************************************************************/
	@Override
	public void send(Message message, String destination)
	{	
		if(message == null || destination == null) {
			Logger.warning(-22, "NetworkMessageSender: Cannot send message.  Either the message or the destination was null.");
			return;
		}
		
		//Create a new sendable message
		SendableMessage sendmsg = new SendableMessage(message, destination);
		messageQueue.add(sendmsg);
		
		//And notify the sender
		synchronized(sendLock) {
			wasNotified.incrementAndGet();
			sendLock.notify();
		}
	}
	
	
	/* *********************************************************************************************
	 * Access Methods
	 * *********************************************************************************************/
	public boolean isSending()
	{
		return messageQueue.size() > 0;
	}
	

	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	private class SendableMessage implements Runnable
	{
		/*
		 * A message that is paired with a destination
		 */

		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		protected Message message;
		protected String destination;

		
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		public SendableMessage(Message message, String destination)
		{
			this.message = message;
			this.destination = destination;
		}


		/* *********************************************************************************************
		 * Runnable Overrides
		 * *********************************************************************************************/

		@Override
		public void run()
		{
			
			Socket socket = null;
			try {
				Logger.progress(-22, "NetworkMessageSender: Sending message...");
				
				//Create a new socket
				socket = new Socket(destination, port);
				
				//Open the in and out streams
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
		        
		        //write this message
		        outStream.writeObject(message);    
		        outStream.flush();
		        outStream.close();
		        
		        try {

			        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			        
			        //Read the response
			        Message responseMessage = (Message)inStream.readObject();
			        //inStream.close();
			        
			        //Pass the response to the listener
			        ApplicationDelegate.inst.getMessageListener().listen(responseMessage);
			        
		        } catch(IOException e) {
		        	//Just means no response object from server
		        	//This is normal
		        }
		        
		        socket.close();

				
			} catch (Exception e)
			{
				Logger.error(-22, "NetworkMessageSender: Failed to send message.");
				//e.printStackTrace();
				
				//Attempt to close the socket if it is not null
				if(socket != null)
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		}
		

		/* *********************************************************************************************
		 * Getters/Setters
		 * *********************************************************************************************/
		@SuppressWarnings("unused")
		public Message getMessage() {
			return message;
		}

		@SuppressWarnings("unused")
		public String getDestination() {
			return destination;
		}


	}
}
