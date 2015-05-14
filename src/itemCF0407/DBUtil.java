package itemCF0407;

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
/**
 * 100用户，1000电影
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
	//训练集
	public Map<Integer,Map<Integer,Integer>> loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		Map<Integer,Map<Integer,Integer>> mm = new TreeMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Integer> m = new TreeMap<Integer,Integer>();
		
		Connection conn = getConn();
		//这里要按movieid排序，不然下面赋值到数组时会出错，因为if和else的map是承接的
		PreparedStatement pst = conn.prepareStatement("select * from base1 order by movieid asc,userid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);

			if(mm.get(movieid)==null){
				m = new TreeMap<Integer,Integer>();
				m.put(userid, score);
				mm.put(movieid, m);
			}else{
				m.put(userid, score);
				mm.put(movieid, m);
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
		
		return mm;
	}
	//测试集
	public Map<Integer,Map<Integer,Integer>> loadMovieLensTest() throws ClassNotFoundException, SQLException{
		Map<Integer,Map<Integer,Integer>> mm = new TreeMap<Integer,Map<Integer,Integer>>();
		//user-ratings
		Map<Integer,Integer> m = new TreeMap<Integer,Integer>();
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from test1 order by movieid asc,userid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
			if(mm.get(movieid)==null){
				m = new TreeMap<Integer,Integer>();
				m.put(userid, score);
				mm.put(movieid, m);
			}else{
				m.put(userid, score);
				mm.put(movieid, m);
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
		return mm;
	}
	
}
