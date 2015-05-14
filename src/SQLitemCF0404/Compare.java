package SQLitemCF0404;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//K=  5		10		20		40		80		160
//MAE=1.1	1.09	1.12	1.02	1.02	1.03
//用时逐渐增多，10条数据，50 000ms~150 000ms，事实证明，直接从数据库读取的方法是最慢的，二维数组次慢，一维数组最快
public class Compare {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		/*int prefer1[][] = db.loadMovieLensTest();
		int prefer2[][] = db.loadMovieLensResult();
		double sum;
		int n;
		
		for(int i=0;i<prefer1.length;i++){
			for(int j=0;j<prefer1[i].length;j++){
				
			}
		}*/

		double score1,score2;
		
		double sum=0.0;
		double mae;
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		PreparedStatement pst1 = conn.prepareStatement("select score from test where id<=10 order by userid asc,movieid asc");
		PreparedStatement pst2 = conn.prepareStatement("select score from result order by userid asc,movieid asc");
		ResultSet rs1 = pst1.executeQuery();
		ResultSet rs2 = pst2.executeQuery();
		while(rs1.next()&&rs2.next()){
			score1 = rs1.getInt(1);		
			score2 = rs2.getInt(1);	
			
			sum+=Math.abs(score2-score1);
		}
		mae = sum/10;
		System.out.println("MAE="+mae);
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
	}

}
