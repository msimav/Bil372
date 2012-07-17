package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.gson.Gson;

public class Utils {

	/**
	 * O anki tarihi doner. Loglama yaparken kullanilir
	 * @return
	 */
	public static String getDateTime() {
		return new Date().toString();
	}
	
	/**
	 * O anki tarihi doner ve VERITABANI tarafindan kullanilir.
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd H:m:s");
		return formatter.format(new Date());
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
		byte[] imageInByte = null;

		try {

			BufferedImage originalImage = ImageIO.read(new File(path));
			int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			//yeniden boyutlandirilma islemi
			BufferedImage resizedImage = Utils.resizeImage(originalImage,type);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();


		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return imageInByte;
	}

	/**
	 * Parametre olarak aldigi stringi cozumleyip ImageIcon a donusturur, JSON formati ile return eder.
	 * @param JSON formatli string
	 * @return JSON formatli string
	 */
	public static ImageIcon byteTOImage(byte[] imageInByte) {

		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage bImageFromConvert = null;

		try {
			bImageFromConvert = ImageIO.read(in);
		} catch (IOException e) {

			e.printStackTrace();
		}

		ImageIcon image = new ImageIcon(bImageFromConvert);

		return image;

	}

	/**
	 * Parametre olarak aldigi byte array i ImageIcon' a donusturur.
	 * @param byte array
	 * @return ImageIcon
	 */
	private static BufferedImage resizeImage(BufferedImage originalImage, int type){

		BufferedImage resizedImage = new BufferedImage(64, 64, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 64, 64, null);
		g.dispose();

		return resizedImage;
	}

	/**
	 * Bir dosyanin uzantisini almak icin kullanilir
	 * @param file uzantisi alinacak dosya
	 * @return dosyanin uzantisi
	 */
	public static String getExtension(File file) {
		String name = file.getName();
		int index = name.lastIndexOf('.');

		if(index > 0 && index < name.length() -1)
			return name.substring(index + 1).toLowerCase();
		else
			return null;
	}
}
