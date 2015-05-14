package svd0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 218594ms
MAE=1.3089617694508169
RMSE=1.5046775216019337
recall=0.3225806451612903
precision=1.0
224ms
*/
public class Compare {
	DBUtil db = new DBUtil();
	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Compare c = new Compare();
		c.MAE_RMSE_recall_precision();
	}

	public void MAE_RMSE_recall_precision() throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		List<Integer> result = new ArrayList<Integer>();
		conn = db.getConn();

		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid<=5 order by movieid");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));
		}
	
		pst2 = conn.prepareStatement("SELECT movieid from svd where userid=? order by score desc limit 20");
		for(int i=1;i<=5;i++){
			pst2.setInt(1, i);
			rs2 = pst2.executeQuery();
			while(rs2.next()){
				list2.add(rs2.getInt(1));
			}
		}
		

		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from svd a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			sum1 = rs1.getDouble(1);
			sum2 = rs1.getDouble(2);
			count = rs1.getInt(3);
		}
		mae = sum1/count;
		rmse = Math.sqrt(sum2/count);
		System.out.println("MAE="+mae);
		System.out.println("RMSE="+rmse);
		
		/*******recall-precision******/	
		result.clear();
		result.addAll(list2);
		result.retainAll(list1);
		double hit = result.size();
		recall = hit/list1.size();
		precision = hit/list2.size();
		System.out.println("recall="+recall);
		System.out.println("precision="+precision);
		if(rs1!=null){
			rs1.close();
		}
		if(rs2!=null){
			rs2.close();
		}
		if(pst1!=null){
			pst1.close();
		}
		if(pst2!=null){
			pst2.close();
		}
		if(conn!=null){
			conn.close();
		}
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
	}
}
