package itemCF0406;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//35 000ms左右
public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		long t1 = System.currentTimeMillis();
		DBUtil db = new DBUtil();
		Recommend re = new Recommend();
		ItemCF uc = new ItemCF();

//		Object itemPrefer[] = db.loadMovieLensTrain();		
//		double sim = uc.sim_item(itemPrefer,1, 2);//0~10ms
//		System.out.println("sim="+sim);
//		double avg = re.getAverage(itemPrefer, 1);//0~10ms
//		System.out.println("avg="+avg);
//		List<Map.Entry<Integer, Double>> list = uc.topKMatches(itemPrefer, 1, 1, 20);//60~70ms 
//		Iterator<Map.Entry<Integer, Double>> it = list.iterator();
//		int userid;
//		double score;
//		while(it.hasNext()){
//			Map.Entry<Integer, Double> entry = it.next();
//			userid = entry.getKey();
//			score = entry.getValue();
//			System.out.println(userid+" "+score);
//		}
//		re.getRating(itemPrefer, 1, 6, 20);//40~60ms
//		int a[]={5,10,20,40,80,160};//相似物品个数
//		int b[]={5,10,15,20};//推荐物品个数
//		for(int i=0;i<a.length;i++){
//			for(int j=0;j<b.length;j++){
//				re.getAllUserRating(1,a[i],b[j]);
//			}
//		}
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("truncate result1");
		pst.executeUpdate();
		for(int i=1;i<=5;i++){//预测10个用户（对没有评价过的数据）的评分
			re.getAllUserRating(i,1,1);
		}
//		re.getAllUserRating(1,1,1);//为一个用户预测剩余项目只用了550ms+
	
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		if(pst!=null){
			pst.close();
		}
		
	}
}
