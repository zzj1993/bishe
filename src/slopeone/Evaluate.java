package slopeone;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Evaluate {

	Connection conn;
	PreparedStatement pst1;
	PreparedStatement pst2;
	ResultSet rs1;
	ResultSet rs2;
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		Evaluate c = new Evaluate();
		c.MAE_RMSE();
	}

	//计算平均绝对误差和均方根误差
	public void MAE_RMSE() throws ClassNotFoundException, SQLException{
		long t1 = System.currentTimeMillis();
		
		double sum1=0.0,sum2=0.0;
		double mae;
		double rmse;
		int count=0;
		conn = DBUtil.getConn();

		/*****计算MAE-RMSE******/
		pst1 = conn.prepareStatement("SELECT sum(abs(a.score-b.score)),sum(pow(a.score-b.score,2)),count(*) from test a,slopeone b where a.userid=b.userid and a.movieid=b.movieid");
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
	}
}
