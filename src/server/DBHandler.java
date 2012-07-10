package server;

/**
 * Bu class veritabani ile baglanti kurarak ilgili islemleri gerceklestirir
 * @author mustafa
 *
 */
public class DBHandler {
	
	private static DBHandler instance;
	
	private DBHandler() {
		// Bu class singleton olacak
	}
	
	public static DBHandler getInstance() {
		return instance;
	}
	
	/**
	 * Bu fonksiyon aldigi oturum acma bilgileri ile oturum acar
	 * @param args: json formatinda olacak
	 * 			Ornegin ["username": "msimav", "passwd": "cokgizlisifre"] gibi
	 * @return oturum acma gecerli ise kullanicinin idsini, degilse -1
	 */
	public int login(String args) {
		return -1;
	}
	
	// Bunu test icin kullaniyorum
	public String test(String testarg) {
		return String.format("Test method returns '%s'.", testarg);
	}
}
