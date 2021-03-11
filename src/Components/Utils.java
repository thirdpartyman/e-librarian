package Components;

import java.awt.Dimension;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	public static Dimension weight = new Dimension(5, 5);
	public static short currentYear = (short) Calendar.getInstance().get(Calendar.YEAR);
	
	public static void print(Object obj)
	{
		try {
			 StringWriter writer = new StringWriter();
			 ObjectMapper mapper = new ObjectMapper();			 
			 String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	         System.out.println(jsonInString);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String makeMultiLine(String text)
	{
		return "<html>" + text.replaceAll(" ", "<br>") + "</html>";
	}
}
