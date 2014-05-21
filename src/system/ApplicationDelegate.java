package system;

import java.io.IOException;

import network.CommonMessageConstructor;
import network.Message;
import network.NetworkRenderer;
import network.Node;
import network.NodeManager;
import network.handlers.ConfigurationHandler;
import network.handlers.IntermediateRenderResponseHandler;
import network.handlers.RegistrationHandler;
import network.handlers.RenderRequestHandler;
import network.handlers.RenderResponseHandler;
import network.handlers.UpdateRequestHandler;
import network.handlers.UpdateResponseHandler;
import network.listen.MessageListener;
import network.listen.NetworkMessageListener;
import network.send.MessageSender;
import network.send.NetworkMessageSender;
import input.DefaultKeyboard;
import input.Keyboard;
import process.Job;
import raster.PixelBuffer;
import raster.ScreenDrawer;
import raytrace.AnimationRenderer;
import raytrace.ConfigurableRayTracer;
import raytrace.ParallelRayTracer;
import raytrace.data.RenderData;
import raytrace.data.UpdateData;
import raytrace.framework.Renderer;

public class ApplicationDelegate extends Job{
	
	/*
	 * If controller specified:
	 * 		attempt to register (loop until success, or timeout reached)
	 * 
	 * If controller:
	 * 		branch to controller code
	 * 
	 * If display == true:
	 * 		draw pixel data to screen
	 * 
	 * If has children nodes:
	 * 		distribute workload and calls to children
	 * 
	 * If leaf node:
	 * 		perform rendering on given workload
	 * 
	 * Handles the main loop, including synchronizing between children nodes
	 * 		-Update, Render, Keyboard callbacks, etc.
	 */
	
	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	public static ApplicationDelegate inst;
	
	protected ScreenDrawer screenDrawer;
	protected PixelBuffer pixelBuffer;
	protected Keyboard keyboard;
	
	protected Renderer renderer;
	protected boolean isStarted;
	
	protected MessageListener messageListener;
	protected MessageSender messageSender;
	protected NodeManager nodeManager;
	protected Node thisNode;
	
	
	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ApplicationDelegate()
	{
		//Set the statically accessible instance to this
		//There should not be more than once instance of application delegate at a time
		inst = this;
		
		isStarted = false;
	}
	

