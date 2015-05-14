package itemCF0403;

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

public class DBUtil {
	public Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
	//ѵ����
	public Object[] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		Object itemPrefer[] = new Object[2000];//2000���ҵ���Ʒ
		//user-ratings
		Map<Integer,Integer> map = new TreeMap<Integer,Integer>();
		
		Connection conn = getConn();
		//����Ҫ��movieid���򣬲�Ȼ���渳ֵ������ʱ�������Ϊif��else��map�ǳнӵ�
		PreparedStatement pst = conn.prepareStatement("select * from base order by movieid asc,userid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);

			if(itemPrefer[movieid]==null){
				map = new TreeMap<Integer,Integer>();//������clear����Ȼ���������Ԫ�ش��ȥ��map�ᱻ����
				map.put(userid, score);
				itemPrefer[movieid]=map;
			}else{
				map.put(userid, score);
				itemPrefer[movieid]=map;
			}
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
		
		return itemPrefer;
	}
	//���Լ�
	public Object[] loadMovieLensTest() throws ClassNotFoundException, SQLException{
		Object itemPrefer[] = new Object[2000];//2000���ҵ���Ʒ
		//user-ratings
		Map<Integer,Integer> map = new TreeMap<Integer,Integer>();
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from test where userid=1 order by movieid asc,userid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
			if(itemPrefer[movieid]==null){
				map = new TreeMap<Integer,Integer>();//������clear����Ȼ���������Ԫ�ش��ȥ��map�ᱻ����
				map.put(userid, score);
				itemPrefer[movieid]=map;
			}else{
				map.put(userid, score);
				itemPrefer[movieid]=map;
			}
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
		return itemPrefer;
	}
	
}
