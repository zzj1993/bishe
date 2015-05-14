package userCF0503;

import itemCF0503.itemAverage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Sets.SetView;

public class UserCF {
	//1、Euclidean相似度
	public double sim_user_Euclidean(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//找到共同评分用户	
		
		if(comItem.size()==0)//相同评价用户个数
			return -1;
		
		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2,count=0;
		double distance=0.0;
		double result=0.0;
		//通过计算两用户的共同评分项目计算相似度
		for(int item:comItem){			
			score1 = itemRating1.get(item);//所有所用评分之和
			score2 = itemRating2.get(item);
			distance += Math.pow(score1-score2, 2);
			count++;
		}
		result = 1.0/(1+Math.sqrt(distance)/Math.sqrt(count));
		return result;
	}
	
	//2、余弦相似度
	public double sim_user_cosine(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//找到共同评分用户	
		
		if(comItem.size()==0)//相同评价用户个数
			return -1;
		
		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		//通过计算两用户的共同评分项目计算相似度
		for(int item:comItem){			
			score1 = itemRating1.get(item);//所有所用评分之和
			score2 = itemRating1.get(item);
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		result = sum12/(Math.sqrt(sum_score12)*Math.sqrt(sum_score22));
		return result;
	}
	
	//3、修正的余弦相似度
	public double sim_user_modicos(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//找到共同评分用户	
		
		if(comItem.size()==0)//相同评价用户个数
			return -1;
		
		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2,sum_score1=0,sum_score2=0;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		
		itemAverage avg = new itemAverage();
		double avgItem1 = avg.getAverage(userItemRating, user1);
		double avgItem2 = avg.getAverage(userItemRating, user2);;
		//通过计算两用户的共同评分项目计算相似度
		for(int item:comItem){			
			score1 = itemRating1.get(item);//所有所用评分之和
			score2 = itemRating2.get(item);
			sum_score1 += score1;
			sum_score2 += score2;
			
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		double fenzi = sum12-avgItem2*sum_score1;//注意坟墓不能为0，附属不能开平方
		double fenmu = Math.sqrt(Math.abs(sum_score12-avgItem1*sum_score1))*Math.sqrt(Math.abs(sum_score22-avgItem2*sum_score2));
		if(fenmu==0)
			return 0;
		else{
			result = fenzi/fenmu;
			return result;
		}		
	}
	
	//4、pearson相似度
	public double sim_user_pearson(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//找到共同评分用户	
		
		if(comItem.size()==0)//相同评价用户个数
			return -1;
		
		//所有偏好之和，所有都用double型，防止相除时得到整数
		double sum1=0,sum2=0;
		//求平方之和
		double sum1Sq=0,sum2Sq=0;
		//求乘积之和 ∑XiYi
		double sumMulti = 0;
		double num1=0.0;
		double num2=0.0;
		double result=0.0;
		//通过计算两用户的共同评分项目计算相似度
		
		for(int item:comItem){
			
			sum1 += itemRating1.get(item);//所有偏好之和
			sum2 += itemRating1.get(item);
			
			sum1Sq += Math.pow(itemRating1.get(item), 2);//求平方之和
			sum2Sq += Math.pow(itemRating1.get(item), 2);
			
			sumMulti += itemRating1.get(item)*itemRating2.get(item);			
		}
		
		num1 = sumMulti - (sum1*sum2/comItem.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/comItem.size())*(sum2Sq-Math.pow(sum2,2)/comItem.size()));  
		if(num2==0)                                                
			return 0;
		result = num1/num2;
		return result;
	}
	
	//获取对item评分的K个最相似用户
	public List<Map.Entry<Integer, Double>> topKMatches(
			Table<Integer, Integer, Integer> userItemRating,
			Table<Integer, Integer, Integer> itemUserRating,
			int userID,int itemID,int k,int s){
		//找出mm中评价过itemID的所有用户	
		Map<Integer, Integer> userRating = itemUserRating.row(itemID);
		
		//项目-相似度-时间戳，不重复
		Map<Integer,Double> userSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//计算相似性
		for(int userid:userRating.keySet()){//item与其他userID已经评论过的项目的相似度						
			if(userid!=itemID){
				switch(s){
					case 1:
						sim = sim_user_Euclidean(userItemRating,userID,userid);
						break;
					case 2:
						sim = sim_user_cosine(userItemRating,userID,userid);
						break;
					case 3:
						sim = sim_user_modicos(userItemRating,userID,userid);
						break;
					case 4:
						sim = sim_user_pearson(userItemRating,userID,userid);
						break;
				}					
				if(sim>0){//相似度>0时才放进去，否则没有意义					
					userSim.put(userid,sim);
				}
			}
		}
		List<Map.Entry<Integer, Double>> userSim_sort = new ArrayList<Map.Entry<Integer, Double>>(userSim.entrySet());
	    Collections.sort(userSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//根据value排序
	        public int compare(Map.Entry<Integer, Double> o1,
	          Map.Entry<Integer, Double> o2) {
	         double result = o2.getValue() - o1.getValue();
	         if(result > 0)
	         	return 1;
	         else if(result == 0)
	         	return 0;
	         else 
	         	return -1;
	        }
	       });
	
	    //相似度从小到大排列，所以要取后K个
		if(userSim_sort.size()<=k){//如果小于k，只选择这些做推荐
			return userSim_sort;
		}else{//如果大于k，选择评分最高的用户
			userSim_sort = userSim_sort.subList(0, k);
			return userSim_sort;
		}
	}
}
