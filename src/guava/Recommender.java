package guava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class Recommender {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Table<Integer, Integer, Double> userItemRanks = TreeBasedTable.create();
		String sql = "select userid,movieid,score from base1 where movieid=2";
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while(rs.next()){
			userItemRanks.put(rs.getInt(1), rs.getInt(2), rs.getDouble(3));
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
		for(int i:userItemRanks.rowKeySet()){
			for(int j:userItemRanks.rowKeySet()){
				if(j<i)
					System.out.print(j+"\t");
			}
			System.out.println();
		}

	}
	
	public static Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");
		return conn;
	}
}
