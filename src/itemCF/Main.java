package itemCF;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		int m=0;
		
		int itemPrefer[][] = db.loadMovieLensTrain();//400ms
		/*for(int i=0;i<itemPrefer[1].length;i++){
			if(itemPrefer[1][i]!=0)
				m++;
		}
		System.out.println(m);*/
//		double sim = uc.sim_item_pearson(itemPrefer, 1, 2);//0~10ms
//		System.out.println("sim="+sim);
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
		System.out.println(re.getRating(itemPrefer, 10, 1, 20));//40~60ms
//		re.getAllUserRating();//
	
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}
}
