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
	//计算项目的平均评分
	public double getAverage(Map<Integer,Map<Integer,Integer>> mm,int itemID){		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
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
	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(Map<Integer,Map<Integer,Integer>> mm,int itemID,int userID,int k){
		double avgOtherItem=0.0;//本用户所有其他项目的平均评分
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int itemid;
		double similarity;
		double score;
		
		ItemCF uc = new ItemCF();
		//获取最相似的K个项目
		List<Map.Entry<Integer, Double>> itemSim = uc.topKMatches(mm,itemID,userID,k);
		Iterator<Map.Entry<Integer, Double>> it = itemSim.iterator();
		Entry<Integer, Double> entry;
		

//		System.out.println(itemSim);//最相似用户有20个，可是用户本身评分才有6个，所以会报空指针
//		System.out.println(m);
		while(it.hasNext()){
			entry = it.next();
			itemid =entry.getKey();
			similarity = entry.getValue();
			//得到用户userID对itemid的评分
			Map<Integer,Integer> m = mm.get(itemid);
			score = m.get(userID);
		
			if(itemid!=itemID){
				avgOtherItem = getAverage(mm,itemid);
				//累加
				simSums += Math.abs(similarity);
				
				weightAvg += (score-avgOtherItem)*similarity;
			}
		}

		double avgItem = getAverage(mm,itemID);//本项目的所有用户评分
		if(simSums==0)
			return avgItem;
		else
			return (avgItem+weightAvg/simSums);
			
	}
	
	/**
	 * @param userID 给userID的用户推荐物品
	 * @param k 相似用户数
	 * @param n 推荐物品个数
	 */
	public void getAllUserRating(int userID,int k,int n) throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Map<Integer,Map<Integer,Integer>> mm1 = db.loadMovieLensTrain();//item-user
		Map<Integer,Map<Integer,Integer>> mm2 = db.loadMovieLensTest();
		
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
		while(rs1.next()){
			s1.add(rs1.getInt(1));//所有项目，924个
		}
		while(rs2.next()){
			s2.add(rs2.getInt(1));
		}
		s1.removeAll(s2);
		
		double rating = 0.0;
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		//当main函数里只调用一次这个函数的话就需要清空，多个的话不用清空
//		pst = conn.prepareStatement("truncate result1");
//		pst.executeUpdate();
		//将预测结果插入		
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
