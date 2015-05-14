package slopeone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Compare {
	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Compare c = new Compare();
		c.MAE_RMSE_recall_precision();
	}
	
	//计算平均绝对误差和均方根误差
	public void MAE_RMSE_recall_precision() throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		double recall;
		double precision;
		int count=0;
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		List<Integer> result = new ArrayList<Integer>();
		conn = DBUtil.getConn();
		//137项是userid=1测试集内容
		pst1 = conn.prepareStatement("SELECT movieid from test1 where userid<=5 order by movieid");
		rs1 = pst1.executeQuery();
		while(rs1.next()){
			list1.add(rs1.getInt(1));//test集里userid<=5的movie加入
		}
		//取前20作为推荐项目		
		pst2 = conn.prepareStatement("SELECT movieid from slope_weight where userid=? order by score desc limit 20");
		for(int i=1;i<=5;i++){
			pst2.setInt(1, i);//找出5个用户各自的前N项推荐
			rs2 = pst2.executeQuery();
			while(rs2.next()){//将推荐的前20*5项放进来
				list2.add(rs2.getInt(1));
			}
		}
		
		/*****计算MAE-RMSE******/
		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from slope_weight a,test1 b where a.movieid=b.movieid and a.userid=b.userid");
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
		
		/*******计算recall-precision******/	
		result.clear();
		result.addAll(list1);
		result.retainAll(list2);
		double hit = result.size();//hit等于test1中user1的movie && 推荐给1的前20movie
		recall = hit/list1.size();
		precision = hit/list2.size();
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
