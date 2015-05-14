package itemCF0407;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ItemCF {
	//�����û�֮������ƶ�
	public double sim_item_pearson(Map<Integer,Map<Integer,Integer>> mm,int item1,int item2){
		//��ͬ�û�	
		ArrayList<Integer> comUser = new ArrayList<Integer>();
		//�������Ѿ��ź��򣬲�����˫��for�����ǵĽ���(10��ms)��hash��ײ��죬0~1ms
		Map<Integer,Integer> m1 = mm.get(item1);
		Set<Integer> set1 = m1.keySet();
		
		Map<Integer,Integer> m2 = mm.get(item2);
		Set<Integer> set2 = m2.keySet();
		
		//�ҳ���ͬ�û�
		for(int i:set2){
			if(set1.contains(i))
				comUser.add(i);
		}
//		System.out.println(comUser);//movie 1,2��ͬ�û�Ϊ1,42,72
		
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
			sum1 += m1.get(user);//������������֮��
			sum2 += m2.get(user);
			
			sum1Sq += Math.pow(m1.get(user), 2);//��ƽ��֮��
			sum2Sq += Math.pow(m2.get(user), 2);
			
			sumMulti += m1.get(user)*m2.get(user);			
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
	
	
	//��ȡ��item�����Ƶ�K����Ŀ
	public List<Map.Entry<Integer, Double>> topKMatches(Map<Integer,Map<Integer,Integer>> mm,int itemID,int userID,int k){
		Set<Integer> itemSet = new HashSet<Integer>();
		//�ҳ�mm��userID���۹�������item
		Map<Integer,Integer> m;		
		for(Entry<Integer,Map<Integer,Integer>> entry:mm.entrySet()){
			m = entry.getValue();
			if(m.containsKey(userID))
				itemSet.add(entry.getKey());
		}
//		System.out.println(itemSet+"itemSet");//
		//���ƶ�-�û���Ӧ�ò��������ƶ�һ�����û������ظ�
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//����������
		for(int other:itemSet){//item������userID�Ѿ����۹�����Ŀ�����ƶ�						
			if(other!=itemID){
				sim = sim_item_pearson(mm,itemID,other);
				if(sim>0)//���ƶ�>0ʱ�ŷŽ�ȥ������û������
					itemSim.put(other,sim);
			}
		}	
		List<Map.Entry<Integer, Double>> itemSim_sort = new ArrayList<Map.Entry<Integer, Double>>(itemSim.entrySet());
	    Collections.sort(itemSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//����value����
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
		if(itemSim_sort.size()<=k){//���С��k��ֻѡ����Щ���Ƽ�
			return itemSim_sort;
		}else{//�������k��ѡ��������ߵ��û�
			itemSim_sort = itemSim_sort.subList(0, k);
			return itemSim_sort;
		}
	}
}
