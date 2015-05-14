package SQLitemCF0404;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ItemCF {
	//������Ŀ֮������ƶ�
	public double sim_item_pearson(int item1,int item2) throws ClassNotFoundException, SQLException{
		int score_a,score_b,count=0;
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		double sum1=0,sum2=0;
		//��ƽ��֮��
		double sum1Sq=0,sum2Sq=0;
		//��˻�֮�� ��XiYi
		double sumMulti = 0;
		double num1=0.0;
		double num2=0.0;
		double result=0.0;
		
		String sql = "SELECT a.score,b.score FROM base a,base b where a.movieid=? and b.movieid=? and a.userid=b.userid";
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, item1);
		pst.setInt(2, item2);
		ResultSet rs = pst.executeQuery();
		//����Ŀ֮���������ͨ�����㹲ͬ�û������ֲ���ã����û�֮���������ͨ�����㹲ͬ��Ŀ�����ֲ����
		while(rs.next()){			
			score_a = rs.getInt(1);
			score_b = rs.getInt(2);
			
			sum1 += score_a;//������������֮��
			sum2 += score_b;
			
			sum1Sq += Math.pow(score_a, 2);//��ƽ��֮��
			sum2Sq += Math.pow(score_b, 2);
			
			sumMulti += score_a*score_b;
			count++;
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(count==0)                                                
			return 0; 
		num1 = sumMulti - (sum1*sum2/count);
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/count)*(sum2Sq-Math.pow(sum2,2)/count));  
		if(num2==0)                                                
			return 0;  

		result = num1/num2;
		return result;
	}
	
	//��ȡ��item�����Ƶ�K����Ŀ
	public Map<Integer,Double> topKMatches(int itemID,int userID,int k) throws SQLException, ClassNotFoundException{
		//�ҳ�userID���۹�������item
		int movieid;
		double sim;//��sim���������ݿ��У�Ȼ������ȡǰK��
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		
		String sql = "SELECT movieid FROM base where userid=?";	
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, userID);
		ResultSet rs = pst.executeQuery();
		
		sql = "truncate item_sim";
		pst = conn.prepareStatement(sql);
		pst.executeUpdate();
		sql = "insert into item_sim(movieid1,movieid2,sim) values(?,?,?)";
		pst = conn.prepareStatement(sql);
		//���userID��item
		while(rs.next()){
			movieid = rs.getInt(1);
			if(movieid!=itemID){
				sim = sim_item_pearson(itemID,movieid);
				pst.setInt(1, itemID);
				pst.setInt(2, movieid);
				pst.setDouble(3, sim);
				pst.executeUpdate();
			}			
		}
		
		//ѡȡ�����Ƶ�K����Ŀ��itemID=movieid1
		sql = "select movieid2,sim from item_sim order by sim desc limit ?";
		pst = conn.prepareStatement(sql);
		pst.setInt(1, k);
		rs = pst.executeQuery();
		//LinkedListHashMap���ǰ�����˳���ŵ�
		Map<Integer,Double> map = new LinkedHashMap<Integer,Double>();
		while(rs.next()){
			map.put(rs.getInt(1), rs.getDouble(2));
		}
		if(pst!=null){
			pst.close();
		}
		if(rs!=null){
			rs.close();
		}

		return map;
	}
}
