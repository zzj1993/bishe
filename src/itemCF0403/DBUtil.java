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
	//训练集
	public Object[] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		Object itemPrefer[] = new Object[2000];//2000左右的物品
		//user-ratings
		Map<Integer,Integer> map = new TreeMap<Integer,Integer>();
		
		Connection conn = getConn();
		//这里要按movieid排序，不然下面赋值到数组时会出错，因为if和else的map是承接的
		PreparedStatement pst = conn.prepareStatement("select * from base order by movieid asc,userid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);

			if(itemPrefer[movieid]==null){
				map = new TreeMap<Integer,Integer>();//不能用clear，不然后面的数组元素存进去的map会被覆盖
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
	//测试集
	public Object[] loadMovieLensTest() throws ClassNotFoundException, SQLException{
		Object itemPrefer[] = new Object[2000];//2000左右的物品
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
				map = new TreeMap<Integer,Integer>();//不能用clear，不然后面的数组元素存进去的map会被覆盖
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
