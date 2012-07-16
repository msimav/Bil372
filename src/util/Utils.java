package util;

import java.util.Date;

import com.google.gson.Gson;

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

	/**
	 * Verilen nesneyi json formatina cevirir.
	 * @param object json formatina cevrilmek istenen nesne
	 * @return json stringi
	 */
	public static <T> String toJSON(T object) {
		Gson converter = new Gson();
		return converter.toJson(object, object.getClass());
	}

	/**
	 * Json olarak verilmis stringi nerekli nesneye cevirir.
	 * @param json json kodlanmis string string
	 * @param classOfT json stringinin karsilik geldigi nesne
	 * @return karsilik gelen nesne
	 */
	public static <T> T fromJSON(String json, Class<T> classOfT) {
		Gson converter = new Gson();
		T data = converter.fromJson(json, classOfT);
		return data;
	}
	
	/**
	 * Database'in resim olarak byte arrayi donmesini saglayan fonksiyon.
	 * Path degiskeni aldigi icin istenen resim serverda bulunmalidir.
	 * @param path byte arrayine cevrilecek resim
	 * @return resmin byte arrayi
	 */
	public static byte[] getAvatar(String path){
		return null; // TODO implement it
	}
}
