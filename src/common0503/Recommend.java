package common0503;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Table;

public class Recommend {
	//平均加权策略，预测userID对itemID的评分,s是相似度度量方法
	public double getRating(Table<Integer, Integer, Integer> uiRating1,
			Table<Integer, Integer, Double> uiTime,
			Table<Integer, Integer, Integer> iuRating2,
			int itemID,int userID,int k,int s) throws ClassNotFoundException, SQLException{
		double avgOtherItem=0.0;//本用户所有其他项目的平均评分
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int itemid;
		double similarity;
		double score;
		double timestamp;//8开头
		double guiyi;
		int year;
		double weight=0.0;//时间权重
		
		uiCF uc = new uiCF();
		//获取最相似的K个项目
		List<Map.Entry<Integer, Double>> uiSim = uc.topKMatches(uiRating1,iuRating2,itemID,userID,k,s);
		Iterator<Map.Entry<Integer, Double>> it = uiSim.iterator();
		Entry<Integer, Double> entry;
		
		DBUtil db = new DBUtil();
		double a[] = db.Max_Min();
		Average avg = new Average();
		
		while(it.hasNext()){
			entry = it.next();
			itemid =entry.getKey();
			similarity = entry.getValue();//得到相似度
			timestamp = uiTime.get(itemid, userID);//得到时间戳
//			timestamp = Math.round(entry.getValue().get(1));//得到时间戳			

			//归一化处理时间
			guiyi = (timestamp-a[1])/(a[0]-a[1]);
			
			//得到用户userID对itemid的评分
			Map<Integer,Integer> m = uiRating1.row(itemid);
			score = m.get(userID);
			
			
			if(itemid!=itemID){
				avgOtherItem = avg.getAverage(uiRating1,itemid);
				weight = 1.0/(1+Math.exp(-guiyi));
//				weight = Math.exp(-0.1*(2015-year));//当前是2015年，时间参数a=0.05
//				weight = Math.pow(1-Math.abs(-score), 2);//当前评分减去要已经评过的
				//累加
				simSums += similarity*weight;				
				weightAvg += (score-avgOtherItem)*similarity*weight;
//				simSums += similarity;				
//				weightAvg += (score*weight-avgOtherItem)*similarity;
//				simSums += similarity;				
//				weightAvg += (score-avgOtherItem)*similarity;
			
			}
		}

		double avgItem = avg.getAverage(uiRating1,itemID);//本项目的所有用户评分
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
		Object a[] = db.loadMovieLensTrain();//item-user
		Table<Integer, Integer, Integer> itemUserRating1 = (Table<Integer, Integer, Integer>) a[0];
		Table<Integer, Integer, Double> itemUserTime1 = (Table<Integer, Integer, Double>) a[1];
		Table<Integer, Integer, Integer> userItemRating1 = (Table<Integer, Integer, Integer>) a[2];
		Table<Integer, Integer, Double> userItemTime1 = (Table<Integer, Integer, Double>) a[3];
		
		
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
		
		/******************item-base*********************/
		//将预测结果插入,Euclidean相似度		
		pst = conn.prepareStatement("insert into result1(userid,movieid,score) values(?,?,?)");				
		for(int movieid:s1){
			rating = getRating(itemUserRating1,itemUserTime1,userItemRating1,movieid,userID,5,1);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//余弦相似度
		pst = conn.prepareStatement("insert into result2(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(itemUserRating1,itemUserTime1,userItemRating1,movieid,userID,5,2);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//修正余弦相似度
		pst = conn.prepareStatement("insert into result3(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(itemUserRating1,itemUserTime1,userItemRating1,movieid,userID,5,3);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//pearson相似度
		pst = conn.prepareStatement("insert into result4(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(itemUserRating1,itemUserTime1,userItemRating1,movieid,userID,5,4);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		
		
		/******************user-base*********************/
		//将预测结果插入,Euclidean相似度		
		pst = conn.prepareStatement("insert into u1(userid,movieid,score) values(?,?,?)");				
		for(int movieid:s1){
			rating = getRating(userItemRating1,userItemTime1,itemUserRating1,userID,movieid,5,1);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//余弦相似度
		pst = conn.prepareStatement("insert into u2(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(userItemRating1,userItemTime1,itemUserRating1,userID,movieid,5,2);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//修正余弦相似度
		pst = conn.prepareStatement("insert into u3(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(userItemRating1,userItemTime1,itemUserRating1,userID,movieid,5,3);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		//pearson相似度
		pst = conn.prepareStatement("insert into u4(userid,movieid,score) values(?,?,?)");		
		for(int movieid:s1){
			rating = getRating(userItemRating1,userItemTime1,itemUserRating1,userID,movieid,5,4);
			
			pst.setInt(1, userID);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
	}
}
