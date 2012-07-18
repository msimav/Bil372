package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
			pst.setString(4, "avatar/default.jpg");
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
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM Topic INNER JOIN User ON Topic.userid = User.id ORDER BY date DESC");

			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				User topicUser = new User(rs.getInt("User.id"), rs.getString("User.name") , rs.getString("User.email"), null, Utils.getAvatar(rs.getString("User.avatar")));
				arrayL.add(new Topic(rs.getInt("Topic.id"), topicUser, rs.getTimestamp("Topic.date").toString(), rs.getString("Topic.title"), null, null));
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
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM Post INNER JOIN User ON Post.userid = User.id WHERE topicid = ? ORDER by Post.date ASC");
			pst.setInt(1, topic.getId());

			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				Post reply = null;
				if(rs.getInt("Post.reply") != -1) {
					PreparedStatement replyrst = conn.prepareStatement("SELECT * FROM Post INNER JOIN User ON Post.userid = User.id WHERE Post.id = ?");
					replyrst.setInt(1, rs.getInt("Post.reply"));
					ResultSet replyrs = replyrst.executeQuery();
					replyrs.next(); //there is just one row returned
					User postUser = new User(replyrs.getInt("User.id"), replyrs.getString("User.name"), null, null, null);
					reply = new Post(replyrs.getInt("Post.id"), postUser, null, replyrs.getDate("Post.date").toString(), replyrs.getString("Post.post"), null);
				}
				User postUser = new User(rs.getInt("User.id"), rs.getString("User.name"), rs.getString("User.email"), null, Utils.getAvatar(rs.getString("User.avatar")));
				arrayL.add(new Post(rs.getInt("Post.id"), postUser, topic, rs.getTimestamp("Post.date").toString(), rs.getString("Post.post"), reply));
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
		try {
			PreparedStatement pst = conn.prepareStatement("INSERT INTO Topic VALUES(null , ? , ? , ?)");
			pst.setInt(1, newtopic.getUser().getId());
			pst.setTimestamp(2, Timestamp.valueOf( newtopic.getDate()));
			pst.setString(3, newtopic.getTitle());
			pst.executeUpdate();

			pst = conn.prepareStatement("SELECT * FROM Topic INNER JOIN User ON User.id = Topic.userid WHERE Topic.id = (SELECT MAX(Topic.id) from Topic)");
			ResultSet rs = pst.executeQuery();
			rs.next();

			User topicUser = new User( rs.getInt("User.id"), rs.getString("User.name"), rs.getString("User.email"), null, Utils.getAvatar( rs.getString("User.avatar")));
			Topic topic = new Topic(rs.getInt("Topic.id"), topicUser, rs.getTimestamp("Topic.date").toString() ,rs.getString("Topic.title"), null, null);

			pst = conn.prepareStatement("INSERT INTO Post(userid, topicid, date, post) VALUES (? , ? , ? , ?)");
			pst.setInt(2, topic.getId());
			pst.setInt(1, topicUser.getId());
			pst.setTimestamp(3, Timestamp.valueOf( topic.getDate()));
			pst.setString(4, newtopic.getFirstPost().getPost());
			pst.executeUpdate();

			return topic;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		PreparedStatement pst;
		try {
			pst = conn.prepareStatement("INSERT INTO Post(userid, topicid, date, post, reply) VALUES(? , ? , ? , ? , ?)");
			pst.setInt(2, newpost.getTopic().getId());
			pst.setInt(1, newpost.getUser().getId());
			pst.setTimestamp(3, Timestamp.valueOf( newpost.getDate()));
			pst.setString(4, newpost.getPost());

			if( newpost.getReply() != null )					// Reply null degilse reply id sini database e yaz , null ise idsini -1 olarak yaz.
				pst.setInt(5, newpost.getReply().getId());
			else
				pst.setInt(5, -1);

			pst.executeUpdate();

			pst = conn.prepareStatement("SELECT * FROM Post WHERE Post.id = (SELECT MAX(Post.id) from Post)");
			ResultSet rs = pst.executeQuery();
			rs.next();

			Post post = new Post(rs.getInt("id"), newpost.getUser(), newpost.getTopic(), rs.getTimestamp("date").toString() , rs.getString("post"), newpost.getReply());
			return post;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		ArrayList<User> arrayL = new ArrayList<User>();
		User [] diziT = null;
		try{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM User WHERE NOT id = ?");
			pst.setInt(1, user.getId());

			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				arrayL.add(new User(rs.getInt("User.id"), rs.getString("User.name") , rs.getString("User.email"), null, Utils.getAvatar(rs.getString("User.avatar"))));
			}
		}catch(Exception e){
			e.getStackTrace();
		}
		diziT = arrayL.toArray(new User[arrayL.size()]);
		return diziT;
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
		try {
			PreparedStatement pst = conn.prepareStatement("INSERT INTO PrivateMessage VALUES( null , ? , ? , ? , ? )");
			pst.setInt(1, newpm.getTo().getId());
			pst.setInt(2, newpm.getFrom().getId());
			pst.setTimestamp(3, Timestamp.valueOf( newpm.getDate() ));
			pst.setString(4, newpm.getMessage());
			pst.executeUpdate();

			pst = conn.prepareStatement("SELECT * FROM PrivateMessage WHERE id = (SELECT MAX(id) FROM PrivateMessage)");
			ResultSet rs = pst.executeQuery();
			rs.next();

			PrivateMessage newMessage = new PrivateMessage(rs.getInt("id"), newpm.getTo() , newpm.getFrom() , rs.getTimestamp("date").toString() , rs.getString("message"));
			return newMessage;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		ArrayList<PrivateMessage> pmList = new ArrayList<PrivateMessage>();
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM PrivateMessage pm INNER JOIN User ON User.id = pm.fromid WHERE pm.toid = ? AND pm.id = (SELECT MAX(id) FROM PrivateMessage WHERE pm.fromid = fromid) ORDER BY date DESC");
			pst.setInt(1, user.getId());

			ResultSet rs = pst.executeQuery();
			while( rs.next() ) {
				User from = new User(rs.getInt("User.id") , rs.getString("User.name") , rs.getString("User.email") , null , Utils.getAvatar(rs.getString("User.avatar")) );
				PrivateMessage newMessage = new PrivateMessage( rs.getInt("id") , user , from , rs.getTimestamp("date").toString() , rs.getString("message"));
				pmList.add(newMessage);
			}

			return pmList.toArray(new PrivateMessage[pmList.size()]);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
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
		ArrayList<PrivateMessage> pmList = new ArrayList<PrivateMessage>();
		try {
			PreparedStatement pst = conn.prepareStatement("(SELECT * FROM PrivateMessage WHERE toid = ? AND fromid = ?) UNION (SELECT * FROM PrivateMessage WHERE toid = ? AND fromid = ?) ORDER BY date DESC");
			pst.setInt(1, me.getId());
			pst.setInt(2, friend.getId());
			pst.setInt(4, me.getId());
			pst.setInt(3, friend.getId());

			ResultSet rs = pst.executeQuery();
			while( rs.next() ) {
				PrivateMessage pm = new PrivateMessage( rs.getInt("id"), me, friend , rs.getTimestamp("date").toString(), rs.getString("message"));
				pmList.add(pm);
			}

			PrivateMessage[] result = pmList.toArray(new PrivateMessage[pmList.size()]);
			return result;
		}
		catch(Exception e) {

		}
		return null;
	}

	public User updateUser( User user ) {		
		try {
			User login = this.login(user);
			if( login != null ) {
				PreparedStatement pst = conn.prepareStatement("UPDATE User SET name = ? , passwd = ? , avatar = ? WHERE id = ?");
				pst.setString(1, user.getName());
				pst.setString(2, user.getPasswd());
				pst.setString(3, Utils.writeImageToFile(user.getAvatar(), user.getEmail()));
				pst.setInt(4,  user.getId());
				pst.executeUpdate();

				return user;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void loginLog( User user , String ip ) {
		try {
			PreparedStatement pst = conn.prepareStatement("INSERT INTO LoginLog VALUES( ? , ? , ?)");
			pst.setInt(1, user.getId());
			pst.setTimestamp(2, Timestamp.valueOf( Utils.getDate() ));
			pst.setString(3, ip);
			pst.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
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