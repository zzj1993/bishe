package SQLitemCF0404;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Recommend {
	//������Ŀ��ƽ������
	public double getAverage(int itemID) throws SQLException, ClassNotFoundException{		
		double avg=0;
		
		String sql = "SELECT avg(score) FROM base where movieid=?";
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setInt(1, itemID);
		ResultSet rs = pst.executeQuery();
		//���userID��item
		while(rs.next()){
			avg = rs.getDouble(1);
		}
		return avg;
	}
	
	//ƽ����Ȩ���ԣ�Ԥ��userID��itemID������
	public double getRating(int itemID,int userID,int k) throws SQLException, ClassNotFoundException{
		double avgOtherItem=0.0;//���û�����������Ŀ��ƽ������
		double simSums=0.0;
		double weightAvg=0.0;//��Ȩƽ��
		int itemid;
		double similarity;
		
		ItemCF uc = new ItemCF();
		//��ȡ�����Ƶ�K����Ŀ
		Map<Integer,Double> map = uc.topKMatches(itemID,userID,k);
		
		String sql = "select score from base where userid=? and movieid=?";			
		DBUtil db = new DBUtil();
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs;
		for(Map.Entry<Integer, Double> entry:map.entrySet()){
			itemid = entry.getKey();
			similarity =entry.getValue();
			avgOtherItem = getAverage(itemid);//�õ�������Ŀ��ƽ����
			//���ƶ��ۼ�
			simSums += Math.abs(similarity);
			//�û���������Ŀ������-������Ŀ��ƽ����
			
			pst.setInt(1, userID);
			pst.setInt(2, itemid);
			rs = pst.executeQuery();
			while(rs.next()){
				weightAvg += (rs.getInt(1)-avgOtherItem)*similarity;
			}
			
		}		
		//����Ŀ�������û�����
		double avgItem = getAverage(itemID);
	
		if(simSums==0)
			return avgItem;
		else 
			return (avgItem+weightAvg/simSums);
	}
	
	public void getAllUserRating() throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();		
		Connection conn = db.getConn();
		
		String sql = "select movieid,userid from test where id<=10";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
	
		
		pst = conn.prepareStatement("truncate result");
		pst.executeUpdate();
		pst = conn.prepareStatement("insert into result(userid,movieid,score) values(?,?,?)");
		
		double rating = 0.0;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		
		while(rs.next()){
			//1-movieid,2-userid
			rating = getRating(rs.getInt(1),rs.getInt(2),5);
//			pw.write(rs.getInt(2)+"\t"+rs.getInt(1)+"\t"+rating+"\n");
			pst.setInt(1, rs.getInt(2));
			pst.setInt(2, rs.getInt(1));//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		if(pst!=null){
			pst.close();
		}
		if(rs!=null){
			rs.close();
		}
		pw.close();
	}
	
}
