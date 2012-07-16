package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.*;

/**
 * Bu class veritabani ile baglanti kurarak ilgili islemleri gerceklestirir
 * @author mustafa
 *
 */
public class DBHandler {

	private static DBHandler instance = null;
	private Connection conn = null;

	private DBHandler() {
		// Bu class singleton olacak
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://192.168.2.140/dalga?user=lan&password=cokgizlisifre");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DBHandler getInstance() {
		if(instance == null)
			instance = new DBHandler();
		return DBHandler.instance;
	}

	/**
	 * Bu fonksiyon aldigi oturum acma bilgileri ile oturum acar
	 * (ayni zamanda log da tutar)
	 * @param args: json formatinda olacak
	 * 			Ornegin {"username": "msimav", "passwd": "cokgizlisifre"} gibi
	 * @return oturum acma gecerli ise kullanicinin idsini, degilse -1
	 */
	public User login(User login) {
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM User WHERE email = ? AND passwd = ?");
			pst.setString(1, login.getEmail());
			pst.setString(2, login.getPasswd());

			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), null, null);
			else
				return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Kullanicinin kayit olmasini saglar.
	 * @param args kayit icin gerekli bilgiler
	 * 			{"kullaniciAdi": "msimav", "email": "mustafa1991@gmail.com", "sifre": "cokgizlisifre"}
	 * 			formatinda, email kontrolu yapilacak, email unique olacak
	 * @return kayit basarili ise kullanicinin bilgileri
	 * 			{"kullaniciID": 32, "kullaniciAdi": "msimav"} seklinde
	 * 			degilse null
	 */
	public User register(User newuser) {
		return null;
	}

	/**
	 * Topic listesini doner. Eger tag belirtilmisse o tag ile taglenmis topicleri doner.
	 * Bu fonksiyon kullanicinin goremedigi topicleri donmez. (TopicUser tablosuna dikkat)
	 * @param args json formatinda {"kullaniciID": 895} veya {"kullaniciID": 09, "topic": ["deneme", "adas"]} gibi
	 * @return kullanicinin goruntuleyebilecegi topiclerin listesi
	 * 			bu listeyi donerken istenilen alanlar:
	 * 			* topicID
	 * 			* username (topic'i acan kullanicinin adi, databasede sadece id'si var ordan ismine ulasin)
	 * 			* date
	 * 			* title
	 * 			* postCount (topic altindaki postlari sayiverin bi zahmet :) )
	 */
	public Topic[] getTopicList(User user) {
		return null;
	}

	/**
	 * Bir topic icindeki postlarin listesini doner
	 * @param args kullanici ve topic idleri {"kullaniciID": 943, "topicID": 53}
	 * @return post listesi
	 * 			bu listeyi donerken istenilen alanlar:
	 * 			* postId
	 * 			* username (post'u atan kullanicinin adi, databasede sadece id'si var ordan ismine ulasin)
	 * 			* post
	 * 			* replyId
	 */
	public Post[] getPost(Topic topic) {
		return null;
	}

	/**
	 * Kullanicinin baslik acmasini saglar. Baslikla beraber ilk post'u da olusturur.
	 * @param args baslik acmasi icin gerekli bilgiler
	 * 			* userId
	 * 			* date
	 * 			* title
	 * 			* post
	 * @return baslik acma basarili veya degil 1/0
	 */
	public Topic createTopic(Topic newtopic) {
		return null;
	}

	/**
	 * Kullanicilarin bir basliga mesaj gondermelerini saglar
	 * @param args mesaj atmak icin gerekli bilgiler
	 * 			* userId
	 * 			* date
	 * 			* post
	 * 			* replyId
	 * @return mesaj atma islemi basarili ise 1 degilse 0
	 */
	public Post createPost(Post newpost) {
		return null;
	}

	/**
	 * Kayitli kullanicilarin listesini doner
	 * @param args herhangi bir arg yok simdilik :)
	 * @return kullanicilar listesi
	 * 			* userId
	 * 			* name
	 * 			* email
	 * 			* avatar (bu kisim sorun cikarabilir bu kismi implement etmeyi sona birakabilirsiniz)
	 */
	public User[] userList(User user) {
		return null;
	}

	/**
	 * Ozel mesaj gondermeye yarayan fonksiyon
	 * @param args mesaj atmak icin gerekli bilgiler
	 * 			* fromId: mesaji gonderen kullanicinin id'si
	 * 			* toId: mesaji alan kullanicinin id'si
	 * 			* date
	 * 			* message
	 * @return basarili 1/ basarisiz 0
	 */
	public PrivateMessage sendPM(PrivateMessage newpm) {
		return null;
	}

	/**
	 * Kullaniciya mesaj gonderen kullanicilarin ve son gonderdikleri mesajlarin listesi
	 * Bu bolumu telefondaki mesaj ekran gibi dusunun yani mesaj gonderenleri listeleyeceksiniz burada
	 * @param args gelen kutusu gosterilecek kullanicinin id'si
	 * @return gelen kutusundaki mesajlari
	 * 			* fromUserName: mesaji gonderen kullanicinin adi
	 * 			* date: bu kullanicinin son mesaj gonderdigi tarih
	 * 			* message: bu kullanicinin son gonderdigi mesaj 
	 * 			* avatar: mesaji gonderen kullanicinin avatari 
	 */
	public PrivateMessage[] getPMs(User user) {
		return null;
	}

	/**
	 * Gelen kutusundaki bir mesaja tiklandiginda bu iki kullanici arasindaki tum
	 * mesajlasmalarin listesi (bilgidin android mesajlari gibi dusunun)
	 * yani sql ile cekerken "toid = ID OR fromid = ID" gibi bir sorgu yazicaksiniz
	 * @param args arasindaki konusma getirilecek kullanicilarin idleri
	 * 			* meid: benim idim
	 * 			* friendid: konustugum arkadasimin idsi
	 * @return
	 */
	public PrivateMessage[] getPMdetails(User me, User friend) {
		return null;
	}

	/**
	 * Bu fonsiyon veritabani baglantisini kapatir.
	 */
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Bunu test icin kullaniyorum
	public String test(String testarg) {
		return String.format("Test method returns '%s'.", testarg);
	}
}
