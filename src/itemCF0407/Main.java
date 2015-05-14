package itemCF0407;
/**
 * Map<int,Map>ʵ��
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//35 000ms����
public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		long t1 = System.currentTimeMillis();
		DBUtil db = new DBUtil();
		Recommend re = new Recommend();
		ItemCF uc = new ItemCF();

//		int len=0;
		Map<Integer,Map<Integer,Integer>> mm = db.loadMovieLensTrain();

//		double sim = uc.sim_item_pearson(mm,1, 2);//0~10ms
//		System.out.println("sim="+sim);
//		double avg = re.getAverage(mm, 1);//0~10ms
//		System.out.println("avg="+avg);
//		List<Map.Entry<Integer, Double>> list = uc.topKMatches(mm, 10, 1, 20);//60~70ms 
//		System.out.println("list="+list);
//		System.out.println(re.getRating(mm, 10, 1, 20));//40~60ms

		//Ϊһ���û�Ԥ��ʣ����Ŀ��д���ı�������541ms��д�����ݿ�������24020ms
		//����ֻ�ܽ�����������ݿ��У�compare��Ҫ�õ�
		for(int i=1;i<=5;i++){
			re.getAllUserRating(i, 1, 1);
		}
		
		long t2 = System.currentTimeMillis();
		System.out.println((t2-t1)+"ms");
	}
}
