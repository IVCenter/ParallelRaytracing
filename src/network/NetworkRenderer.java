package network;

import process.logging.Logger;
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
	
	
	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public NetworkRenderer(NodeManager nodeManager)
	{
		this.nodeManager = nodeManager;
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
	}

	@Override
	public void render(RenderData data)
	{
		Logger.progress(-1, "Rendering...");
		//
	}

	
	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	//

}
