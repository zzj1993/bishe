package userCF0402;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
		
//		int prefer1[][] = db.loadMovieLensTrain();
//		lf.getAllUserRating();
//		int prefer1[][] = lf.loadMovieLensTrain();
		
		
//		Object prefer1[] = db.loadMovieLensTrain();
//		Object prefer2[] = db.loadMovieLensTest();
		
		long t1 = System.currentTimeMillis();
//		Map map = (Map) prefer[1];
//		
//		System.out.println(map.keySet());
/*		Map<Integer,Integer> map = new HashMap<Integer,Integer>();

		
		Object a[] = new Object[10];
		map.put(7, 3);
		map.put(8, 3);
		map.put(9, 3);
		a[3]=map;
		
			if(a[2]==null){
				map = new HashMap<Integer,Integer>();//不能用clear，不然后面的数组元素存进去的map会被覆盖
				map.put(11, 22);
				a[2]=map;
			}else{
				map.put(111, 222);
				a[2]=map;
			}
			System.out.println(a[3]);
			System.out.println(a[2]);*/

//		uc.sim_user_pearson(prefer, 1, 2);//1 ms
//		re.getAverage(prefer, 1);//0 ms
//		System.out.println(re.getAverage(prefer, 1)+"avg1");
//		uc.topKMatches(prefer1, 1, 1, 20);//0ms 
//		re.getRating(prefer, 1, 6, 20);//10 ms，for循环中，所有用户对未评价过的所有项目的评分
		re.getAllUserRating();//400 000 ms，约7 min。200 000
				
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}
}
