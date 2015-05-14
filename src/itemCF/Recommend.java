package itemCF;

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
	//计算项目的平均评分
	public double getAverage(int itemPrefer[][],int itemID){		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
		for(int user=0;user<itemPrefer[itemID].length;user++){			
			if(itemPrefer[itemID][user]!=0)
			{
				sum += itemPrefer[itemID][user];
				count++;
			}
		}
		if(count==0)
			return 0;
		else
			return (double)sum/count;
	}
	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(int itemPrefer[][],int itemID,int userID,int k){
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
		while(it.hasNext()){
			entry = it.next();
			itemid =entry.getKey();
			similarity = entry.getValue();
			
			if(itemid!=itemID){
				avgOtherItem = getAverage(itemPrefer,itemid);
				//累加
				simSums += Math.abs(similarity);
				weightAvg += (itemPrefer[itemid][userID]-avgOtherItem)*similarity;
//				System.out.println(itemPrefer[itemid][userID]+"--");
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
		int prefer1[][] = db.loadMovieLensTrain();//item-user
		int prefer2[][] = db.loadMovieLensTest();
		
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("insert into result(userid,movieid,score) values(?,?,?)");
		
		double rating = 0.0;
		for(int i=0;i<prefer2.length;i++){//i item
			for(int j=0;j<prefer2[i].length;j++){//j user
				if(prefer2[i][j]!=0){
					rating = getRating(prefer1,i,j,20);
					pst.setInt(1, j);
					pst.setInt(2, i);
					pst.setDouble(3, rating);
					pst.executeUpdate();
				}
			}
		}
		if(pst!=null){
			pst.close();
		}
	}
	
	//一二维长度调换
/*	public int[][] transformPrefers(int prefer[][]){
		int m = prefer.length;
		int n = prefer[0].length;
		int itemPrefer[][] = new int[n][m];
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				itemPrefer[j][i] = prefer[i][j];
			}
		}
		return itemPrefer;
	}*/
}
