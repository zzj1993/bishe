package userCF0503;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Table;
//用时30 000 ms
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {		
		DBUtil db = new DBUtil();
		UserCF uc = new UserCF();
		Recommend re = new Recommend();

		long t1 = System.currentTimeMillis();
//		Object a[] = db.loadMovieLensTrain();
//		Table<Integer,Integer,Integer> userItemRating = (Table<Integer, Integer, Integer>) a[0]; 
//		Table<Integer,Integer,Double> userItemTime = (Table<Integer, Integer, Double>) a[1];
//		System.out.println(userItemTime);

//		uc.sim_user_pearson(prefer, 1, 2);//1 ms
//		re.getAverage(prefer, 1);//0 ms
//		System.out.println(re.getAverage(prefer, 1)+"avg1");
//		uc.topKMatches(prefer1, 1, 1, 20);//0ms 
//		re.getRating(prefer, 1, 6, 20);//10 ms，for循环中，所有用户对未评价过的所有项目的评分
//		for(int i=1;i<=5;i++){
			re.getAllUserRating();
//		}
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}
}
