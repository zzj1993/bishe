package itemCF0503;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;

public class itemAverage {
	public double getAverage(Table<Integer, Integer, Integer> itemUserRating,int itemID){		
		double count=0.0,sum=0.0;
		Map<Integer, Integer> userRating = itemUserRating.row(itemID);
		for(Entry<Integer, Integer> entry:userRating.entrySet()){
			sum += entry.getValue();
		}
		count = userRating.size();	
		if(count==0)
			return 0;
		else
			return (double)sum/count;
	}
}
