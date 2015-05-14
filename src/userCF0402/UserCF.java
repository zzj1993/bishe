package userCF0402;

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

public class UserCF {
	//计算用户之间的相似度
	public double sim_user_pearson(Object prefer[],int user1,int user2){
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,Integer> map1 = (Map) prefer[user1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map) prefer[user2];
		Set<Integer> set2 = map2.keySet();
		//找到共同项目
		for(int i:set2){
			if(set1.contains(i)){
				sim.add(i);
//				System.out.println(i);
			}
		}

//		System.out.println(sim.size());

		if(sim.size()==0)//相同评价项目个数
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
		
		for(int item:sim){
			
			sum1 += map1.get(item);//所有偏好之和
			sum2 += map2.get(item);
			
			sum1Sq += Math.pow(map1.get(item), 2);//求平方之和
			sum2Sq += Math.pow(map2.get(item), 2);
			
			sumMulti += map1.get(item)*map2.get(item);			
		}
		
		num1 = sumMulti - (sum1*sum2/sim.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/sim.size())*(sum2Sq-Math.pow(sum2,2)/sim.size()));  
		if(num2==0)                                                
			return 0;

		result = num1/num2;
//		System.out.println("result="+result);
		return result;
	}
	
	//获取对item评分的K个最相似用户
	public List<Map.Entry<Integer, Double>> topKMatches(Object prefer[],int user,int itemID,int k){
		Set<Integer> userSet = new HashSet<Integer>();
		//找出所有prefer中评价过itemID的用户，存入userSet
		/*for(int i=0;i<prefer.length;i++){			
			if(prefer[i][itemID]!=0){
				userSet.add(i);//评论过项目itemID的人
			}
		}*/
		for(int i=0;i<prefer.length;i++){	
			if(prefer[i]!=null){
				Map<Integer,Integer> m = (Map)prefer[i];				
				if(m.containsKey(itemID))
					userSet.add(i);
			}
			
//			if(m.containsKey(itemID))
//				userSet.add(i);//评论过项目itemID的人
		}
//		System.out.println("userSet.size"+userSet.size());
		//相似度-用户，应该不会有相似度一样的用户，不重复
		Map<Integer,Double> userSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//计算相似性
		for(int other:userSet){
			if(other!=user){				
				sim = sim_user_pearson(prefer,user,other);				
				userSim.put(other,sim);
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
