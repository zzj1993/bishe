package itemCF0405;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemCF {
	//计算用户之间的相似度
	public double sim_item_Euclidean(Object itemPrefer[],int item1,int item2){
		//相同用户	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,Integer> map1 = (Map)itemPrefer[item1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map)itemPrefer[item2];
		Set<Integer> set2 = map2.keySet();
		//找出共同用户
		for(int i:set2){
			if(set1.contains(i))
				sim.add(i);
		}
//		System.out.println(sim.size());
		
		if(sim.size()==0)//相同评价用户个数
			return -1;

		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2,count=0;
		double distance=0.0;
		double result=0.0;
		//通过计算两用户的共同评分项目计算相似度
		for(int user:sim){			
			score1 = map1.get(user);//所有所用评分之和
			score2 = map2.get(user);
			distance += Math.pow(score1-score2, 2);
			count++;
		}
		result = 1.0/(1+Math.sqrt(distance)/Math.sqrt(count));
		return result;
	}
	public double sim_item_cosine(Object itemPrefer[],int item1,int item2){
		//相同用户	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,Integer> map1 = (Map)itemPrefer[item1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map)itemPrefer[item2];
		Set<Integer> set2 = map2.keySet();
		//找出共同用户
		for(int i:set2){
			if(set1.contains(i))
				sim.add(i);
		}
		if(sim.size()==0)//相同评价用户个数
			return -1;

		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		//通过计算两用户的共同评分项目计算相似度
		for(int user:sim){			
			score1 = map1.get(user);//所有所用评分之和
			score2 = map2.get(user);
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		result = sum12/(Math.sqrt(sum_score12)*Math.sqrt(sum_score22));
		return result;
	}
	/*********调整的余弦相似度**********/
	public double sim_item_modicos(Object itemPrefer[],double avgItem1,double avgItem2,int item1,int item2){
		//相同用户	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,Integer> map1 = (Map)itemPrefer[item1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map)itemPrefer[item2];
		Set<Integer> set2 = map2.keySet();
		//找出共同用户
		for(int i:set2){
			if(set1.contains(i))
				sim.add(i);
		}
		if(sim.size()==0)//相同评价用户个数
			return -1;

		
		//所有偏好之和，所有都用double型，防止相除时得到整数
		int score1,score2,sum_score1=0,sum_score2=0;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		//通过计算两用户的共同评分项目计算相似度
		for(int user:sim){			
			score1 = map1.get(user);//所有所用评分之和
			score2 = map2.get(user);
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
	public double sim_item_pearson(Object itemPrefer[],int item1,int item2){
		//相同用户	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		Map<Integer,Integer> map1 = (Map)itemPrefer[item1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map)itemPrefer[item2];
		Set<Integer> set2 = map2.keySet();
		//找出共同用户
		for(int i:set2){
			if(set1.contains(i))
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
			
			sum1 += map1.get(user);//所有所用评分之和
			sum2 += map2.get(user);
			
			sum1Sq += Math.pow(map1.get(user), 2);//求平方之和
			sum2Sq += Math.pow(map2.get(user), 2);
			
			sumMulti += map1.get(user)*map2.get(user);			
		}
		
		num1 = sumMulti - (sum1*sum2/sim.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/sim.size())*(sum2Sq-Math.pow(sum2,2)/sim.size()));  
		if(num2==0)                                                
			return 0;  

		result = num1/num2;
		return result;
	}
	//获取与item最相似的K个项目
	public List<Map.Entry<Integer, Double>> topKMatches(Object itemPrefer[],int item,int userID,int k){
		Set<Integer> itemSet = new HashSet<Integer>();
		//找出itemPrefer中userID评价过的所有item
		for(int i=0;i<itemPrefer.length;i++){	
			if(itemPrefer[i]!=null){
				Map<Integer,Integer> m = (Map)itemPrefer[i];				
				if(m.containsKey(userID))
					itemSet.add(i);
			}
		}
//		System.out.println("itemSet.size="+itemSet.size());
		//相似度-用户，应该不会有相似度一样的用户，不重复
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//计算相似性
		Recommend re = new Recommend();
		double avgItem = re.getAverage(itemPrefer, item); 
//		System.out.println("item"+item);
		for(int other:itemSet){
			if(other!=item){				
//				sim = sim_item_Euclidean(itemPrefer,item,other);
//				sim = sim_item_cosine(itemPrefer,item,other);
				double avgOther = re.getAverage(itemPrefer, other);
				sim = sim_item_modicos(itemPrefer,avgItem,avgOther,item,other);
//				sim = sim_item_pearson(itemPrefer,item,other);
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
