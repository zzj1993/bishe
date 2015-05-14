package userCF0503;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Compare {
	DBUtil db = new DBUtil();
	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Compare c = new Compare();
//		for(int i=1;i<=4;i++){
			c.MAE_RMSE_recall_precision();
//		}	
	}
	
	//����ƽ���������;��������
	public void MAE_RMSE_recall_precision() throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();//test1 like
		List<Integer> list2 = new ArrayList<Integer>();//test1 unlike
		List<Integer> list3 = new ArrayList<Integer>();//result rec
		List<Integer> list4 = new ArrayList<Integer>();//result unrec
		List<Integer> result = new ArrayList<Integer>();
		conn = db.getConn();
		pst1 = conn.prepareStatement("SELECT movieid from test where score>=4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));//test����userid<=5��movie����
		}		
		pst1 = conn.prepareStatement("SELECT movieid from test where score<4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list2.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		pst1 = conn.prepareStatement("SELECT movieid from u1 where score>=4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list3.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		pst1 = conn.prepareStatement("SELECT movieid from u1 where score<4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list4.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		
		/*****����MAE-RMSE******/
/*		switch(s){
			case 1:
				System.out.println("*********Euclidean*********");
				pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from result1 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
				pst2 = conn.prepareStatement("SELECT movieid from result1 where userid=? order by score desc limit 20");
				break;
			case 2:
				System.out.println("*********Cosine*********");
				pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from result2 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
				pst2 = conn.prepareStatement("SELECT movieid from result2 where userid=? order by score desc limit 20");
				break;
			case 3:
				System.out.println("*********Modicos*********");
				pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from result3 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
				pst2 = conn.prepareStatement("SELECT movieid from result3 where userid=? order by score desc limit 20");
				break;
			case 4:
				System.out.println("*********Pearson*********");
				pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from result4 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
				pst2 = conn.prepareStatement("SELECT movieid from result4 where userid=? order by score desc limit 20");
				break;
		}
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			sum1 = rs1.getDouble(1);
			sum2 = rs1.getDouble(2);
			count = rs1.getInt(3);
		}*/
		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from u1 a,test b where a.movieid=b.movieid and a.userid=b.userid");
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
		
		/*******����recall-precision******/
		//ȡǰ20��Ϊ�Ƽ���Ŀ		
		
//		for(int i=1;i<=5;i++){
//			pst2.setInt(1, i);//�ҳ�5���û����Ե�ǰN���Ƽ�
//			rs2 = pst2.executeQuery();
//			while(rs2.next()){//���Ƽ���ǰ20*5��Ž���
//				list2.add(rs2.getInt(1));
//			}
//		}
		
		result.clear();
		result.addAll(list1);
		result.retainAll(list3);//ϲ��&���Ƽ�
		double hit = result.size();//hit����test1��user1��movie && �Ƽ���1��ǰ20movie
		result.clear();
		result.addAll(list2);
		result.retainAll(list3);//��ϲ��&���Ƽ�
		precision = hit/(result.size()+hit);//(ϲ��&���Ƽ�)/(ϲ��&���Ƽ�+��ϲ��&���Ƽ�)
				
		result.clear();
		result.addAll(list1);
		result.retainAll(list4);//ϲ��&δ���Ƽ�
		recall = hit/(result.size()+hit);//(ϲ��&���Ƽ�)/(ϲ��&���Ƽ�+ϲ��&�����Ƽ�)
		
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
