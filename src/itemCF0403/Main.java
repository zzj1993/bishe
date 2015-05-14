package itemCF0403;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//35 000ms×óÓÒ
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		long t1 = System.currentTimeMillis();
		DBUtil db = new DBUtil();
		Recommend re = new Recommend();
		ItemCF uc = new ItemCF();

		Object itemPrefer[] = db.loadMovieLensTrain();		
		double sim = uc.sim_item_pearson(itemPrefer,1, 2);//0~10ms
		System.out.println("sim="+sim);
//		double avg = re.getAverage(itemPrefer, 1);//0~10ms
//		System.out.println("avg="+avg);
//		List<Map.Entry<Integer, Double>> list = uc.topKMatches(itemPrefer, 1, 1, 20);//60~70ms 
//		Iterator<Map.Entry<Integer, Double>> it = list.iterator();
//		int userid;
//		double score;
//		while(it.hasNext()){
//			Map.Entry<Integer, Double> entry = it.next();
//			userid = entry.getKey();
//			score = entry.getValue();
//			System.out.println(userid+" "+score);
//		}
//		re.getRating(itemPrefer, 1, 6, 20);//40~60ms
		re.getAllUserRating();
	
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}
}
