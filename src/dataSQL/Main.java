package dataSQL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Martrics.SparseArray;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		loadFile2 lf = new loadFile2();
//		int prefer1[][] = lf.loadMovieLensTrain();
//		lf.getAllUserRating();
		int prefer1[][] = lf.loadMovieLensTrain();
		
		long t1 = System.currentTimeMillis();
//		lf.sim_user_pearson(prefer1, 1, 3);//1 ms
//		lf.getAverage(prefer1, 1);//0 ms
//		lf.topKMatches(prefer1, 1, 1, 20);//0ms 
//		lf.getRating(prefer1, 1, 7, 20);//10 ms��forѭ���У������û���δ���۹���������Ŀ������
		lf.getAllUserRating();//400 000 ms��Լ7 min��200 000
	
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
		
	}

}
