package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import process.logging.Logger;

public class NetworkMessageListener extends MessageListener implements Runnable {
	
	/*
	 * A network based message listener
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected final ServerSocket serverSocket;
	protected final ExecutorService pool;
	protected final Thread thisThread;

	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public NetworkMessageListener(int port, int poolSize) throws IOException
	{
		super();
		serverSocket = new ServerSocket(port);
		pool = Executors.newFixedThreadPool(poolSize);
		thisThread = new Thread(this);
		thisThread.start();
	}
	

	/* *********************************************************************************************
	 * Runnable Overrides
	 * *********************************************************************************************/
	public void run()
	{
		Logger.progress(-20, "NetworkMessageListener: Starting...");
		try{
			//While spidey face holds true, serve the socket
			for(;;)
			{
				pool.execute(new Handler(serverSocket.accept()));
			}
			
		} catch (IOException ex) {
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
	 * Private Classes
	 * *********************************************************************************************/
	private class Handler implements Runnable
	{
		/*
		 * A handler for incoming messages
		 */
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		private final Socket socket;
		

		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
		Handler(Socket socket)
		{ 
			this.socket = socket;
		}
		

		/* *********************************************************************************************
		 * Runnable Override
		 * *********************************************************************************************/
		public void run()
		{
			try {
				
				Logger.progress(-21, "NetworkMessageListener: Serving socket...");
				ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
				Message message = (Message)stream.readObject();
				stream.close();
				listen(message);
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
