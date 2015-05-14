package itemCF0406;

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
	public double getAverage(Object itemPrefer[],int itemID){		
		double count=0.0,sum=0.0;
		
		//���ﲻ����for(int item:prefer[userID])
		Map<Integer,Integer> m = (Map) itemPrefer[itemID];
		Iterator<Integer> it = m.keySet().iterator();
		while(it.hasNext()){
			sum += m.get(it.next());
		}
		count = m.size();
		
		if(count==0)
			return 0;
		else
			return (double)sum/count;
	}
	
	//ƽ����Ȩ���ԣ�Ԥ��userID��itemID������
	public double getRating(Object itemPrefer[],int itemID,int userID,int k){
		double avgOtherItem=0.0;//���û�����������Ŀ��ƽ������
		double simSums=0.0;
		double weightAvg=0.0;//��Ȩƽ��
		int itemid;
		double similarity;
		
		ItemCF uc = new ItemCF();
		//��ȡ�����Ƶ�K����Ŀ
		List<Map.Entry<Integer, Double>> itemSim = uc.topKMatches(itemPrefer,itemID,userID,k);
		Iterator<Map.Entry<Integer, Double>> it = itemSim.iterator();
		Entry<Integer, Double> entry;
		Map<Integer,Integer> m;
		while(it.hasNext()){
			entry = it.next();
			itemid =entry.getKey();
			similarity = entry.getValue();
			
			if(itemid!=itemID){
				avgOtherItem = getAverage(itemPrefer,itemid);
				//�ۼ�
				simSums += Math.abs(similarity);
				m = (Map)itemPrefer[itemid];
				weightAvg += (m.get(userID)-avgOtherItem)*similarity;
			}
		}
		
		//����Ŀ�������û�����
		double avgItem = getAverage(itemPrefer,itemID);
	
		if(simSums==0){//153����Ŀ���ƶ�Ϊ0
			return avgItem;
		}
		else{
//			System.out.println("simSums="+0);
			return (avgItem+weightAvg/simSums);
		}
			
	}
	
	/**
	 * @param userID ��userID���û��Ƽ���Ʒ
	 * @param k �����û���
	 * @param n �Ƽ���Ʒ����
	 */
	public void getAllUserRating(int userID,int k,int n) throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Object prefer1[] = db.loadMovieLensTrain();//item-user
		Object prefer2[] = db.loadMovieLensTest();
		
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
		Set<Integer> result = new HashSet<Integer>();
		while(rs1.next()){
			s1.add(rs1.getInt(1));//������Ŀ��924��
		}
		while(rs2.next()){
			s2.add(rs2.getInt(1));
		}
		s1.removeAll(s2);
		
		double rating = 0.0;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\2.test"));
		//��Ԥ��������
	
		pst = conn.prepareStatement("insert into result1(userid,movieid,score) values(?,?,?)");
				
		for(int movieid:s1){
			rating = getRating(prefer1,movieid,userID,5);
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
