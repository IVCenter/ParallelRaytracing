package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import process.logging.Logger;

public class NodeManager implements Iterable<Node> {
	
	/*
	 * A class for managing nodes
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected ArrayList<Node> nodes;
	protected HashMap<String, Node> idToNodeMap;
	

	/* *********************************************************************************************
	 * Cosntructor
	 * *********************************************************************************************/
	public NodeManager()
	{
		nodes = new ArrayList<Node>();
		idToNodeMap = new HashMap<String, Node>();
	}

	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public void addNode(Node node)
	{
		if(idToNodeMap.containsKey(node.getId()))
		{
			Logger.warning(-25, "NodeManager: Can not add two nodes with the same ID [" + node.getId() + "].");
			return;
		}
		
		idToNodeMap.put(node.getId(), node);
		nodes.add(node);
	}
	
	public void removeNode(Node node)
	{
		if(idToNodeMap.containsKey(node.getId()))
		{
			Logger.warning(-25, "NodeManager: Can not remove a node it does not contain. Node ID [" + node.getId() + "].");
			return;
		}
		
		idToNodeMap.put(node.getId(), null);
		nodes.remove(node);
	}
	
	public boolean hasNode(Node node)
	{
		return idToNodeMap.containsKey(node.getId());
	}

	
	/* *********************************************************************************************
	 * Non-Mutation Methods
	 * *********************************************************************************************/
	public int getNodeCount()
	{
		return nodes.size();
	}
	
	
	/* *********************************************************************************************
	 * Iterable Overrides
	 * *********************************************************************************************/
	@Override
	public Iterator<Node> iterator()
	{
		return nodes.iterator();
	}

}