	/* *********************************************************************************************
	 * Initialization
	 * *********************************************************************************************/
	@Override
	protected void initialize()
	{
		progress("Initializing Application Delegate...");
		
		
		//Create a node manager
		nodeManager = new NodeManager();
		
		
		//Create a node object for this node
		//TODO: Configure?
		thisNode = new Node();
		
		
		//Setup a network message listener
		try {
			messageListener = new NetworkMessageListener(Configuration.Networking.getMessageReceivePort(), 
														 Configuration.Networking.getMessageThreadCount());

			messageListener.addMessageHandler(new RegistrationHandler());
			messageListener.addMessageHandler(new ConfigurationHandler());
			messageListener.addMessageHandler(new UpdateRequestHandler());
			messageListener.addMessageHandler(new UpdateResponseHandler());
			messageListener.addMessageHandler(new RenderRequestHandler());
			messageListener.addMessageHandler(new IntermediateRenderResponseHandler());
			messageListener.addMessageHandler(new RenderResponseHandler());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Setup a network message sender
		try {
			messageSender = new NetworkMessageSender(Configuration.Networking.getMessageReceivePort(), 
					 								 Configuration.Networking.getMessageThreadCount());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		//Test message
		//Message regMsg = CommonMessageConstructor.createRegistrationMessage();
		//messageSender.send(regMsg, "137.110.52.67");
		

		//Drawing to Screen?
		configureAsDrawingToScreen(Configuration.isDrawingToScreen());
		
		//Is a leaf node?
		configureAsLeaf(Configuration.isLeaf());
		
		//Is a controller node?
		configureAsController(Configuration.isController());
		
		//Is a clock?
		configureAsClock(Configuration.isClock());
	}
	

	/* *********************************************************************************************
	 * Configuration
	 * *********************************************************************************************/
	public void configureAs(boolean shouldConfigure)
	{
		
	}
	
	public void configureAsDrawingToScreen(boolean shouldConfigure)
	{
		//If drawing to screen
		if(shouldConfigure)
		{
			screenDrawer = new ScreenDrawer(Configuration.getScreenWidth(), Configuration.getScreenHeight());
			screenDrawer.setVerticalSynchronize(false);
			screenDrawer.setMsPerFrame(1000/12);
			
			pixelBuffer = screenDrawer.getPixelBuffer();
			
			keyboard = Configuration.getKeyboard();
			if(keyboard == null) {
				keyboard = new DefaultKeyboard(pixelBuffer);
			}
			screenDrawer.addKeyListener(keyboard);
			
		}else{
			pixelBuffer = new PixelBuffer(Configuration.getScreenWidth(), Configuration.getScreenHeight());
		}
	}
	
	public void configureAsLeaf(boolean shouldConfigure)
	{
		//	If leaf its a ray tracer
		//	If has children its a network distribution renderer
		if(shouldConfigure)
		{
			renderer = new ConfigurableRayTracer(new ParallelRayTracer());
		}else{
			renderer = new NetworkRenderer(nodeManager, messageSender);
		}
	}
	
	public void configureAsController(boolean shouldConfigure)
	{
		if(shouldConfigure)
		{
			//
			
		}else{
			//If not a controller, attempt to register with controller
			//TOOD: register loop? how to kill once registered?
			
		}
	}
	
	public void configureAsClock(boolean shouldConfigure)
	{
		if(shouldConfigure)
		{
			//Wrap the current renderer in an animation renderer if it isn't one already
			if(renderer != null && !(renderer instanceof AnimationRenderer))
			{
				renderer = new AnimationRenderer(renderer);
			}
			
		}else{
			
			//TODO: What do?
			
		}
	}

	
	/* *********************************************************************************************
	 * Job Overrides
	 * *********************************************************************************************/
	@Override
	protected void begin()
	{
		//Initialize the renderer
		//This may establish network connections, or block until one is established
		renderer.initialize();
		
		//If this node is the clock, start the main loop, else, sleep this thread
		//The sleep prevents this Job subclass from finalizing, and instead of using
		//	a loop for timing, this relies on a parent node to send timing signals
		if(Configuration.isClock() /*&& Configuration.isLeaf()*/)
		{
			startMainLoop();
		}else{
			try {
				//In case of spurious wake ups
				for(;;)
					Thread.sleep(Long.MAX_VALUE);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void finalize()
	{
		//
	}

	
	/* *********************************************************************************************
	 * Main Loop Methods
	 * *********************************************************************************************/
	protected void startMainLoop()
	{
		//If not already started
		//if(isStarted) {
		//	warning("ApplicationDelegate: Main loop is already started.  The loop must be stopped before it can" +
		//			"be started again.");
		//	return;
		//}
		
		
		progress("Starting Main Loop...");
		
		//If this is a clock (implicit by this method being called), and its a lead, then this is a stand-alone node
		//So set the started flag to true
		if(Configuration.isLeaf)
			isStarted = true;
		
		//Configure the initial update data
		UpdateData udata = initUpdateData();
		
		//Configure the initial render data
		RenderData rdata = initRenderData();
		
		//Main Loop
		for(;;)//While spider face holds true
		{
			//While the render loop is paused
			while(!isStarted) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			//Update the update data
			udata.setScene(Configuration.getMasterScene());
			udata.setDt(0.0416667);
			
			//update
			renderer.update(udata);

			//update the render data
			rdata.setPixelBuffer(pixelBuffer);
			rdata.setScene(Configuration.getMasterScene());
			
			//render
			renderer.render(rdata);
		}
	}
	
	private UpdateData initUpdateData()
	{
		//Configure the initial update data
		UpdateData udata = new UpdateData();
		
		return udata;
	}
	
	private RenderData initRenderData()
	{
		//Configure the initial render data
		RenderData rdata = new RenderData();
		
		return rdata;
	}
	

	/* *********************************************************************************************
	 * Getters/Setters?
	 * *********************************************************************************************/
	public ScreenDrawer getScreenDrawer() {
		return screenDrawer;
	}

	public void setScreenDrawer(ScreenDrawer screenDrawer) {
		this.screenDrawer = screenDrawer;
	}

	public PixelBuffer getPixelBuffer() {
		return pixelBuffer;
	}

	public void setPixelBuffer(PixelBuffer pixelBuffer) {
		this.pixelBuffer = pixelBuffer;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public NodeManager getNodeManager() {
		return nodeManager;
	}

	public void setNodeManager(NodeManager nodeManager) {
		this.nodeManager = nodeManager;
	}

	public Node getThisNode() {
		return thisNode;
	}

	public void setThisNode(Node thisNode) {
		this.thisNode = thisNode;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	
	public void start() {
		this.isStarted = true;
	}
	
	public void stop() {
		this.isStarted = false;
	}
}
