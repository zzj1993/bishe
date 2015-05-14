package itemCF0411;
/**
 * Map<int,Map>实现
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//35 000ms左右
public class Main {
	/**
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
		long t1 = System.currentTimeMillis();
		DBUtil db = new DBUtil();
		Recommend re = new Recommend();
		ItemCF uc = new ItemCF();

//		int len=0;
//		Map<Integer,Map<Integer,List<Double>>> mm = db.loadMovieLensTrain();
//		Map<Integer,List<Integer>> m = mm.get(1);
//		for(Map.Entry<Integer, List<Integer>> entry:m.entrySet()){
//			int userid = entry.getKey();
//			List<Integer> list = entry.getValue();
//			System.out.println(userid+" "+list.get(0)+" "+list.get(1));
//		}
//		double sim = uc.sim_item_pearson(mm,1, 2);//0~10ms
//		System.out.println("sim="+sim);
//		double avg = re.getAverage(mm, 1);//0~10ms
//		System.out.println("avg="+avg);
//		List<Map.Entry<Integer, Double>> list = uc.topKMatches(mm, 10, 1, 20);//60~70ms 
//		System.out.println("list="+list);
//		System.out.println(re.getRating(mm, 10, 1, 20));//40~60ms
		Connection conn = db.getConn();
		PreparedStatement pst = conn.prepareStatement("truncate result1");
		pst.executeUpdate();
		
		//为一个用户预测剩余项目并写到文本中用了541ms，写到数据库中用了24020ms
		//但是只能将结果插入数据库中，compare需要用到
		for(int i=1;i<=5;i++){
			re.getAllUserRating(i, 1, 1);
		}
		
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
		
//		//对时间进行归一化处理
//		double t = (892910860-a[1])/(a[0]-a[1]);
//		System.out.println(1.0/(1+Math.exp(-t)));
		
//		double b = Math.exp(-0.05*(2015-2014));
//		System.out.println(b);
//		int a = 874784337;//893257941，都是1970年的，所以这个时间戳不是现在年份而是距离1970年过了几秒
//		int b = a/(60*60*24*365);
		  //Date或者String转化为时间戳
//	      SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	      String date = sdf.format(new Date(874784337*1000L));
////	      System.out.println(date);
//	      long beginDate=1328007600;
//
//
//	      String sd = sdf.format(new Date(893286638*1000L));
//
//	      System.out.println(sd);
//	 
//		System.out.println(Calendar.getInstance().get(Calendar.YEAR));

	}
}
