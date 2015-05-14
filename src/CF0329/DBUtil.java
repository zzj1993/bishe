package CF0329;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	public Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
	//—µ¡∑ºØ
	public int[][] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		int prefer[][]=new int[1000][2000];

		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from base");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
			prefer[userid][movieid] = score;
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
		
		return prefer;
	}
	//≤‚ ‘ºØ
	public int[][] loadMovieLensTest() throws ClassNotFoundException, SQLException{
		int prefer[][]=new int[1000][2000];
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from test where id<=1000");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
			
			prefer[userid][movieid] = score;
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
		return prefer;
	}
	
	public int[][] loadMovieLensResult() throws ClassNotFoundException, SQLException{
		int prefer[][]=new int[1000][2000];
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from result");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);			
			
			prefer[userid][movieid] = score;
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
		return prefer;
	}
	
}
