package itemCF0411;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ItemCF {
	//计算用户之间的相似度
	public double sim_item_pearson(Map<Integer,Map<Integer,List<Double>>> mm,int item1,int item2) throws ClassNotFoundException, SQLException{
		//相同用户	
		ArrayList<Integer> comUser = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,List<Double>> m1 = mm.get(item1);
		Set<Integer> set1 = m1.keySet();
		
		Map<Integer,List<Double>> m2 = mm.get(item2);
		Set<Integer> set2 = m2.keySet();
		
		//找出共同用户
		for(int i:set2){
			if(set1.contains(i))
				comUser.add(i);
		}
//		System.out.println(comUser);//movie 1,2共同用户为1,42,72
		
		if(comUser.size()==0)//相同评价用户个数
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
		for(int user:comUser){
			sum1 += m1.get(user).get(0);//所有所用评分之和
			sum2 += m2.get(user).get(0);
			
			sum1Sq += Math.pow(m1.get(user).get(0), 2);//求平方之和
			sum2Sq += Math.pow(m2.get(user).get(0), 2);
			
			sumMulti += m1.get(user).get(0)
					*m2.get(user).get(0);

		}		
		num1 = sumMulti - (sum1*sum2/comUser.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/comUser.size())*(sum2Sq-Math.pow(sum2,2)/comUser.size()));  
	
		if(num2==0)                                                
			return 0;  
		else{
			result = num1/num2;
			return result;
		}		
	}

	//获取与item最相似的K个项目
	public List<Map.Entry<Integer, List<Double>>> topKMatches(Map<Integer,Map<Integer,List<Double>>> mm,int itemID,int userID,int k) throws ClassNotFoundException, SQLException{
		//存放item-timestamp
		Map<Integer,Double> itemMap = new HashMap<Integer,Double>();
		//找出mm中userID评价过的所有item
		Map<Integer,List<Double>> m;
		for(Entry<Integer,Map<Integer,List<Double>>> entry:mm.entrySet()){
			m = entry.getValue();
			if(m.containsKey(userID)){				
				itemMap.put(entry.getKey(), m.get(userID).get(1));//item-timestamp
			}				
		}

		//项目-相似度-时间戳，不重复
		Map<Integer,List<Double>> itemSim = new HashMap<Integer,List<Double>>();
		double sim=0.0;
		List<Double> li;
		//计算相似性
		for(Map.Entry<Integer,Double> entry:itemMap.entrySet()){//item与其他userID已经评论过的项目的相似度						
			int other = entry.getKey();
			double timestamp = entry.getValue();
			if(other!=itemID){
				li = new ArrayList<Double>();
				sim = sim_item_pearson(mm,itemID,other);
				li.add(sim);
				li.add(timestamp);
				if(sim>0){//相似度>0时才放进去，否则没有意义					
					itemSim.put(other,li);
				}
			}
		}	
		List<Map.Entry<Integer, List<Double>>> itemSim_sort = new ArrayList<Map.Entry<Integer, List<Double>>>(itemSim.entrySet());
	    Collections.sort(itemSim_sort, new Comparator<Map.Entry<Integer, List<Double>>>() {//根据value排序
	        public int compare(Map.Entry<Integer, List<Double>> o1,
	          Map.Entry<Integer, List<Double>> o2) {//分数放在list(0)
	         double result = o2.getValue().get(0) - o1.getValue().get(0);
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
