package client.gui.statistics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MysqlConnect{

	Connection conn = null;

	public void connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = "jdbc:mysql://127.0.0.1/";
		String dbName = "dalga";
		String userName = "ozan";
		String password = "123";
		try {			
			conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int[] getTopicId(){
		ArrayList<Integer> arrayL = new ArrayList<Integer>();
		int[] dizi = null;
		try {
			PreparedStatement st = conn.prepareStatement("SELECT topicid FROM post WHERE reply = ?");
			st.setInt(1, -1);
			ResultSet rs = st.executeQuery();
			
			while( rs.next() ) {
				arrayL.add(rs.getInt("topicid"));
			}

			dizi = new int[arrayL.size()];

			for(int i=0 ; i < arrayL.size() ; i++){
				dizi[i] = arrayL.get(i);
			}

			st.close();
		}
		catch( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return dizi;
	}
	
	public String getTopicName(int id) {
		
		String str = "boþ";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT title FROM topic WHERE id = '"+id+"'");
			if(rs.next())
				str = rs.getString("title");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}
	
	public String getUserName(int id) {
		String str = "boþ";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT name FROM user WHERE id = '"+id+"'");
			if(rs.next())
				str = rs.getString("name");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return str;
	}
	
	public int getReplyNumbers(int id){

		int count = 0;
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(topicid) FROM post WHERE topicid = '"+id+"' "); 
           
			while( rs.next() ) {
				count = rs.getInt(1);
			}

		}
		catch( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return count;
	}
	
	public int[] getLoginId() {
		ArrayList<Integer> arrayL = new ArrayList<Integer>();
		int[] dizi = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT userid FROM loginlog");
			while( rs.next() ) {
				arrayL.add(rs.getInt("userid"));
			}

			dizi = new int[arrayL.size()];

			for(int i=0 ; i<arrayL.size() ; i++){
				dizi[i] = arrayL.get(i);
			}

			st.close();
		}
		catch( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return dizi;
	}
	
	public int getLoginLog(int id) {
		
		int count = 0;
		
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(userid) FROM loginlog WHERE userid = '"+id+"' ");

			while( rs.next() ) {
				count = rs.getInt(1);
			}

		}
		catch( Exception e ) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return count;
		
	}
	
	
	public static void bubbleSort(ArrayList<ikiBoyutluArray> arr) {
		boolean doMore = true;
		while (doMore) {
			doMore = false;  // assume this is last pass over array
			for (int i=0; i<arr.size()-1; i++) {
				if (arr.get(i).numberOfPost < arr.get(i+1).numberOfPost) {
					// exchange elements
					
					ikiBoyutluArray temp = arr.get(i+1);
					arr.set((i+1),arr.get(i)); 
					arr.set(i,temp);
				
					
					doMore = true;  // after an exchange, must look again 
				}
			}
		}
	}

}
