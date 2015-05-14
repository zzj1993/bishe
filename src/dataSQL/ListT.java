package dataSQL;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ListT {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException{
		
		long t1 = System.currentTimeMillis();
		loadFile lf = new loadFile();
		
		int prefer1[][] = lf.loadMovieLensTrain();
		
//		double rating = lf.getRating(prefer1, 1, 3, 20);
//		System.out.println("rating="+rating);
//		List<Entry<Integer, Double>> map = lf.topKMatches(prefer1, 1, 1, 20);
		lf.getAllUserRating();
//		System.out.println(lf.getAverage(prefer1, 1));

		long t2 = System.currentTimeMillis();
		System.out.println("end"+(t2-t1)+"ms");
	}
}
