package itemCF0407;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Recommend {
	//������Ŀ��ƽ������
	public double getAverage(Map<Integer,Map<Integer,Integer>> mm,int itemID){		
		double count=0.0,sum=0.0;
		
		//���ﲻ����for(int item:prefer[userID])
		Map<Integer,Integer> m = mm.get(itemID);
		for(Entry<Integer,Integer> entry:m.entrySet()){
			sum += entry.getValue();
		}
		count = m.size();
		
		if(count==0)
			return 0;
		else
			return (double)sum/count;
	}
	
	//ƽ����Ȩ���ԣ�Ԥ��userID��itemID������
	public double getRating(Map<Integer,Map<Integer,Integer>> mm,int itemID,int userID,int k){
		double avgOtherItem=0.0;//���û�����������Ŀ��ƽ������
		double simSums=0.0;
		double weightAvg=0.0;//��Ȩƽ��
		int itemid;
		double similarity;
		double score;
		
		ItemCF uc = new ItemCF();
		//��ȡ�����Ƶ�K����Ŀ
		List<Map.Entry<Integer, Double>> itemSim = uc.topKMatches(mm,itemID,userID,k);
		Iterator<Map.Entry<Integer, Double>> it = itemSim.iterator();
		Entry<Integer, Double> entry;
		

//		System.out.println(itemSim);//�������û���20���������û��������ֲ���6�������Իᱨ��ָ��
//		System.out.println(m);
		while(it.hasNext()){
			entry = it.next();
			itemid =entry.getKey();
			similarity = entry.getValue();
			//�õ��û�userID��itemid������
			Map<Integer,Integer> m = mm.get(itemid);
			score = m.get(userID);
		
			if(itemid!=itemID){
				avgOtherItem = getAverage(mm,itemid);
				//�ۼ�
				simSums += Math.abs(similarity);
				
				weightAvg += (score-avgOtherItem)*similarity;
			}
		}

		double avgItem = getAverage(mm,itemID);//����Ŀ�������û�����
		if(simSums==0)
			return avgItem;
		else
			return (avgItem+weightAvg/simSums);
			
	}
	
	/**
	 * @param userID ��userID���û��Ƽ���Ʒ
	 * @param k �����û���
	 * @param n �Ƽ���Ʒ����
	 */
	public void getAllUserRating(int userID,int k,int n) throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Map<Integer,Map<Integer,Integer>> mm1 = db.loadMovieLensTrain();//item-user
		Map<Integer,Map<Integer,Integer>> mm2 = db.loadMovieLensTest();
		
		Connection conn = db.getConn();
		//��ձ�
//		PreparedStatement pst = conn.prepareStatement("truncate result");
//		pst.executeUpdate();
		//�õ����е�movie����1650��м���Щ���ȱ
		String sql = "SELECT distinct movieid from base1 order by movieid asc";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs1 = pst.executeQuery();
		//�õ�userID���۹���movie
		sql = "select movieid from base1 where userid=?";		
		pst = conn.prepareStatement(sql);
		pst.setInt(1, userID);		
		ResultSet rs2 = pst.executeQuery();
		
		Set<Integer> s1 = new HashSet<Integer>();
		Set<Integer> s2 = new HashSet<Integer>();
		while(rs1.next()){
			s1.add(rs1.getInt(1));//������Ŀ��924��
		}
		while(rs2.next()){
			s2.add(rs2.getInt(1));
		}
		s1.removeAll(s2);
		
		double rating = 0.0;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		//��main������ֻ����һ����������Ļ�����Ҫ��գ�����Ļ��������
//		pst = conn.prepareStatement("truncate result1");
//		pst.executeUpdate();
		//��Ԥ��������		
		pst = conn.prepareStatement("insert into result1(userid,movieid,score) values(?,?,?)");
				
		for(int movieid:s1){
			rating = getRating(mm1,movieid,userID,5);
			pw.write(userID+"\t"+movieid+"\t"+rating+"\n");
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		if(pst!=null){
			pst.close();
		}
		pw.close();
	}
	
}
