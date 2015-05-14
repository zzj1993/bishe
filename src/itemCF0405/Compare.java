package itemCF0405;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//K=  5		10		20		40		80		160
//MAE=1.07	1.02	1.06	1.07	1.06	1.06//欧式距离
//MAE=1.1	1.1		1.07	1.09	1.07	1.07//余弦相似度
//MAE=0.91	1.04	1.02	1.0		1.01	0.99//修正余弦相似度
//MAE=1.1	1.09	1.12	1.02	1.02	1.03//pearson
//Tanimoto相似度（Jaccard系数）和Manhattan相似度，LogLikelihood（对数似然相似度）
//只适合评分只有0,1那种。Spearmon一般用于学术研究或小规模计算表
//用时逐渐增多，100条数据，4000ms+
public class Compare {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		long t1 = System.currentTimeMillis();

		double score1,score2;
		
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		int count=0;
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		PreparedStatement pst1 = conn.prepareStatement("SELECT score from test where userid=1 order by movieid");
		PreparedStatement pst2 = conn.prepareStatement("select score from result where movieid in(SELECT movieid FROM `data`.`test` where userid=1 order by movieid) order by movieid");
		ResultSet rs1 = pst1.executeQuery();
		ResultSet rs2 = pst2.executeQuery();
		while(rs1.next()&&rs2.next()){
			score1 = rs1.getInt(1);		
			score2 = rs2.getInt(1);	
			
			sum1 += Math.abs(score2-score1);
			sum2 += Math.pow(score1-score2, 2);
			count++;
		}
		mae = sum1/count;
		rmse = sum2/count;
		System.out.println("MAE="+mae);
		System.out.println("RMSE="+rmse);
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
