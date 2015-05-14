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
	
		if(simSums==0){//153个项目相似度为0
			return avgItem;
		}
		else{
//			System.out.println("simSums="+0);
			return (avgItem+weightAvg/simSums);
		}
			
	}
	
	/**
	 * @param userID 给userID的用户推荐物品
	 * @param k 相似用户数
	 * @param n 推荐物品个数
	 */
	public void getAllUserRating(int userID,int k,int n) throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Object prefer1[] = db.loadMovieLensTrain();//item-user
		Object prefer2[] = db.loadMovieLensTest();
		
		Connection conn = db.getConn();
		//清空表
//		PreparedStatement pst = conn.prepareStatement("truncate result");
//		pst.executeUpdate();
		//得到所有的movie，有1650项，中间有些序号缺
		String sql = "SELECT distinct movieid from base1 order by movieid asc";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs1 = pst.executeQuery();
		//得到userID评论过的movie
		sql = "select movieid from base1 where userid=?";		
		pst = conn.prepareStatement(sql);
		pst.setInt(1, userID);		
		ResultSet rs2 = pst.executeQuery();
		
		Set<Integer> s1 = new HashSet<Integer>();
		Set<Integer> s2 = new HashSet<Integer>();
		Set<Integer> result = new HashSet<Integer>();
		while(rs1.next()){
			s1.add(rs1.getInt(1));//所有项目，924个
		}
		while(rs2.next()){
			s2.add(rs2.getInt(1));
		}
		s1.removeAll(s2);
		
		double rating = 0.0;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\2.test"));
		//将预测结果插入
	
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
