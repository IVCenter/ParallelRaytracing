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
	protected HashMap<String, Node> ipToNodeMap;
	

	/* *********************************************************************************************
	 * Cosntructor
	 * *********************************************************************************************/
	public NodeManager()
	{
		nodes = new ArrayList<Node>();
		ipToNodeMap = new HashMap<String, Node>();
	}

	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public void addNode(Node node)
	{
		if(this.hasNode(node))
		{
			Logger.warning(-25, "NodeManager: Can not add two nodes with the same ID [" + node.getId() + "].");
			return;
		}
		
		ipToNodeMap.put(node.getIp(), node);
		nodes.add(node);
	}
	
	public void updateNode(Node node)
	{
		if(!this.hasNode(node))
		{
			Logger.warning(-25, "NodeManager: Can not update an unknown node with ID [" + node.getId() + "].");
			return;
		}
		
		Node existing = ipToNodeMap.get(node.getIp());
		existing.setId(node.getId());
		existing.setLastMessageTime(node.getLastMessageTime());
		existing.setLoadPercent(node.getLoadPercent());
		existing.setMemoryPercent(node.getMemoryPercent());
		existing.setNumberOfCores(node.getNumberOfCores());
		existing.setPerformanceScore(node.getPerformanceScore());
	}
	
	public void removeNode(Node node)
	{
		if(ipToNodeMap.containsKey(node.getId()))
		{
			Logger.warning(-25, "NodeManager: Can not remove a node it does not contain. Node ID [" + node.getId() + "].");
			return;
		}
		
		ipToNodeMap.put(node.getIp(), null);
		nodes.remove(node);
	}
	
	public boolean hasNode(Node node)
	{
		//TODO: Should this also check against IP?
		//TODO: Should two nodes ever have the same IP?
		/*
		Node existing = ipToNodeMap.containsKey(node.getId());
		if(existing == null)
			return false;
		
		return existing.getIp().equals(node.getIp());
		*/
		return ipToNodeMap.containsKey(node.getIp());
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
