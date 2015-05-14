package itemCF0406;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemCF {
	//�����û�֮������ƶ�

	public double sim_item_pearson(Object itemPrefer[],int item1,int item2){
		//��ͬ�û�	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		//�������Ѿ��ź��򣬲�����˫��for�����ǵĽ���(10��ms)��hash��ײ��죬0~1ms
		Map<Integer,Integer> map1 = (Map)itemPrefer[item1];
		Set<Integer> set1 = map1.keySet();
		
		Map<Integer,Integer> map2 = (Map)itemPrefer[item2];
		Set<Integer> set2 = map2.keySet();
		//�ҳ���ͬ�û�
		for(int i:set2){
			if(set1.contains(i))
				sim.add(i);
		}
//		System.out.println(sim.size());
		
		if(sim.size()==0)//��ͬ�����û�����
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
		for(int user:sim){
			
			sum1 += map1.get(user);//������������֮��
			sum2 += map2.get(user);
			
			sum1Sq += Math.pow(map1.get(user), 2);//��ƽ��֮��
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
	
	
	//��ȡ��item�����Ƶ�K����Ŀ
	public List<Map.Entry<Integer, Double>> topKMatches(Object itemPrefer[],int item,int userID,int k){
		Set<Integer> itemSet = new HashSet<Integer>();
		//�ҳ�itemPrefer��userID���۹�������item
		for(int i=0;i<itemPrefer.length;i++){	
			if(itemPrefer[i]!=null){
				Map<Integer,Integer> m = (Map)itemPrefer[i];				
				if(m.containsKey(userID))
					itemSet.add(i);
			}
		}
		//���ƶ�-�û���Ӧ�ò��������ƶ�һ�����û������ظ�
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//����������
		Recommend re = new Recommend();
		double avgItem = re.getAverage(itemPrefer, item); 
		for(int other:itemSet){
			if(other!=item){				
				double avgOther = re.getAverage(itemPrefer, other);
				sim = sim_item_pearson(itemPrefer,item,other);
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
