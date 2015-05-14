package userCF0503;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;

public class Recommend {	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(
			Table<Integer, Integer, Integer> userItemRating,
			Table<Integer, Integer, Double> userItemTime,
			Table<Integer, Integer, Integer> itemUserRating,
			int userID,int itemID,int k,int s) throws ClassNotFoundException, SQLException{
		double avgOther=0.0;
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int otherid;
		double similarity;
		double timestamp;//8开头
		double guiyi;
		int score;
		double weight=0.0;//时间权重
		
		UserCF uc = new UserCF();
		List<Map.Entry<Integer, Double>> userSim = uc.topKMatches(userItemRating,itemUserRating,userID,itemID,k,s);
		Iterator<Map.Entry<Integer, Double>> it = userSim.iterator();
		Entry<Integer, Double> entry;
		DBUtil db = new DBUtil();
		double a[] = db.Max_Min();
		userAverage avg = new userAverage();
		
		while(it.hasNext()){
			entry = it.next();
			otherid =entry.getKey();
			similarity = entry.getValue();
			timestamp = userItemTime.get(otherid, itemID);//得到时间戳，注意是otherid不是userID
			//归一化处理时间
			guiyi = (timestamp-a[1])/(a[0]-a[1]);
			
			Map<Integer,Integer> m = userItemRating.row(otherid);
			score = m.get(itemID);
			
			if(otherid!=userID){
				avgOther = avg.getAverage(userItemRating,otherid);//待比较用户的平均分
				weight = 1.0/(1+Math.exp(-guiyi));
				
				simSums += similarity*weight;				
				weightAvg += (score-avgOther)*similarity*weight;
			}
		}
		
		double avgUser = avg.getAverage(userItemRating,userID);
	
		if(simSums==0)
			return avgUser;
		else 
			return (avgUser+weightAvg/simSums);
	}
	
	public void getAllUserRating() throws ClassNotFoundException, SQLException, IOException{
		DBUtil db = new DBUtil();
		Object a[] = db.loadMovieLensTrain();
		Table<Integer, Integer, Integer> userItemRating1 = (Table<Integer, Integer, Integer>) a[0];
		Table<Integer, Integer, Double> userItemTime1 = (Table<Integer, Integer, Double>) a[1];
		Table<Integer, Integer, Integer> itemUserRating1 = (Table<Integer, Integer, Integer>) a[2];
		Table<Integer, Integer, Double> itemUserTime1 = (Table<Integer, Integer, Double>) a[3];
		
		Connection conn = db.getConn();
		String sql = "SELECT userid,movieid from test";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		int userid,movieid;
		double rating;
		pst = conn.prepareStatement("insert into u1(userid,movieid,score) values(?,?,?)");
		while(rs.next()){
			userid = rs.getInt(1);
			movieid = rs.getInt(2);//4是pearson相似度
			rating = getRating(userItemRating1,userItemTime1,itemUserRating1,userid,movieid,5,4);
			
			pst.setInt(1, userid);
			pst.setInt(2, movieid);//itemid=i
			pst.setDouble(3, rating);
			pst.executeUpdate();
		}
		if(rs!=null){
			rs.close();
		}

		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
	}
}
