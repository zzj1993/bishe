package dataSQL;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class importData {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
/*		BufferedReader br = new BufferedReader(new FileReader("..\\ratings_set2.dat"));
		String s;
		String[] ss;
		String sql = "insert into test(userid,movieid,score) values(?,?,?)";
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement(sql);
		while((s=br.readLine())!=null){
			ss = s.split(",");
			pst.setInt(1, Integer.parseInt(ss[0]));
			pst.setInt(2, Integer.parseInt(ss[1]));
			pst.setDouble(3, Double.parseDouble(ss[2]));
			pst.executeUpdate();
		}		
		if(pst!=null){
			pst.close();
		}
		if(br!=null){
			br.close();
		}*/

	}
	public static Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data1", "root", "123456");
		return conn;
	}

}
