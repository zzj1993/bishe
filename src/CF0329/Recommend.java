package CF0329;

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

public class Recommend {
	//计算用户的平均评分
	public double getAverage(int prefer[][],int userID){		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
		for(int item=0;item<prefer[userID].length;item++){			
			if(prefer[userID][item]!=0)
			{
				sum += prefer[userID][item];
				count++;
			}
		}
		return (double)sum/count;
	}
	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(int prefer[][],int userID,int itemID,int k){
		double avgOther=0.0;
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int otherid;
		double similarity;
		
		UserCF uc = new UserCF();
		List<Map.Entry<Integer, Double>> userSim = uc.topKMatches(prefer,userID,itemID,k);
		Iterator<Map.Entry<Integer, Double>> it = userSim.iterator();
		Entry<Integer, Double> entry;
		while(it.hasNext()){
			entry = it.next();
			otherid =entry.getKey();
			similarity = entry.getValue();
			
			if(otherid!=userID){
				avgOther = getAverage(prefer,otherid);//待比较用户的平均分
				//累加
				simSums += Math.abs(similarity);
				weightAvg += (prefer[otherid][itemID]-avgOther)*similarity;
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
		int prefer1[][] = db.loadMovieLensTrain();
		int prefer2[][] = db.loadMovieLensTest();
		

		
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("insert into result(userid,movieid,score) values(?,?,?)");
		
		double rating = 0.0;
		for(int i=0;i<prefer2.length;i++){
			for(int j=0;j<prefer2[i].length;j++){
				if(prefer2[i][j]!=0){
					rating = getRating(prefer1,i,j,20);
//					pw.write(i+"\t"+j+"\t"+rating+"\n");
					pst.setInt(1, i);
					pst.setInt(2, j);
					pst.setDouble(3, rating);
					pst.executeUpdate();
//					System.out.println(i);
				}
			}
		}
		if(pst!=null){
			pst.close();
		}
//		pw.close();
	}
}
