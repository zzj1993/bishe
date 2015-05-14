package userCF0503;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;

public class userAverage {
	//计算用户的平均评分
	public double getAverage(Table<Integer, Integer, Integer> userItemRating,int userID){		
		double count=0.0,sum=0.0;
		Map<Integer, Integer> itemRating = userItemRating.row(userID);
		for(Entry<Integer, Integer> entry:itemRating.entrySet()){
			sum += entry.getValue();
		}
		count = itemRating.size();	
		if(count==0)
			return 0;
		else
			return (double)sum/count;
	}
}
