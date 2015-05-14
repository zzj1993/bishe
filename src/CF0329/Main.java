package CF0329;

import java.io.IOException;
import java.sql.SQLException;
//1000��Ԥ������40 000 ms����
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
		int prefer1[][] = db.loadMovieLensTrain();
//		lf.getAllUserRating();
//		int prefer1[][] = lf.loadMovieLensTrain();
		
		
//		uc.sim_user_pearson(prefer1, 1, 2);//1 ms
//		System.out.println(uc.sim_user_pearson(prefer1, 1, 2));
		//		lf.getAverage(prefer1, 1);//0 ms
//		uc.topKMatches(prefer1, 1, 1, 20);//0ms 
//		lf.getRating(prefer1, 1, 7, 20);//10 ms��forѭ���У������û���δ���۹���������Ŀ������
		re.getAllUserRating();//400 000 ms��Լ7 min��200 000
	
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}
}
