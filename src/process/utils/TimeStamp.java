package process.utils;

import java.util.Date;

import process.utils.StringUtils;

public class TimeStamp {

	/* *********************************************************************************************
	 * Static Utility Methods
	 * *********************************************************************************************/
	/**
	 * 
	 * @return
	 */
	public static String makeEmpty()
	{
		return "[0000/00/00][00:00:00.000]";
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String make()
	{
		String temp = new String("");
		Date timeStamp = new Date();
		temp = "[" + (1900 + timeStamp.getYear()) + "/" + (1 +timeStamp.getMonth()) + "/" + timeStamp.getDate() + "][" + timeStamp.getHours() + ":" + (timeStamp.getMinutes() < 10?("0" + timeStamp.getMinutes()):(timeStamp.getMinutes())) + ":" + (timeStamp.getSeconds() < 10?("0" + timeStamp.getSeconds()):(timeStamp.getSeconds())) + "]\t";
		return temp;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String makeForFileName()
	{
		String temp = new String("");
		Date timeStamp = new Date();
		temp = "" + (1900 + timeStamp.getYear()) + "" + (1 +timeStamp.getMonth()) + "" + timeStamp.getDate() + "-" + timeStamp.getHours() + "." + (timeStamp.getMinutes() < 10?("0" + timeStamp.getMinutes()):(timeStamp.getMinutes())) + "." + (timeStamp.getSeconds() < 10?("0" + timeStamp.getSeconds()):(timeStamp.getSeconds())) + "";
		return temp;
	}
	
	/**
	 * 
	 * @param l
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String makeFromLong(long l)
	{
		String temp = new String("");
		Date timeStamp = new Date(l);
		temp = "[" + (1900 + timeStamp.getYear()) + "/" + (1 +timeStamp.getMonth()) + "/" + timeStamp.getDate() + "][" + timeStamp.getHours() + ":" + (timeStamp.getMinutes() < 10?("0" + timeStamp.getMinutes()):(timeStamp.getMinutes())) + ":" + (timeStamp.getSeconds() < 10?("0" + timeStamp.getSeconds()):(timeStamp.getSeconds())) + "." + StringUtils.zeroPad(l%1000, 3) + "]";
		return temp;
	}
	
	/**
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String makeFromDate(Date timeStamp)
	{
		return makeFromLong(timeStamp.getTime());
	}

}
