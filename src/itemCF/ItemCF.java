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
	//�����û�֮������ƶ�
	public double sim_item_pearson(int itemPrefer[][],int item1,int item2){
		//��ͬ�û�	
		ArrayList<Integer> sim = new ArrayList<Integer>();
		int i=0;
		//�������Ѿ��ź��򣬲�����˫��for�����ǵĽ���(10��ms)��hash��ײ��죬0~1ms
		Set<Integer> set = new HashSet<Integer>();
		
		//�ҳ���ͬ�û�
		for(i=0;i<itemPrefer[item1].length;i++){//������ʱ��hashʡʱ
			if(itemPrefer[item1][i]!=0){
			  	set.add(i);//���û�װ��ȥ
			}	
		}
		for(i=0;i<itemPrefer[item2].length;i++){
			if(set.contains(i)&&itemPrefer[item2][i]!=0)
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
			
			sum1 += itemPrefer[item1][user];//����ƫ��֮��
			sum2 += itemPrefer[item2][user];
			
			sum1Sq += Math.pow(itemPrefer[item1][user], 2);//��ƽ��֮��
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
	
	//��ȡ��item�����Ƶ�K����Ŀ
	public List<Map.Entry<Integer, Double>> topKMatches(int itemPrefer[][],int item,int userID,int k){
		Set<Integer> itemSet = new HashSet<Integer>();
		//�ҳ�itemPrefer��userID���۹�������item
		for(int i=0;i<itemPrefer.length;i++){			
			if(itemPrefer[i][userID]!=0){
				itemSet.add(i);
			}
		}

		//���ƶ�-�û���Ӧ�ò��������ƶ�һ�����û������ظ�
		Map<Integer,Double> itemSim = new HashMap<Integer,Double>();
		double sim=0.0;
		//����������
		for(int other:itemSet){
			if(other!=item){				
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
