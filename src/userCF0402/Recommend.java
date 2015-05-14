package userCF0402;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Recommend {
	//计算用户的平均评分
	public double getAverage(Object prefer[],int userID){		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
		Map<Integer,Integer> m = (Map) prefer[userID];
		Iterator<Integer> it = m.keySet().iterator();
		while(it.hasNext()){
			sum += m.get(it.next());
		}
		count = m.size();
		return (double)sum/count;
	}
	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(Object prefer[],int userID,int itemID,int k){
		double avgOther=0.0;
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int otherid;
		double similarity;
		
		UserCF uc = new UserCF();
		List<Map.Entry<Integer, Double>> userSim = uc.topKMatches(prefer,userID,itemID,k);
		Iterator<Map.Entry<Integer, Double>> it = userSim.iterator();
		Entry<Integer, Double> entry;
		Map<Integer,Integer> m;
		while(it.hasNext()){
			entry = it.next();
			otherid =entry.getKey();
			similarity = entry.getValue();
			
			
			
			if(otherid!=userID){
				avgOther = getAverage(prefer,otherid);//待比较用户的平均分
				//累加
				simSums += Math.abs(similarity);
				
				m = (Map)prefer[otherid];
				weightAvg += (m.get(itemID)-avgOther)*similarity;
			}
		}
		
		double avgUser = getAverage(prefer,userID);
	
		if(simSums==0)
			return avgUser;
		else 
			return (avgUser+weightAvg/simSums);
	}
	
	public void getAllUserRating() throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Object prefer1[] = db.loadMovieLensTrain();
		Object prefer2[] = db.loadMovieLensTest();
		
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("truncate result");
		pst.executeUpdate();
		pst = conn.prepareStatement("insert into result(userid,movieid,score) values(?,?,?)");
		double rating = 0.0;
		
//		Iterator<Entry<Integer,Integer>> it;
//		Entry<Integer, Integer> entry;
		int userid,itemid;
//		m = (Map)prefer2[1];
//		System.out.println(m.entrySet());
		for(int i=0;i<prefer2.length;i++){	
			if(prefer2[i]!=null){//可能有些用户没有
				Map<Integer,Integer> m = (Map)prefer2[i];
				Iterator<Entry<Integer,Integer>> it = m.entrySet().iterator();
				while(it.hasNext()){
					Entry<Integer, Integer> entry = it.next();
					itemid = entry.getKey();
					rating = getRating(prefer1,i,itemid,160);
					pw.write(i+"\t"+itemid+"\t"+rating+"\n");
					
					pst.setInt(1, i);//userid=i
					pst.setInt(2, itemid);
					pst.setDouble(3, rating);
					pst.executeUpdate();
				}
			}
		}
		if(pst!=null){
			pst.close();
		}
		pw.close();
	}
}
