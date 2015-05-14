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
	//1��Euclidean���ƶ�
	public double sim_user_Euclidean(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�	
		
		if(comItem.size()==0)//��ͬ�����û�����
			return -1;
		
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2,count=0;
		double distance=0.0;
		double result=0.0;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int item:comItem){			
			score1 = itemRating1.get(item);//������������֮��
			score2 = itemRating2.get(item);
			distance += Math.pow(score1-score2, 2);
			count++;
		}
		result = 1.0/(1+Math.sqrt(distance)/Math.sqrt(count));
		return result;
	}
	
	//2���������ƶ�
	public double sim_user_cosine(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�	
		
		if(comItem.size()==0)//��ͬ�����û�����
			return -1;
		
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int item:comItem){			
			score1 = itemRating1.get(item);//������������֮��
			score2 = itemRating1.get(item);
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		result = sum12/(Math.sqrt(sum_score12)*Math.sqrt(sum_score22));
		return result;
	}
	
	//3���������������ƶ�
	public double sim_user_modicos(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�	
		
		if(comItem.size()==0)//��ͬ�����û�����
			return -1;
		
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2,sum_score1=0,sum_score2=0;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		
		itemAverage avg = new itemAverage();
		double avgItem1 = avg.getAverage(userItemRating, user1);
		double avgItem2 = avg.getAverage(userItemRating, user2);;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int item:comItem){			
			score1 = itemRating1.get(item);//������������֮��
			score2 = itemRating2.get(item);
			sum_score1 += score1;
			sum_score2 += score2;
			
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		double fenzi = sum12-avgItem2*sum_score1;//ע���Ĺ����Ϊ0���������ܿ�ƽ��
		double fenmu = Math.sqrt(Math.abs(sum_score12-avgItem1*sum_score1))*Math.sqrt(Math.abs(sum_score22-avgItem2*sum_score2));
		if(fenmu==0)
			return 0;
		else{
			result = fenzi/fenmu;
			return result;
		}		
	}
	
	//4��pearson���ƶ�
	public double sim_user_pearson(Table<Integer, Integer, Integer> userItemRating,int user1,int user2){
		Map<Integer,Integer> itemRating1 = userItemRating.row(user1);
		Map<Integer,Integer> itemRating2 = userItemRating.row(user2);

		Set<Integer> set1 = itemRating1.keySet();
		Set<Integer> set2 = itemRating2.keySet();
		SetView<Integer> comItem = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�	
		
		if(comItem.size()==0)//��ͬ�����û�����
			return -1;
		
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		double sum1=0,sum2=0;
		//��ƽ��֮��
		double sum1Sq=0,sum2Sq=0;
		//��˻�֮�� ��XiYi
		double sumMulti = 0;
		double num1=0.0;
		double num2=0.0;
		double result=0.0;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		
		for(int item:comItem){
			
			sum1 += itemRating1.get(item);//����ƫ��֮��
			sum2 += itemRating1.get(item);
			
			sum1Sq += Math.pow(itemRating1.get(item), 2);//��ƽ��֮��
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
	
	//��ȡ��item���ֵ�K���������û�
	public List<Map.Entry<Integer, Double>> topKMatches(
			Table<Integer, Integer, Integer> userItemRating,
			Table<Integer, Integer, Integer> itemUserRating,
			int userID,int itemID,int k,int s){
		//�ҳ�mm�����۹�itemID�������û�	
		Map<Integer, Integer> userRating = itemUserRating.row(itemID);
		
		//��Ŀ-���ƶ�-ʱ��������ظ�
		Map<Integer,Double> userSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//����������
		for(int userid:userRating.keySet()){//item������userID�Ѿ����۹�����Ŀ�����ƶ�						
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
				if(sim>0){//���ƶ�>0ʱ�ŷŽ�ȥ������û������					
					userSim.put(userid,sim);
				}
			}
		}
		List<Map.Entry<Integer, Double>> userSim_sort = new ArrayList<Map.Entry<Integer, Double>>(userSim.entrySet());
	    Collections.sort(userSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//����value����
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
	
	    //���ƶȴ�С�������У�����Ҫȡ��K��
		if(userSim_sort.size()<=k){//���С��k��ֻѡ����Щ���Ƽ�
			return userSim_sort;
		}else{//�������k��ѡ��������ߵ��û�
			userSim_sort = userSim_sort.subList(0, k);
			return userSim_sort;
		}
	}
}
