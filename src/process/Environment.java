package process;

import java.util.HashMap;
import java.util.Map;

import process.logging.Logger;

@SuppressWarnings("serial")
public class Environment extends HashMap<String, Object> {

	public Environment() {
		super();
	}

	private Environment(Object... objects) {
		super();
		this.populate(objects);
	}

	private Environment(Environment pop) {
		super();
		this.populate(pop);
	}

	private Environment populate(Object... objects)
	{
		Populate:{
		if(objects.length % 2 != 0)
			break Populate;

		for(int i = 0; i < objects.length;) {
			if(!(objects[i] instanceof String))
				break Populate;
			this.put((String)objects[i++], objects[i++]);
		}
	}
	return this;
	}

	private Environment populate(Environment pop) {
		this.putAll(pop);
		return this;
	}

	public static Environment list(Object... objects) {
		return new Environment(objects);
	}

	public static Environment map(Environment pop) {
		return new Environment(pop);
	}

	public Environment addList(Object... objects) {
		return Environment.map(this).populate(objects);
	}

	public Environment addMap(Environment pop) {
		return Environment.map(this).populate(pop);
	}

	public Environment set(String key, Object value) {
		this.put(key, value);
		return this;
	}

	public boolean containsKeys(String... keys) {
		for(String key : keys)
			if(!this.containsKey(key))
				return false;
		return true;
	}
	
	/* ****************************************************************************************************
	 * Type Casting Getter
	 * ****************************************************************************************************/
	
	/**
	 * Convert Object to given type via generics
	 */
	@SuppressWarnings("unchecked")
	protected <T> T convert(Object o)
	{
		//If the incoming object is null, then return null
		if(o == null) {
			Logger.warning(-1, "Environment.convert(Object): Object o is null, unable to convert");
			return null;
		}
		
		//Create a fake instance of T
		T t = ((T[])(new Object[1]))[0];

		//Attempt to cast o into a t of type T
		try{
			t = (T)o;
		}catch(Exception cce) {
			Logger.error(-1, "Environment.convert(Object): Unable to cast Object o into type " + t.getClass().getName());
			Logger.error(-1, cce.getMessage());
			Logger.error(-1, stackTraceToString(cce));
			return null;
		}
		
		return t;
	}
	
	public <T> T get(String key)
	{
		Object o = super.get(key);
		
		if(o == null) {
			Logger.warning(-1, "Environment.get(): Unable to find value for key [" + key + "].");
		}
		
		return convert(o);
	}
	

	
	/* ****************************************************************************************************
	 * toString Methods
	 * ****************************************************************************************************/
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		for(Map.Entry<String, Object> entry : this.entrySet())
		{
			sb.append("\t<");
			sb.append(safeToString(entry.getKey()));
			sb.append(" :,: ");
			sb.append(safeToString(entry.getValue()));
			sb.append(">, \r\n");
		}
		
		sb.append("]\r\n");
		
		return sb.toString();
	}
	
	private String safeToString(Object o)
	{
		if(o == null)
			return "null";
		
		return o.toString();
	}
	
	private String stackTraceToString(Exception e)
	{
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] elements = e.getStackTrace();
		for(StackTraceElement ele : elements)
		{
			sb.append(ele.toString() + "\n");
		}
		return sb.toString();
	}
}