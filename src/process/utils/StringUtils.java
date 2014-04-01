package process.utils;


public class StringUtils {
	
	/* *********************************************************************************************
	 * Static Utility Methods
	 * *********************************************************************************************/
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsOnlyNumbers(String str)
	{
		if(str == null || str.isEmpty())
			return false;
		
		for(int i = 0, len = str.length(); i < len; i++)
			if(!Character.isDigit(str.charAt(i)))
				return false;
		
		return true;
	}
	
	/**
	 * 
	 * @param number
	 * @param width
	 * @return
	 */
	public static String zeroPad(long number, long width)
	{
		String numberString = Long.toString(number);
		long delta = width - numberString.length();
		
		if(delta < 1)
			return numberString;
		
		StringBuilder result = new StringBuilder();
		while(delta-- > 0)
			result.append(0);
		
		return result.append(numberString).toString();
	}
	
	/**
	 * 
	 * @param str
	 * @param width
	 * @return
	 */
	public static String spacePadBack(String str, long width)
	{
		long delta = width - str.length();
		
		if(delta < 1)
			return str;
		
		StringBuilder result = new StringBuilder();
		result.append(str);
		
		while(delta-- > 0)
			result.append(" ");
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @param width
	 * @return
	 */
	public static String column(String str, int width)
	{
		if(str == null)
			str = "null";
		
		long delta = width - str.length();
		
		if(delta == 0)
			return str;
		
		if(delta < 0)
			return str.substring(0, width);
		
		StringBuilder result = new StringBuilder();
		result.append(str);
		
		while(delta --> 0)
			result.append(" ");
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @param width
	 * @return
	 */
	public static String makeString(char str, int width)
	{
		if(width < 1)
			return "";
		
		StringBuilder result = new StringBuilder();
		
		while(width --> 0)
			result.append(str);
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSpacesAndPunctuation(String str)
	{
		StringBuilder result = new StringBuilder();
		
		Character c;
		for(int i = 0; i < str.length(); i++) {
			 c = str.charAt(i);
			
			if(!StringUtils.isSpaceOrPunctuation(c))
				result.append(c);
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isSpaceOrPunctuation(char c)
	{
		return (Character.isWhitespace(c) || !Character.isLetterOrDigit(c));
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String stripPunctuation(String str)
	{
		StringBuilder result = new StringBuilder();
		
		Character c;
		for(int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			
			if(Character.isLetterOrDigit(c) || Character.isWhitespace(c))
				result.append(c);
		}
		
		return result.toString();
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotVisible(String str)
	{
		return !(StringUtils.isVisible(str));
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isVisible(String str)
	{
		if(str == null || str.isEmpty())
			return false;
		
		for(int i = 0; i < str.length(); i++)
			if(!Character.isWhitespace(str.charAt(i)))
				return true;
		
		return false;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
		for(int i = 0, len = str.length(); i < len; i++)
			if(!Character.isDigit(str.charAt(i)))
				return false;
		
		return true;
	}

	/**
	 * 
	 * @param minLen
	 * @param maxLen
	 * @return
	 */
	public static String randomString(int minLen, int maxLen)
	{
		int len = (int)(Math.random() * (maxLen-minLen)) + minLen;

		StringBuilder result = new StringBuilder();

		for(int i = 0; i < len; i++)
			result.append((char)((int)(Math.random() * 52) + 65));

		return result.toString();
	}
	
	/**
	 * 
	 * @param e
	 * @return
	 */
	public static String stackTraceToString(Exception e)
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
