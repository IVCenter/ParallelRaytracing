package network;

import java.util.Iterator;

import network.send.MessageSender;
import process.logging.Logger;
import raytrace.camera.Camera;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;

public class NetworkRenderer implements Renderer {

	/*
	 * A configurable ray tracer that allows 
	 */
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected NodeManager nodeManager;
	protected MessageSender messageSender;
	
	protected int outstandingRequests = 0;
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public NetworkRenderer(NodeManager nodeManager, MessageSender messageSender)
	{
		this.nodeManager = nodeManager;
		this.messageSender = messageSender;
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
		Logger.progress(-1, "Updating...");
		/*
		 * Create update message
		 * Loop nodes
		 * 		send update message
		 */
		
		//For all nodes, send an update request
		for(Node node : nodeManager)
		{
			Message message = CommonMessageConstructor.createUpdateRequestMessage(data);
			messageSender.send(message, node.ip);
			
			outstandingRequests++;
		}
		
		//Wait until all nodes are done
		waitOnOutstandingRequests();
	}

	@Override
	public void render(RenderData data)
	{
		Logger.progress(-1, "Rendering...");
		
		//TODO: Per-frame Load balance
		
		//TODO: Decompose Camera
		Iterator<Camera> cameras = data.getScene().getActiveCamera().decompose(nodeManager.getNodeCount()).iterator();
		
		//
		
		//For all nodes, send a render request
		for(Node node : nodeManager)
		{
			Message message = CommonMessageConstructor.createRenderRequestMessage(cameras.next());
			messageSender.send(message, node.ip);
			
			outstandingRequests++;
		}
		
		//Wait until all nodes are done
		waitOnOutstandingRequests();
	}
	
	private void waitOnOutstandingRequests()
	{
		//Wait until all requests are completed
		while(outstandingRequests > 0) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	//
	public int getOutstandingRequests() {
		return outstandingRequests;
	}
	
	//Used by response handlers to alert the renderer when each node completes updating/rendering
	public synchronized void completedARequest()
	{
		if(outstandingRequests <= 0)
			return;

		outstandingRequests -= 1;
		
		if(outstandingRequests == 0)
		{
			this.notifyAll();
		}
	}

}
