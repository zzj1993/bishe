package itemCF;

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

public class ItemCF {
	//计算用户之间的相似度
	public double sim_item_pearson(int itemPrefer[][],int item1,int item2){
		//相同用户	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		int i=0;
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Set<Integer> set = new HashSet<Integer>();
		
		//找出共同用户
		for(i=0;i<itemPrefer[item1].length;i++){//遍历耗时，hash省时
			if(itemPrefer[item1][i]!=0){
			  	set.add(i);//把用户装进去
			}	
		}
		for(i=0;i<itemPrefer[item2].length;i++){
			if(set.contains(i)&&itemPrefer[item2][i]!=0)
				sim.add(i);	
		}
//		System.out.println(sim.size());
		if(sim.size()==0)//相同评价用户个数
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
		for(int user:sim){
			
			sum1 += itemPrefer[item1][user];//所有偏好之和
			sum2 += itemPrefer[item2][user];
			
			sum1Sq += Math.pow(itemPrefer[item1][user], 2);//求平方之和
			sum2Sq += Math.pow(itemPrefer[item2][user], 2);
			
			sumMulti += itemPrefer[item1][user]*itemPrefer[item2][user];			
		}
		
		num1 = sumMulti - (sum1*sum2/sim.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/sim.size())*(sum2Sq-Math.pow(sum2,2)/sim.size()));  
		if(num2==0)                                                
			return 0;  

		result = num1/num2;
//		System.out.println(result);
		return result;
	}
	
	//获取与item最相似的K个项目
	public List<Map.Entry<Integer, Double>> topKMatches(int itemPrefer[][],int item,int userID,int k){
		Set<Integer> itemSet = new HashSet<Integer>();
		//找出itemPrefer中userID评价过的所有item
		for(int i=0;i<itemPrefer.length;i++){			
			if(itemPrefer[i][userID]!=0){
				itemSet.add(i);
			}
		}

		//相似度-用户，应该不会有相似度一样的用户，不重复
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//计算相似性
		for(int other:itemSet){
			if(other!=item){				
				sim = sim_item_pearson(itemPrefer,item,other);				
				itemSim.put(other,sim);
			}
		}
		List<Map.Entry<Integer, Double>> itemSim_sort = new ArrayList<Map.Entry<Integer, Double>>(itemSim.entrySet());
	    Collections.sort(itemSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//根据value排序
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
		if(itemSim_sort.size()<=k){//如果小于k，只选择这些做推荐
			return itemSim_sort;
		}else{//如果大于k，选择评分最高的用户
			itemSim_sort = itemSim_sort.subList(0, k);
			return itemSim_sort;
		}
	}
}
