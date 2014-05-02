package network;

import process.utils.TimeStamp;

public class Node {
	
	/*
	 * Information about a Node
	 */
	/* *********************************************************************************************
	 * Static Vars
	 * *********************************************************************************************/
	private static int nextNumber = 0;
	private static synchronized int nextNumber() { return nextNumber++; }
	

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	protected int nodeNumber;
	protected String id;
	protected String ip;
	
	protected int numberOfCores;
	protected double loadPercent;
	protected double memoryPercent;
	protected double performanceScore;
	
	protected long registrationTime;
	protected long lastMessageTime;
	
	
	/* *********************************************************************************************
	 * Cosntructor
	 * *********************************************************************************************/
	public Node() 
	{
		nodeNumber = nextNumber();
		id = "Unassigned";
		ip = "0.0.0.0";
		
		numberOfCores = Runtime.getRuntime().availableProcessors();
		loadPercent = 0.0;
		memoryPercent = 0.0;
		performanceScore = 1.0;
		
		registrationTime = 0L;
		lastMessageTime = 0L;
	}
	

	/* *********************************************************************************************
	 * toString Method
	 * *********************************************************************************************/
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Node:[");
		sb.append(nodeNumber);
		sb.append(" - ");
		sb.append(ip);
		sb.append(" - ");
		sb.append(id);
		sb.append("]\n");

		sb.append("\tCores [" + numberOfCores + "]\n");
		sb.append("\tLoad Percent [" + loadPercent + "]\n");
		sb.append("\tMemory Percent[" + memoryPercent + "]\n");
		sb.append("\tPerformance Score[" + performanceScore + "]\n");

		sb.append("\tRegistration Time [" + (TimeStamp.makeFromLong(registrationTime)) + "]\n");
		sb.append("\tLast Activity Time [" + (TimeStamp.makeFromLong(lastMessageTime)) + "]\n");
		
		return  sb.toString();
	}
	

	/* *********************************************************************************************
	 * Getter/Setter Methods
	 * *********************************************************************************************/
	public int getNodeNumber() {
		return nodeNumber;
	}

	public void setNodeNumber(int nodeNumber) {
		this.nodeNumber = nodeNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getNumberOfCores() {
		return numberOfCores;
	}

	public void setNumberOfCores(int numberOfCores) {
		this.numberOfCores = numberOfCores;
	}

	public double getLoadPercent() {
		return loadPercent;
	}

	public void setLoadPercent(double loadPercent) {
		this.loadPercent = loadPercent;
	}

	public double getMemoryPercent() {
		return memoryPercent;
	}

	public void setMemoryPercent(double memoryPercent) {
		this.memoryPercent = memoryPercent;
	}

	public double getPerformanceScore() {
		return performanceScore;
	}

	public void setPerformanceScore(double performanceScore) {
		this.performanceScore = performanceScore;
	}

	public long getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(long registrationTime) {
		this.registrationTime = registrationTime;
	}

	public long getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(long lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}
}
