package itemCF0503;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
/**
 * 100�û���1000��Ӱ
 * @author junzai
 *
 */
public class DBUtil {
	public Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
	//ѵ������rating��time��������������
	public Object[] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		Table<Integer, Integer, Integer> itemUserRating = TreeBasedTable.create();
		Table<Integer, Integer, Double> itemUserTime = TreeBasedTable.create();
		
		Table<Integer, Integer, Integer> userItemRating = TreeBasedTable.create();
		Table<Integer, Integer, Double> userItemTime = TreeBasedTable.create();
		Object a[] = new Object[4];
		
		Connection conn = getConn();
		//����Ҫ��movieid���򣬲�Ȼ���渳ֵ������ʱ���������Ϊif��else��map�ǳнӵ�
		PreparedStatement pst = conn.prepareStatement("select * from base order by movieid asc,userid asc ");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score;
		double timestamp;
		
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getDouble(5);
			
			itemUserRating.put(movieid,userid, score);
			itemUserTime.put(movieid,userid, timestamp);
		}
		a[0] = itemUserRating;
		a[1] = itemUserTime;
		
		pst = conn.prepareStatement("select * from base order by userid asc,movieid asc");
		rs = pst.executeQuery();
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getDouble(5);
			
			userItemRating.put(userid,movieid, score);
			userItemTime.put(userid,movieid, timestamp);
		}
		a[2] = userItemRating;
		a[3] = userItemTime;
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
	
	//ʱ���һ���������ҵ�������С��ʱ��
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