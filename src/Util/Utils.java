package Util;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;

import Database.BBK;
import Database.HibernateUtil;

public class Utils {

	public static Dimension weight = new Dimension(5, 5);
	public static short currentYear = (short) Calendar.getInstance().get(Calendar.YEAR);

	public static void print(Object obj) {
		try {
			StringWriter writer = new StringWriter();
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			System.out.println(jsonInString);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String makeMultiLine(String text) {
		return "<html>" + text.replaceAll(" ", "<br>") + "</html>";
	}

	public static String hash(char[] chars) {
		MessageDigest d = null;
		try {
			d = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		d.reset();
		d.update(new String(chars).getBytes());
		return new String(d.digest());
	}
	
	public static <T> boolean fieldsAreNotNull(T object)
	{
    	for(Field field : object.getClass().getDeclaredFields())
    	{	
    		try {
				if (field.get(object) == null)
					return false;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return false;
			}
    	}
    	return true;
	}
	
	public static Date removeTime(Date date)
	{
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		return instance.getTime();
	}

	public static void reloadBBKtable() {
		try {

			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();

			File file = new File("BBK");
			FileReader fr;
			fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
			String line = reader.readLine();
			while (line != null) {
				var arr = line.split("\t");
				if (arr.length != 2) {
					System.err.println(arr[0]);
					continue;
				}
				System.out.print(arr.length);
				String index = arr[0];
				String text = arr[1];
				BBK bbk = new BBK(index, text);
				session.save(bbk);
				System.out.println(index);

				line = reader.readLine();
			}
			transaction.commit();

			session.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
