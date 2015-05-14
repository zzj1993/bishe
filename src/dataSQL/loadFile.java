package dataSQL;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class loadFile {
//	private static int prefer[][]=new int[1000][2000];
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
/*	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		long t1 = System.currentTimeMillis();
		loadFile lf = new loadFile();
		lf.getAllUserRating();
		long t2 = System.currentTimeMillis();
		System.out.println("end"+(t2-t1)/1000+"秒");
	}*/
	
	public static Connection getConn() throws ClassNotFoundException, SQLException{
		Connection conn=null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/data", "root", "123456");		
//		System.out.println("ok");
		return conn;
	}

	
	/**基于用户的协同过滤
	 * 1、首先读取当前用户评价过的信息
	 * 2、找到有共同评分项目的用户，计算用户相似度（通过相同项目）
	 * 3、对TopK用户的非共同项目进行推荐
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	/*public void readCurUser(int userID) throws ClassNotFoundException, SQLException{		
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select movieid,score,timestamp from base where userid=?");
		pst.setInt(1, userID);
		ResultSet rs = pst.executeQuery();
		int movieid,score,timestamp;
		while(rs.next()){
			movieid = rs.getInt(1);
			score = rs.getInt(2);
			timestamp = rs.getInt(3);
			prefer[userID][movieid] = score;
//			System.out.println(movieid+" "+score+" "+timestamp);
		}
	}*/
	
	//训练集
	public int[][] loadMovieLensTrain() throws ClassNotFoundException, SQLException, IOException{
		int prefer[][]=new int[1000][2000];
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from base");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
//			pw.write(userid+"\t"+movieid+"\t"+score+"\n");
			prefer[userid][movieid] = score;
		}
//		pw.close();
		return prefer;
	}
	//测试集
	public int[][] loadMovieLensTest() throws ClassNotFoundException, SQLException{
		int prefer[][]=new int[1000][2000];
		
		Connection conn = getConn();
		PreparedStatement pst = conn.prepareStatement("select * from test where id<=1000");
		ResultSet rs = pst.executeQuery();
		int userid,movieid,score,timestamp;
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			timestamp = rs.getInt(5);
			
			
			prefer[userid][movieid] = score;
		}
		return prefer;
	}
	
	
	//计算用户之间的相似度
	public double sim_user_pearson(int prefer[][],int user1,int user2){
		ArrayList<Integer> sim = new ArrayList<Integer>();
		int i=0;
		
		//两数组已经排好序，不用用双重for求他们的交集(10多ms)，hash碰撞最快，0~1ms
		HashMap<Integer,Integer> ht = new HashMap<Integer,Integer>();
		for(i=0;i<prefer[user1].length;i++){
			if(prefer[user1][i]!=0){
				ht.put(i,prefer[user1][i]);
			}	
		}		
		for(i=0;i<prefer[user2].length;i++){
			if(ht.containsKey(i)){
				sim.add(i);
			}	
		}
//		System.out.println(ht.size());
		if(sim.size()==0)//相同评价项目个数
			return -1;

		//所有偏好之和，所有都用double型，防止相除时得到整数
		double sum1=0,sum2=0;
		//求平方之和
		double sum1Sq=0,sum2Sq=0;
		//求乘积之和 ∑XiYi
		double sumMulti = 0;
		double num1=0.0;
		double num2=0.0;
		double result=0.0;
//		for(i=0;i<sim.size();i++){//通过计算两用户的共同评分项目计算相似度
		for(int item:sim){
			
			sum1 += prefer[user1][item];//所有偏好之和
			sum2 += prefer[user2][item];
			
			sum1Sq += Math.pow(prefer[user1][item], 2);//求平方之和
			sum2Sq += Math.pow(prefer[user2][item], 2);
			
			sumMulti += prefer[user1][item]*prefer[user2][item];			
		}
		
		num1 = sumMulti - (sum1*sum2/sim.size());
		num2 = Math.sqrt( (sum1Sq-Math.pow(sum1,2)/sim.size())*(sum2Sq-Math.pow(sum2,2)/sim.size()));  
		if(num2==0)                                                
			return 0;  

		result = num1/num2;
		
//		System.out.println("result="+result);
//		System.out.println(user1+"\t"+user2+"\t"+sum1+"\t"+sum2+"\t"+sum1Sq+"\t"+sum2Sq+"\t"+sumMulti+"\t"+num1+"\t"+num2+"\t"+result);
		return result;
	}
	
	//获取对item评分的K个最相似用户
	public List<Map.Entry<Integer, Double>> topKMatches(int prefer[][],int user,int itemID,int k){
		long t1 = System.currentTimeMillis();
		Set<Integer> userSet = new HashSet<Integer>();
		//找出所有prefer中评价过item的用户，存入userSet
		for(int i=0;i<prefer.length;i++){
			for(int j=0;j<prefer[i].length;j++){
				if(prefer[i][j]!=0&&j==itemID){
//					System.out.println("i="+i);
					userSet.add(i);//用户i评论过项目itemID
				}
			}			
		}
		long t2 = System.currentTimeMillis();
		System.out.println("top"+(t2-t1)+"ms");
		System.out.println("userSetsize="+userSet.size());
		//相似度-用户，应该不会有相似度一样的用户，不重复
		Map<Integer,Double> userSim = new HashMap<Integer,Double>();
		double sim=0.0;
//		long t1 = System.currentTimeMillis();
		//计算相似性
		for(int other:userSet){
			if(other!=user){
				sim = sim_user_pearson(prefer,user,other);
				userSim.put(other,sim);
			}
		}
//		sim = sim_user_pearson(prefer,1,2);
//		System.out.println("userSim1="+userSim);
//		long t2 = System.currentTimeMillis();
//		System.out.println("topK"+(t2-t1)+"ms");
		List<Map.Entry<Integer, Double>> userSim_sort = new ArrayList<Map.Entry<Integer, Double>>(userSim.entrySet());
	    Collections.sort(userSim_sort, new Comparator<Map.Entry<Integer, Double>>() {//根据value排序
	        public int compare(Map.Entry<Integer, Double> o1,
	          Map.Entry<Integer, Double> o2) {
	         double result = o2.getValue() - o1.getValue();
	         if(result > 0)
	         	return 1;
	         else if(result == 0)
	         	return 0;
	         else 
	         	return -1;
	        }
	       });
	
//	    System.out.println("userSim_sort="+userSim_sort);    
		//相似度从小到大排列，所以要取后K个
		if(userSim_sort.size()<=k){//如果小于k，只选择这些做推荐
			return userSim_sort;
		}else{//如果大于k，选择评分最高的用户
			userSim_sort = userSim_sort.subList(0, k);
//			System.out.println("userSim_sort1="+userSim_sort);
//			System.out.println("userSim2="+userSim);
			return userSim_sort;
		}
//		System.out.println("topKUser="+topKUser);
	}
	
	//计算用户的平均评分
	public double getAverage(int prefer[][],int userID){
		
		double count=0.0,sum=0.0;
		
		//这里不能用for(int item:prefer[userID])
		for(int item=0;item<prefer[userID].length;item++){			
			if(prefer[userID][item]!=0)
			{
				sum += prefer[userID][item];
				count++;
			}
		}
//		System.out.println(sum+"\t"+count+"\t"+sum/count);
		return (double)sum/count;
	}
	
	//平均加权策略，预测userID对itemID的评分
	public double getRating(int prefer[][],int userID,int itemID,int k){
		double avgOther=0.0;
		double simSums=0.0;
		double weightAvg=0.0;//加权平均
		int otherid;
		double similarity;
		
		List<Map.Entry<Integer, Double>> userSim = topKMatches(prefer,userID,itemID,k);
		Iterator<Map.Entry<Integer, Double>> it = userSim.iterator();
		Entry<Integer, Double> entry;
//		System.out.println("userSim="+userSim);
//		System.out.println(userID+"\t"+445+"\t"+userSim.get(447));
		while(it.hasNext()){
			entry = it.next();
			otherid =entry.getKey();
			similarity = entry.getValue();
			
			if(otherid!=userID){
				avgOther = getAverage(prefer,otherid);//待比较用户的平均分
				//累加
				simSums += Math.abs(similarity);
				weightAvg += (prefer[otherid][itemID]-avgOther)*similarity;
			}
		}
		
		double avgUser = getAverage(prefer,userID);
	
		if(simSums==0)
			return avgUser;
		else 
			return (avgUser+weightAvg/simSums);
	}
	
	public void getAllUserRating() throws ClassNotFoundException, SQLException, IOException{
		int prefer1[][] = loadMovieLensTrain();
		int prefer2[][] = loadMovieLensTest();
		
		PrintWriter pw = new PrintWriter(new FileWriter("..\\1.test"));
		
		double rating = 0.0;
//		pw.write(1+"\t"+1+"\t"+rating+"\n");
		for(int i=0;i<prefer2.length;i++){
			for(int j=0;j<prefer2[i].length;j++){
				if(prefer2[i][j]!=0){
					rating = getRating(prefer1,i,j,20);
					pw.write(i+"\t"+j+"\t"+rating+"\n");
					System.out.print(i+"\t"+j+"\t"+rating+"\n");
				}
			}
		}
		pw.close();
	}
	/**基于项目的协同过滤（用户-共同项目评分计算用户相似度，项目-共同用户评分计算项目相似度）
	 *1、读取评价过该项目的所有用户，
	 *2、计算项目的相似度
	 *3、推荐最相似的K个物品
	 */
	
	
}
