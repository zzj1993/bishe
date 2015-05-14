package userCF0503;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class DBUtil {
	public Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
	//训练集
	public Object[] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		Table<Integer, Integer, Integer> userItemRating = TreeBasedTable.create();
		Table<Integer, Integer, Double> userItemTime = TreeBasedTable.create();
		
		Table<Integer, Integer, Integer> itemUserRating = TreeBasedTable.create();
		Table<Integer, Integer, Double> itemUserTime = TreeBasedTable.create();
		Object a[] = new Object[4];
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from base order by userid asc,movieid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score;
		double timestamp;
		
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getDouble(5);
			
			userItemRating.put(userid,movieid, score);
			userItemTime.put(userid,movieid, timestamp);
			
		}
		a[0] = userItemRating;
		a[1] = userItemTime;
		
		pst = conn.prepareStatement("select * from base order by movieid asc,userid asc");
		rs = pst.executeQuery();
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getDouble(5);
			
			itemUserRating.put(movieid,userid, score);
			itemUserTime.put(movieid,userid, timestamp);
		}
		a[2] = itemUserRating;
		a[3] = itemUserTime;
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}		
		return a;
	}
	
	//时间归一化处理，找到最大和最小的时间
	public double[] Max_Min() throws ClassNotFoundException, SQLException{
		double a[] = new double[2];
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select max(timestamp),min(timestamp) from base");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			a[0] = rs.getDouble(1);
			a[1] = rs.getDouble(2);
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
		return a;
	}
	
}
