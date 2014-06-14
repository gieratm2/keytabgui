package net.sf.keytabgui.util;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ToString {
	
	/**
	 * Wypisuje nazwę klasy wraz z listą pól i wartości tych pól.
	 * 
	 * @param obj
	 * @return
	 */
	public synchronized static String toString(Object obj){
		StringBuilder sb = new StringBuilder(obj.getClass().getSimpleName());
		
		// wypisywanie wartości pól
		sb.append('[');
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		Field f;
		boolean accessible;
		for (int i=0; i<declaredFields.length;i++){
			f = declaredFields[i];
			sb.append(f.getName());
			sb.append('=');
			try {
				accessible = f.isAccessible();
				if (!accessible)
					f.setAccessible(true);
				
				// poprawne wypisywanie tablic String[] i byte[]
				Object value = f.get(obj);
				if (value instanceof String[]) {
					sb.append(Arrays.toString((Object[])value));
				} else if (value instanceof byte[]) {
					sb.append(bytesToHex((byte[])value));
				} else {
					sb.append(value);
				}
				
				
				if (!accessible)
					f.setAccessible(accessible);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if (i<declaredFields.length-1)
				sb.append(", ");
		}
		sb.append(']');
		
		return sb.toString();
	}
	
	/**
	 * @see http://stackoverflow.com/a/9855338/1206341
	 */
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static void main(String[] args){
		byte[] b = {85, 120, 50, -89, -22, -16, 29, 9, 37, 116, 29, 127, 51, 8, 116, 124, -72, 117, -21, 126, 68, 43, -2, -96, -44, 9, 18, -43, -115, 65, -6, -18};
		System.out.println(bytesToHex(b).toLowerCase());
	}
}
