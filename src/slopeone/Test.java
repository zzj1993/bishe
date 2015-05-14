package slopeone;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Test {
	static int[] a = new int[3];
	public static void main(String[] args) {
/*		Map<Integer,Map<Integer,Double>> mm = new HashMap<Integer,Map<Integer,Double>>();
		Map<Integer,Double> m = new HashMap<Integer,Double>();
		m.put(1, 1.0);
		m.put(2, 2.0);
		m.put(3, 3.0);
		mm.put(1, m);
		mm.put(2, m);
		mm.put(3, m);
//		System.out.println(mm);
//		System.out.println(mm.values());
		
	    try {
			SlopeOne1.loadMovieLensTrain();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    for(int i=0;i<SlopeOne1.mAllItems.length;i++)
	    	System.out.println(SlopeOne1.mAllItems[i]);*/
	    Map<Integer,Map<Integer,Integer>> mm1= new HashMap<Integer,Map<Integer,Integer>>();
	    Map<Integer,Integer> m1 = new HashMap<Integer,Integer>();
	    m1.put(1, 1);
	    m1.put(2, 1);
	    mm1.put(1, m1);
	    m1 = new HashMap<Integer,Integer>();
	    m1.put(1, 1);
	    m1.put(2, 1);
	    m1.put(3, 1);
	    m1.put(4, 1);
	    mm1.put(2, m1);
	    System.out.println(mm1.values());
	}

}
