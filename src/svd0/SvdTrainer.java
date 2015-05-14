package svd0;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SvdTrainer {	
	SvdModel model;
	SvdReader trainData;
	String trainFile;
	
	public SvdTrainer(String trainFilePath) throws IOException
	{
		trainFile = trainFilePath;
		trainData = new MovieLensDataReader(trainFile);
		int userNum = trainData.getUserNum();
		int itemNum = trainData.getItemNum();
		double minPref=trainData.getMinPref();
		double maxPref=trainData.getMaxPref();
		
		int rank =150;//增大潜在隐义因子数量,收敛速度较快,但程序运行速度较慢
		model= new SvdModel(userNum,itemNum,rank,minPref,maxPref);
		
		
	}
	
	public void train(int maxIter)
	{
		int count=0;
		double lastRmse = Double.MAX_VALUE;
//		while(true)
//		{		
//			System.out.printf("第%d次迭代:",count++);
			double loss=0.0;
			int n=0;
			while(trainData.hasNext())
			{
				RatingRecord rating = trainData.getNext();
				int u=rating.user;
				int i=rating.item;
				double p=rating.pref;
							
				//更新用户u的特征向量			
				//更新物品i的特征向量
//				loss+= model.update(u,i,p);
//				n++;							
			}
			
//			double rmse=Math.sqrt(loss/n);
//			System.out.println("rmse:"+rmse);
			
			//判断一下,如果rmse开始增大,则停止迭代
//			if(rmse>lastRmse) break;
//			if(count>maxIter) break;
			
//			lastRmse = rmse;			
//			trainData.reset();						
//		}133992ms
		
	}
	
	public void test(String testFile) throws IOException, ClassNotFoundException, SQLException
	{
		SvdReader testData = new MovieLensDataReader(testFile);
		double sum=0.0;
		int n=0;
		Connection conn = DBUtil.getConn();
		PreparedStatement pst =conn.prepareStatement("insert into svd(userid,movieid,score) values(?,?,?)");
		while(testData.hasNext())
		{
			RatingRecord rating = testData.getNext();
			int u=rating.user;
			int i=rating.item;
//			double p=rating.pref;
			double predicted = model.predict(u, i);
			pst.setInt(1, u+1);
			pst.setInt(2, i+1);
			pst.setDouble(3, predicted);
			pst.executeUpdate();
//			double error=(p-predicted)*(p-predicted);
//			sum+=error;	
//			n++;
		}
//		double rmse=Math.sqrt(sum/n);
//		System.out.println("test rmse:"+rmse);
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		long t1 = System.currentTimeMillis();
		SvdTrainer trainer = new SvdTrainer("dataset/u1.base");
		trainer.train(1000);
		trainer.test("dataset/u1.test");
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
/*		BufferedWriter bw = new BufferedWriter(new FileWriter("dataset/u1.test"));
		Connection conn = DBUtil.getConn();
		PreparedStatement pst =conn.prepareStatement("select userid,movieid,score from test1");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score;
		while(rs.next()){
			userid = rs.getInt(1);
			movieid = rs.getInt(2);
			score = rs.getInt(3);
			bw.write(userid+"\t"+movieid+"\t"+score+"\n");
		}
		bw.close();
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}*/
	}

}
