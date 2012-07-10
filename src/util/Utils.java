package util;

import java.util.Date;

public class Utils {

	/**
	 * O anki tarihi doner. Loglama yaparken kullanilir
	 * @return
	 */
	public static String getDate() {
		return new Date().toString();
	}
	
	/**
	 * Sifreleri veritabaninda hashlanmis olarak saklamak lazim
	 * http://stackoverflow.com/a/6565597/806261
	 * @param arg: md5lenecek string
	 * @return stringin md5li hali
	 */
	public static String md5hash(String arg) {
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        md.reset();
	        byte[] array = md.digest(arg.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
}
