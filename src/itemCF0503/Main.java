package itemCF0503;
/**
 * Map<int,Map>实现
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Table;

//35 000ms左右
public class Main {
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
		long t1 = System.currentTimeMillis();
		DBUtil db = new DBUtil();
		Recommend re = new Recommend();
		ItemCF uc = new ItemCF();

//		Object[] a = db.loadMovieLensTrain();
//		Table<Integer, Integer, Integer> itemUserRating = (Table<Integer, Integer, Integer>) a[0];
//		Table<Integer, Integer, Double> itemUserTime = (Table<Integer, Integer, Double>) a[1];		
//		System.out.println(itemUserRating);
		
//		System.out.println(itemUserTime);
//		Map<Integer,List<Integer>> m = mm.get(1);
//		for(Map.Entry<Integer, List<Integer>> entry:m.entrySet()){
//			int userid = entry.getKey();
//			List<Integer> list = entry.getValue();
//			System.out.println(userid+" "+list.get(0)+" "+list.get(1));
//		}
//		Table<Integer, Integer, Double> itemSim = uc.sim_item_pearson(itemUserRating);//0~10ms
//		for(int i:itemSim.rowKeySet()){
//			for(int j:itemSim.columnKeySet()){
//				System.out.print(itemSim.get(i, j)+"\t");
//			}
//			System.out.println();
//		}
//		double avg = re.getAverage(itemUserRating, 1);//0~10ms
//		System.out.println("avg="+avg);
//		List<Map.Entry<Integer, Double>> list = uc.topKMatches(mm, 10, 1, 20);//60~70ms 
//		System.out.println("list="+list);
//		System.out.println(re.getRating(mm, 10, 1, 20));//40~60ms
		
		
		//为一个用户预测剩余项目并写到文本中用了541ms，写到数据库中用了24020ms
		//但是只能将结果插入数据库中，compare需要用到
//		for(int i=1;i<=5;i++){
			re.getAllUserRating();
//		}
		
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");


	}
}
