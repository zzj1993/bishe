package itemCF0503;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class ItemCF {
	//1��ŷ����þ���
	public double sim_item_Euclidean(Table<Integer, Integer, Integer> itemUserRating,int item1,int item2){
		Map<Integer,Integer> userRating1 = itemUserRating.row(item1);
		Map<Integer,Integer> userRating2 = itemUserRating.row(item2);

		Set<Integer> set1 = userRating1.keySet();
		Set<Integer> set2 = userRating2.keySet();
		SetView<Integer> comUser = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�
//		for (Integer integer : comUser)
//		    System.out.println(integer);		
		
		if(comUser.size()==0)//��ͬ�����û�����
			return -1;

		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2,count=0;
		double distance=0.0;
		double result=0.0;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int user:comUser){			
			score1 = userRating1.get(user);//������������֮��
			score2 = userRating2.get(user);
			distance += Math.pow(score1-score2, 2);
			count++;
		}
		result = 1.0/(1+Math.sqrt(distance)/Math.sqrt(count));
		return result;
	}
	
	//2���������ƶ�
	public double sim_item_cosine(Table<Integer, Integer, Integer> itemUserRating,int item1,int item2){
		Map<Integer,Integer> userRating1 = itemUserRating.row(item1);
		Map<Integer,Integer> userRating2 = itemUserRating.row(item2);

		Set<Integer> set1 = userRating1.keySet();
		Set<Integer> set2 = userRating2.keySet();
		SetView<Integer> comUser = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�
//		for (Integer integer : comUser)
//		    System.out.println(integer);		
		
		if(comUser.size()==0)//��ͬ�����û�����
			return -1;
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int user:comUser){			
			score1 = userRating1.get(user);//������������֮��
			score2 = userRating2.get(user);
			sum12 += score1*score2;
			sum_score12 += Math.pow(score1, 2);
			sum_score22 += Math.pow(score2, 2);		
		}
		result = sum12/(Math.sqrt(sum_score12)*Math.sqrt(sum_score22));
		return result;
	}
	//3���������������ƶ�
	public double sim_item_modicos(Table<Integer, Integer, Integer> itemUserRating,int item1,int item2){
		Map<Integer,Integer> userRating1 = itemUserRating.row(item1);
		Map<Integer,Integer> userRating2 = itemUserRating.row(item2);

		Set<Integer> set1 = userRating1.keySet();
		Set<Integer> set2 = userRating2.keySet();
		SetView<Integer> comUser = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�
//		for (Integer integer : comUser)
//		    System.out.println(integer);		
		
		if(comUser.size()==0)//��ͬ�����û�����
			return -1;
		
		//����ƫ��֮�ͣ����ж���double�ͣ���ֹ���ʱ�õ�����
		int score1,score2,sum_score1=0,sum_score2=0;
		double sum_score12=0.0,sum_score22=0.0;
		double result=0.0;
		double sum12=0;
		
		itemAverage avg = new itemAverage();
		double avgItem1 = avg.getAverage(itemUserRating, item1);
		double avgItem2 = avg.getAverage(itemUserRating, item2);;
		//ͨ���������û��Ĺ�ͬ������Ŀ�������ƶ�
		for(int user:comUser){			
			score1 = userRating1.get(user);//������������֮��
			score2 = userRating1.get(user);
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
	public double sim_item_pearson(Table<Integer, Integer, Integer> itemUserRating,int item1,int item2) throws ClassNotFoundException, SQLException{
		Map<Integer,Integer> userRating1 = itemUserRating.row(item1);
		Map<Integer,Integer> userRating2 = itemUserRating.row(item2);

		Set<Integer> set1 = userRating1.keySet();
		Set<Integer> set2 = userRating2.keySet();
		SetView<Integer> comUser = Sets.intersection(set1, set2);//�ҵ���ͬ�����û�
//		for (Integer integer : comUser)
//		    System.out.println(integer);		
		
		if(comUser.size()==0)//��ͬ�����û�����
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
		for(int user:comUser){
			sum1 += userRating1.get(user);//������������֮��
			sum2 += userRating2.get(user);
			
			sum1Sq += Math.pow(userRating1.get(user), 2);//��ƽ��֮��
			sum2Sq += Math.pow(userRating2.get(user), 2);
			
			sumMulti += userRating1.get(user)*userRating2.get(user);
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
	
	//��ȡ��item�����Ƶ�K����Ŀ,s�����ƶȶ�������
	public List<Map.Entry<Integer, Double>> topKMatches(
			Table<Integer, Integer, Integer> itemUserRating,
			Table<Integer, Integer, Integer> userItemRating,
			int itemID,int userID,int k,int s) throws ClassNotFoundException, SQLException{
		//�ҳ�mm��userID���۹�������item		
		Map<Integer, Integer> itemRating = userItemRating.row(userID);
		
		//��Ŀ-���ƶ�-ʱ��������ظ�
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//����������
		for(int itemid:itemRating.keySet()){//item������userID�Ѿ����۹�����Ŀ�����ƶ�						
			if(itemid!=itemID){
				switch(s){
					case 1:
						sim = sim_item_Euclidean(itemUserRating,itemID,itemid);
						break;
					case 2:
						sim = sim_item_cosine(itemUserRating,itemID,itemid);
						break;
					case 3:
						sim = sim_item_modicos(itemUserRating,itemID,itemid);
						break;
					case 4:
						sim = sim_item_pearson(itemUserRating,itemID,itemid);
						break;
				}
					
				if(sim>0){//���ƶ�>0ʱ�ŷŽ�ȥ������û������					
					itemSim.put(itemid,sim);
				}
			}
		}	
		List<Map.Entry<Integer, Double>> itemSim_sort = new ArrayList<Map.Entry<Integer, Double>>(itemSim.entrySet());
	    Collections.sort(itemSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//����value����
	        public int compare(Map.Entry<Integer, Double> o1,
	          Map.Entry<Integer, Double> o2) {//��������list(0)
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
		if(itemSim_sort.size()<=k){//���С��k��ֻѡ����Щ���Ƽ�
			return itemSim_sort;
		}else{//�������k��ѡ��������ߵ��û�
			itemSim_sort = itemSim_sort.subList(0, k);
			return itemSim_sort;
		}
	}
}
