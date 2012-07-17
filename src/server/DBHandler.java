package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.Utils;

import beans.*;

/*
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
			String url = "jdbc:mysql://192.168.2.140/";
			String dbName = "dalga";
			String userName = "lan";
			String password = "cokgizlisifre";
			conn = DriverManager.getConnection(url+dbName,userName,password);
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
	 * Ornegin {"username": "msimav", "passwd": "cokgizlisifre"} gibi
	 * @return oturum acma gecerli ise kullanicinin idsini, degilse -1
	 */
	public User login(User login) {
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM User WHERE	email = ? AND passwd = ?");
			pst.setString(1, login.getEmail());
			pst.setString(2, login.getPasswd());

			ResultSet rs = pst.executeQuery();
			if(rs.next())
				return new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"), null, Utils.getAvatar(rs.getString("avatar")));
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
	 * {"kullaniciAdi": "msimav", "email": "mustafa1991@gmail.com", "sifre":
"cokgizlisifre"}
	 * formatinda, email kontrolu yapilacak, email unique olacak
	 * @return kayit basarili ise kullanicinin bilgileri
	 * {"kullaniciID": 32, "kullaniciAdi": "msimav"} seklinde
	 * degilse null
	 */
	public boolean userExists(String email){
		try{
			PreparedStatement pst = conn.prepareStatement("SELECT COUNT(email) FROM	User WHERE email = ?");
			pst.setString(1, email);

			ResultSet rs = pst.executeQuery();
			rs.next();

			if(rs.getInt(1) != 0)
				return true;
			else
				return false;
		}catch(Exception e){
			e.getStackTrace();
		}
		return false;
	}

	public User register(User newuser) {
		try{
			PreparedStatement pst = conn.prepareStatement("INSERT INTO User	VALUES(null, ? , ? , ? , ?)");
			pst.setString(1, newuser.getName());
			pst.setString(2, newuser.getEmail());
			pst.setString(3, newuser.getPasswd());
			pst.setString(4, "default");
			// TODO default olan kisma resim eklencek
			pst.executeUpdate();
			return newuser;
		}catch(Exception e){
			e.getStackTrace();
		}
		return null;
	}

	/**
	 * Topic listesini doner. Eger tag belirtilmisse o tag ile taglenmis
topicleri doner.
	 * Bu fonksiyon kullanicinin goremedigi topicleri donmez. (TopicUser
tablosuna dikkat)
	 * @param args json formatinda {"kullaniciID": 895} veya {"kullaniciID":
09, "topic": ["deneme", "adas"]} gibi
	 * @return kullanicinin goruntuleyebilecegi topiclerin listesi
	 * bu listeyi donerken istenilen alanlar:
	 * * topicID
	 * * username (topic'i acan kullanicinin adi, databasede sadece id'si var
ordan ismine ulasin)
	 * * date
	 * * title
	 * * postCount (topic altindaki postlari sayiverin bi zahmet :) )
	 */
	public Topic[] getTopicList(User user) {
		ArrayList<Topic> arrayL = new ArrayList<Topic>();
		Topic [] diziT = null;
		try{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM topic INNER JOIN user ON topic.userid = user.id");

			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				User topicUser = new User(rs.getInt("user.id"), rs.getString("user.name") , rs.getString("user.email"), null, Utils.getAvatar(rs.getString("user.avatar")));
				arrayL.add(new Topic(rs.getInt("topic.id"), topicUser, rs.getDate("topic.date").toString(), rs.getString("topic.title"), null, null));
				//TODO utils.get avatar , tagleri getir
			}
		}catch(Exception e){
			e.getStackTrace();
		}
		diziT = arrayL.toArray(new Topic[arrayL.size()]);
		return diziT;
	}

	/**
	 * Bir topic icindeki postlarin listesini doner
	 * @param args kullanici ve topic idleri {"kullaniciID": 943, "topicID": 53}
	 * @return post listesi
	 * bu listeyi donerken istenilen alanlar:
	 * * postId
	 * * username (post'u atan kullanicinin adi, databasede sadece id'si var
ordan ismine ulasin)
	 * * post
	 * * replyId
	 */
	public Post[] getPost(Topic topic) {
		ArrayList<Post> arrayL = new ArrayList<Post>();
		Post [] diziP = null;
		try{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM post INNER JOIN user ON post.userid = user.id WHERE topicid = ?");
			pst.setInt(1, topic.getId());

			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				Post reply = null;
				if(rs.getInt("post.reply") != -1) {
					PreparedStatement replyrst = conn.prepareStatement("SELECT * FROM post INNER JOIN user ON post.userid = user.id WHERE post.id = ?");
					replyrst.setInt(1, rs.getInt("post.reply"));
					ResultSet replyrs = replyrst.executeQuery();
					replyrs.next(); //there is just one row returned
					User postUser = new User(replyrs.getInt("user.id"), replyrs.getString("user.name"), null, null, null);
					reply = new Post(replyrs.getInt("post.id"), postUser, null, replyrs.getDate("post.date").toString(), replyrs.getString("post.post"), null);
				}
				User postUser = new User(rs.getInt("user.id"), rs.getString("user.name"), rs.getString("user.email"), null, Utils.getAvatar(rs.getString("user.avatar")));
				arrayL.add(new Post(rs.getInt("post.id"), postUser, topic, rs.getDate("post.date").toString(), rs.getString("post.post"), reply));
				//TODO utils.get avatar
			}
		}catch(Exception e){
			e.getStackTrace();
		}
		diziP = arrayL.toArray(new Post[arrayL.size()]);
		return diziP;
	}

	/**
	 * Kullanicinin baslik acmasini saglar. Baslikla beraber ilk post'u da
olusturur.
	 * @param args baslik acmasi icin gerekli bilgiler
	 * * userId
	 * * date
	 * * title
	 * * post
	 * @return baslik acma basarili veya degil 1/0
	 */
	public Topic createTopic(Topic newtopic) {
		return null;
	}

	/**
	 * Kullanicilarin bir basliga mesaj gondermelerini saglar
	 * @param args mesaj atmak icin gerekli bilgiler
	 * * userId
	 * * date
	 * * post
	 * * replyId
	 * @return mesaj atma islemi basarili ise 1 degilse 0
	 */
	public Post createPost(Post newpost) {
		return null;
	}

	/**
	 * Kayitli kullanicilarin listesini doner
	 * @param args herhangi bir arg yok simdilik :)
	 * @return kullanicilar listesi
	 * * userId
	 * * name
	 * * email
	 * * avatar (bu kisim sorun cikarabilir bu kismi implement etmeyi sona
birakabilirsiniz)
	 */
	public User[] userList(User user) {
		return null;
	}

	/**
	 * Ozel mesaj gondermeye yarayan fonksiyon
	 * @param args mesaj atmak icin gerekli bilgiler
	 * * fromId: mesaji gonderen kullanicinin id'si
	 * * toId: mesaji alan kullanicinin id'si
	 * * date
	 * * message
	 * @return basarili 1/ basarisiz 0
	 */
	public PrivateMessage sendPM(PrivateMessage newpm) {
		return null;
	}

	/**
	 * Kullaniciya mesaj gonderen kullanicilarin ve son gonderdikleri
mesajlarin listesi
	 * Bu bolumu telefondaki mesaj ekran gibi dusunun yani mesaj gonderenleri
listeleyeceksiniz burada
	 * @param args gelen kutusu gosterilecek kullanicinin id'si
	 * @return gelen kutusundaki mesajlari
	 * * fromUserName: mesaji gonderen kullanicinin adi
	 * * date: bu kullanicinin son mesaj gonderdigi tarih
	 * * message: bu kullanicinin son gonderdigi mesaj
	 * * avatar: mesaji gonderen kullanicinin avatari
	 */
	public PrivateMessage[] getPMs(User user) {
		return null;
	}

	/**
	 * Gelen kutusundaki bir mesaja tiklandiginda bu iki kullanici arasindaki
tum
	 * mesajlasmalarin listesi (bilgidin android mesajlari gibi dusunun)
	 * yani sql ile cekerken "toid = ID OR fromid = ID" gibi bir sorgu
yazicaksiniz
	 * @param args arasindaki konusma getirilecek kullanicilarin idleri
	 * * meid: benim idim
	 * * friendid: konustugum arkadasimin idsi
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