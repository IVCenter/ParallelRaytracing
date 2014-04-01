package process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import process.utils.StringUtils;

@SuppressWarnings("serial")
public class RecordList extends ArrayList<HashMap<String, String>> {

	/*
	 * A representation of a list of records.
	 */

	/* *********************************************************************************************
	 * Instance Vars
	 * *********************************************************************************************/
	private ArrayList<String> attributes;

	private int columnWidth = 32;


	/* *********************************************************************************************
	 * Constructors
	 * *********************************************************************************************/
	public RecordList()
	{
		attributes = new ArrayList<String>();
	}


	/* *********************************************************************************************
	 * Mutation Methods
	 * *********************************************************************************************/
	public int addEmptyRecord()
	{
		this.add(new HashMap<String, String>());
		return this.size()-1;
	}

	public void clear()
	{
		if(attributes != null)
			attributes.clear();
		super.clear();
	}


	/* *********************************************************************************************
	 * Concrete Methods
	 * *********************************************************************************************/

	


	/* *********************************************************************************************
	 * toString Methods
	 * *********************************************************************************************/
	public String toString()
	{
		StringBuilder fullStr = new StringBuilder();

		String header = addHeader(attributes);
		fullStr.append(header);
		fullStr.append("\r\n");
		fullStr.append("|");
		fullStr.append(StringUtils.makeString('-', header.length()-3) + "|");
		fullStr.append("\r\n");
		fullStr.append(addRows(attributes));

		return fullStr.toString();
	}

	private String addHeader(ArrayList<String> header)
	{
		StringBuilder headerString = new StringBuilder();

		headerString.append("| ");

		for(int index = 0; index < header.size(); index++)
			headerString.append( StringUtils.column(header.get(index), columnWidth) + " | " );

		return headerString.toString();
	}

	private String addRows(ArrayList<String> header)
	{
		StringBuilder rows = new StringBuilder();

		for(int index = 0; index < this.size(); index++) {
			rows.append("| ");
			rows.append(mapToString(header, this.get(index)));
			rows.append("\r\n");
		}

		return rows.toString();
	}

	private String mapToString(ArrayList<String> header, HashMap<String, String> map)
	{
		StringBuilder mapToString = new StringBuilder();

		for(String attr : header) {
			mapToString.append( StringUtils.column(map.get(attr), columnWidth) + " | " );
		}

		return mapToString.toString();
	}

	public boolean doesAttributesContain(String str)
	{
		return this.getAttributes().contains(str);
	}
	

	/* *********************************************************************************************
	 * Getter/Setting Methods
	 * *********************************************************************************************/
	public ArrayList<String> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(Collection<String> attrs)
	{
		this.attributes = new ArrayList<String>();
		
		for(String attr : attrs) 
		{
			if(attr == null || attr.isEmpty())
				continue;
			
			this.attributes.add(attr);
		}
	}

	public void setColumnWidth(int width)
	{
		columnWidth = width;
	}
}
