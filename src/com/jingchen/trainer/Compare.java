package com.jingchen.trainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
MAE=0.7686482805555556
RMSE=0.9706076482945214
*/
public class Compare {
	DBUtil db = new DBUtil();
	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Compare c = new Compare();
		c.MAE_RMSE_recall_precision();
	}

	public void MAE_RMSE_recall_precision() throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();//test1 like
		List<Integer> list2 = new ArrayList<Integer>();//test1 unlike
		List<Integer> list3 = new ArrayList<Integer>();//result rec
		List<Integer> list4 = new ArrayList<Integer>();//result unrec
		List<Integer> result = new ArrayList<Integer>();
		conn = db.getConn();
		pst1 = conn.prepareStatement("SELECT movieid from test1 where score>=4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));//test集里userid<=5的movie加入
		}		
		pst1 = conn.prepareStatement("SELECT movieid from test1 where score<4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list2.add(rs1.getInt(1));//test集里userid<=5的movie加入
		}
		pst1 = conn.prepareStatement("SELECT movieid from svd where score>=4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list3.add(rs1.getInt(1));//test集里userid<=5的movie加入
		}
		pst1 = conn.prepareStatement("SELECT movieid from svd where score<4");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list4.add(rs1.getInt(1));//test集里userid<=5的movie加入
		}
		
		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from svd a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			sum1 = rs1.getDouble(1);
			sum2 = rs1.getDouble(2);
			count = rs1.getInt(3);
		}
		mae = sum1/count;
		rmse = Math.sqrt(sum2/count);
		System.out.println("MAE="+mae);
		System.out.println("RMSE="+rmse);
		
		/*******recall-precision******/	
		result.clear();
		result.addAll(list1);
		result.retainAll(list3);//喜欢&被推荐
		double hit = result.size();//hit等于test1中user1的movie && 推荐给1的前20movie
		result.clear();
		result.addAll(list2);
		result.retainAll(list3);//不喜欢&被推荐
		precision = hit/(result.size()+hit);//(喜欢&被推荐)/(喜欢&被推荐+不喜欢&被推荐)
				
		result.clear();
		result.addAll(list1);
		result.retainAll(list4);//喜欢&未被推荐
		recall = hit/(result.size()+hit);//(喜欢&被推荐)/(喜欢&被推荐+喜欢&不被推荐)
		
		System.out.println("recall="+recall);
		System.out.println("precision="+precision);
		if(rs1!=null){
			rs1.close();
		}
		if(rs2!=null){
			rs2.close();
		}
		if(pst1!=null){
			pst1.close();
		}
		if(pst2!=null){
			pst2.close();
		}
		if(conn!=null){
			conn.close();
		}
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
	}
}
