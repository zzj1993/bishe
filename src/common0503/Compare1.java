package common0503;
/** ֻ���û�1��
 * MAE=0.9136606209206743
RMSE=1.322334112463504
hit=6.0
recall=0.043795620437956206
precision=0.3
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Compare1 {
	DBUtil db = new DBUtil();
	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	//�����н�(retainAll)����(addAll����/������)����(removeAll)������(containAll)
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Compare1 c = new Compare1();
		c.MAE_RMSE_recall_precision1(5);
	}
	
	//����ƽ���������;��������
	public void MAE_RMSE_recall_precision(int s) throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();//test like
		List<Integer> list2 = new ArrayList<Integer>();//test unlike
		List<Integer> list3 = new ArrayList<Integer>();//result recommend
		List<Integer> list4 = new ArrayList<Integer>();//result unrecommend
		List<Integer> result = new ArrayList<Integer>();
		conn = db.getConn();
		
		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid=? and score>=4");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));//test����userid<=5��movie����
		}	
		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid=? and score<=2");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list2.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		pst1 = conn.prepareStatement("SELECT movieid from result1 where userid=? and score>=4");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list3.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		pst1 = conn.prepareStatement("SELECT movieid from result1 where userid=? and score<=2");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list4.add(rs1.getInt(1));//test����userid<=5��movie����
		}
		//item-base
		System.out.println("*********Pearson*********");
//		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from result4 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
		pst2 = conn.prepareStatement("SELECT movieid from result4 where userid=? order by score desc limit 20");

//		System.out.println("*********Pearson*********");
//		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from u4 a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
//		pst2 = conn.prepareStatement("SELECT movieid from u4 where userid=? order by score desc limit 20");

		
		
		/*******����recall-precision******/
		//ȡǰ20��Ϊ�Ƽ���Ŀ		
		
//		for(int i=1;i<=5;i++){
			pst2.setInt(1, s);//�ҳ�5���û����Ե�ǰN���Ƽ�
			rs2 = pst2.executeQuery();
			while(rs2.next()){//���Ƽ���ǰ20*5��Ž���
				list2.add(rs2.getInt(1));
			}
//		}
		result.clear();
		result.addAll(list1);
		result.retainAll(list2);//����
		double hit = result.size();//hit����test1��user1��movie && �Ƽ���1��ǰ20movie
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
	//����ƽ���������;��������
	public void MAE_RMSE_recall_precision1(int s) throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		List<Integer> list3 = new ArrayList<Integer>();
		List<Integer> list4 = new ArrayList<Integer>();
		List<Integer> result1 = new ArrayList<Integer>();
		List<Integer> result2 = new ArrayList<Integer>();
		List<Integer> result3 = new ArrayList<Integer>();
		List<Integer> result4 = new ArrayList<Integer>();
		conn = db.getConn();
		//137����userid=1���Լ�����
		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid=? and score>=4");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));//�û�ϲ��
		}
		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid=? and score<=2");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list2.add(rs1.getInt(1));//�û���ϲ��
		}
		pst1 = conn.prepareStatement("SELECT movieid from result4 where userid=? order by score desc limit 20");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list3.add(rs1.getInt(1));//���Ƽ���Ʒ
		}
		pst1 = conn.prepareStatement("SELECT movieid from result4 where userid=? order by score desc limit 20,100000");// order by score desc limit 20
		pst1.setInt(1, s);
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list4.add(rs1.getInt(1));//δ���Ƽ���Ʒ
		}
		result1.clear();//list1,list3����
		result1.addAll(list1);
		result1.retainAll(list3);
		
		result2.clear();//list1,list3����
		result2.addAll(list1);
		result2.retainAll(list4);
		
		result3.clear();//list1,list3����
		result3.addAll(list2);
		result3.retainAll(list3);
		
		result4.clear();//list1,list3����
		result4.addAll(list2);
		result4.retainAll(list4);
		double hit = result1.size();//hit����test1��user1��movie && �Ƽ���1��ǰ20movie
		
		
		recall = hit/(float)(result1.size()+result3.size());
		precision = hit/(float
				
				
				)(result1.size()+result2.size());
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
