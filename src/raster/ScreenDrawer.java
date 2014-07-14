package raster;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import process.logging.Logger;

public class ScreenDrawer extends JFrame {
	
	/*
	 * A simple delegate for handling the pushing of pixels to screen
	 * The general idea is that the ray caster can draw pixels to the buffer
	 * 	as they come and the screen will refresh at a constant rate regardless
	 * Based on the stack overflow post: http://stackoverflow.com/questions/6478693/java-fast-pixel-operations
	 */
	/*
	 * TODO:
	 * 
	 * 		-Determine when the window size changes, and push the new width/height to the
	 * 			rest of the system (camera, pixel buffer, etc.)
	 * 		-
	 */
	
	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 1L;

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	//protected JFrame frame;
	protected JPanel panel;
	
    protected int width;
    protected int height;
    
    protected long msPerFrame = 49;//About 20fps
    protected long frames = 0;
	
    protected PixelBuffer pixelBuffer;

	protected boolean refreshScreen = true;
	protected boolean verticalSynchronize = true;//Setting this to true fixes screen tearing...and murders the frame rate
	
	protected boolean loggingFramesPerSecond = false;
	

	/* *********************************************************************************************
	 * Constructor
	 * *********************************************************************************************/
	public ScreenDrawer(final int width, final int height)
	{
		//Init frame
		super();
		
		//Initialize instance vars
		this.width = width;
		this.height = height;
		this.pixelBuffer = new PixelBuffer(width, height);
		
		//Launch the frame on a separate thread
		SwingUtilities.invokeLater(new Runnable()
		{
            @Override
            public void run()
            {
                //frame = new JFrame();
                panel = new ScreenPanel();
                getContentPane().add(panel, BorderLayout.CENTER);
                setSize(width, height);
                setUndecorated(true);
                setVisible(true);
            }
        });
	}
	
	
	/* *********************************************************************************************
	 * Getters/Setters
	 * *********************************************************************************************/
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		//TODO: Update buffer, etc.
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		//TODO: Update buffer, etc.
		this.height = height;
	}

	public long getMsPerFrame() {
		return msPerFrame;
	}

	public void setMsPerFrame(long msPerFrame) {
		this.msPerFrame = msPerFrame;
	}

	public long getFrameCount() {
		return frames;
	}

	public PixelBuffer getPixelBuffer() {
		return pixelBuffer;
	}

	public void setPixelBuffer(PixelBuffer pixelBuffer) {
		this.pixelBuffer = pixelBuffer;
	}

	public boolean isRefreshingScreen() {
		return refreshScreen;
	}

	public void setRefreshingScreen(boolean refreshScreen) {
		this.refreshScreen = refreshScreen;
	}

	public boolean isVerticalSynchronized() {
		return verticalSynchronize;
	}

	public void setVerticalSynchronize(boolean verticalSynchronize) {
		this.verticalSynchronize = verticalSynchronize;
	}

	public boolean isLoggingFramesPerSecond() {
		return loggingFramesPerSecond;
	}

	public void setLoggingFramesPerSecond(boolean loggingFramesPerSecond) {
		this.loggingFramesPerSecond = loggingFramesPerSecond;
	}


	/* *********************************************************************************************
	 * Private Classes
	 * *********************************************************************************************/
	/**
	 * 
	 * 
	 */
	private class ScreenPanel extends JPanel
	{
	    /**
		 * 
		 */
		private static final long serialVersionUID = -6941685821781413090L;

		
		/* *********************************************************************************************
		 * Instance Vars
		 * *********************************************************************************************/
		private Image src = null;
	    private ScreenWorker worker;

	    
		/* *********************************************************************************************
		 * Constructor
		 * *********************************************************************************************/
	    public ScreenPanel()
	    {
	        worker = new ScreenWorker();
	        worker.execute();
	    }

	    
		/* *********************************************************************************************
		 * PaintingMethods
		 * *********************************************************************************************/
	    protected void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	        if (src != null) g.drawImage(src, 0, 0, this);
	    }
	    

		/* *********************************************************************************************
		 * Even More Private Classes
		 * *********************************************************************************************/
	    /**
	     * 
	     *
	     */
	    private class ScreenWorker extends SwingWorker<Void, Image>
	    {

	    	/* *********************************************************************************************
	    	 * Instance Vars
	    	 * *********************************************************************************************/
	        private BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        private Graphics2D g2 = bi.createGraphics();

	    	/* *********************************************************************************************
	    	 * Worker Overrides
	    	 * *********************************************************************************************/
	        /**
	         * 
	         */
	        protected void process(List<Image> chunks)
	        {
	            for (Image bufferedImage : chunks)
	            {
	                src = bufferedImage;
	                repaint();
	            }
	        }
	        
	        /**
	         * 
	         */
	        protected Void doInBackground() throws Exception
	        {
	            long start = System.currentTimeMillis();
            	long loopStart = 0;
            	long sleepTime = 0;
            	
	            while (refreshScreen)
	            {
	            	int[] mem = pixelBuffer.getPixels();
	            	loopStart = System.currentTimeMillis();
	            	
	            	//If its been about a second or so, print the current fps
	            	if(loopStart - start >= 1000) {
	            		if(loggingFramesPerSecond)
	            			Logger.progress(-2, "ScreenDrawer: FPS:[" + ((double) frames / (loopStart - start) * 1000) + "]");
	            		start = loopStart;
	            		frames = 0;
	            	}
	                
	            	//Load the pixel buffer into an image object
	                Image img = createImage(new MemoryImageSource(width, height, mem, 0, width));
	                
	                //If we vertical synchronizing is on, prevent tearing by using fresh buffers
	                if(verticalSynchronize) {
	                	g2.dispose();
	                	bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	                	g2 = bi.createGraphics();
	                }
	                
	                g2.drawImage(img, 0, 0, null);
	                
	                publish(bi);
	                frames++;
	                
	                //Determine how much time we need to sleep to maintain the desired ms per frame
	                sleepTime = msPerFrame - (System.currentTimeMillis() - loopStart);
	                if(sleepTime > 0)
	                	Thread.sleep(sleepTime);
	            }
	            
	            return null;
	        }
	    }
	    
	}
}
