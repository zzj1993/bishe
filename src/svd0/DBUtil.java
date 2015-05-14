package svd0;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class DBUtil {
	Table<Integer,Integer,Double> records;
	Iterator<Integer> iter;
	int maxUser;
	int maxItem;
	double minPref;
	double maxPref;
	public static Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
	
	public void getSomething() throws ClassNotFoundException, SQLException{
		Connection conn=getConn();
		PreparedStatement pst = conn.prepareStatement("select count(distinct(userid)),count(distinct(movieid)),min(score),max(score) FROM base1");
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			maxUser = rs.getInt(1);
			maxItem = rs.getInt(2);
			minPref = rs.getDouble(3);
			maxPref = rs.getDouble(4);
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
	}
	
	public  double getMinPref()
	{
		return minPref;
	}
	
	public  double getMaxPref()
	{
		return maxPref;
	}
	
	
	public int getUserNum()
	{
		return maxUser;
	}
	
	public int getItemNum()
	{
		return maxItem;
	}
	
	public boolean hasNext()
	{
		return iter.hasNext();
	}
	
	public int getNext()
	{
		return iter.next();
	}
	
	public void reset()
	{
		iter = records.rowKeySet().iterator();
	}
	public Table<Integer,Integer,Double> getRecords() throws ClassNotFoundException, SQLException, IOException{
		Connection conn = getConn();
		//这里要按movieid排序，不然下面赋值到数组时会出错，因为if和else的map是承接的
		PreparedStatement pst = conn.prepareStatement("select * from base1 order by movieid asc,userid asc ");
		ResultSet rs = pst.executeQuery();
		int userid,movieid;
		double score;
		
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getDouble(4);
			
			records.put(userid, movieid, score);
		}
		iter = records.rowKeySet().iterator();
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
		return records;
	}
}
