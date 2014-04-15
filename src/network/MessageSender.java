package network;

public class MessageSender {
	
	/*
	 * NOTES:
	 * 
	 * 		RenderData stores job info (pixel to render, etc.)
	 * 		A  RenderRequest sends this data
	 * 		A received render request called render() on the current renderer with the given RenderData
	 * 		If its a clock it manages itself
	 * 		If the node is a tracing leaf, it calls a RenderReponse generation method after renderer returns?
	 * 		If it is not a leaf and not a clock, then it just waits for responses, and triggers its
	 * 			own response only when its childrens resposnes have all returned
	 * 
	 * 		Send intermediate message about small pixel blocks that were modified
	 * 			-Needs PixelModifiedHandler (or similar message handle support)
	 * 			-Send the message up the chain
	 */

}
