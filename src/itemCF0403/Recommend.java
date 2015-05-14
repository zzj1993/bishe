package itemCF0403;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Recommend {
	//计算项目的平均评分
	public double getAverage(Object itemPrefer[],int itemID){		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
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
	
	

	//平均加权策略，预测userID对itemID的评分
	public double getRating(Object itemPrefer[],int itemID,int userID,int k){
		double avgOtherItem=0.0;//本用户所有其他项目的平均评分
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int itemid;
		double similarity;
		
		ItemCF uc = new ItemCF();
		//获取最相似的K个项目
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
				//累加
				simSums += Math.abs(similarity);
				m = (Map)itemPrefer[itemid];
				weightAvg += (m.get(userID)-avgOtherItem)*similarity;
			}
		}
		
		//本项目的所有用户评分
		double avgItem = getAverage(itemPrefer,itemID);
	
		if(simSums==0)
			return avgItem;
		else 
			return (avgItem+weightAvg/simSums);
	}
	
	public void getAllUserRating() throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Object prefer1[] = db.loadMovieLensTrain();//item-user
		Object prefer2[] = db.loadMovieLensTest();
		
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("truncate result");
		pst.executeUpdate();
		pst = conn.prepareStatement("insert into result(userid,movieid,score) values(?,?,?)");
		
		double rating = 0.0;
		int userid,itemid;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		
		for(int i=0;i<prefer2.length;i++){	
			if(prefer2[i]!=null){//可能有些用户没有
				Map<Integer,Integer> m = (Map)prefer2[i];
				Iterator<Entry<Integer,Integer>> it = m.entrySet().iterator();
				while(it.hasNext()){
					Entry<Integer, Integer> entry = it.next();
					userid = entry.getKey();
					rating = getRating(prefer1,userid,i,20);
					pw.write(userid+"\t"+i+"\t"+rating+"\n");
					
					pst.setInt(1, userid);
					pst.setInt(2, i);//itemid=i
					pst.setDouble(3, rating);
					pst.executeUpdate();
				}//end while
			}//end if			
		}//end for
		if(pst!=null){
			pst.close();
		}
		pw.close();
	}
	
}
