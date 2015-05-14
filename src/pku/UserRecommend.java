package pku;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserRecommend {

	/**
	 * 初始化
	 */
	String[] films={"阿凡达","一步之遥","大闹天宫","神都龙王","神探狄仁杰"};
	static String[] users={"aa","bb","cc","dd"};
	Map scores = (HashMap)initScore();
	
	/**
	 * 初始化用户-电影-分数
	 * @return 返回用户-电影-分数的Map
	 */
	public Map initScore(){
		Map<String,Map> ufscores = new HashMap<String,Map>();
		//aa
		Map<String,Integer> filmscores = new HashMap<String,Integer>();
		filmscores.put(films[0], 9);
		filmscores.put(films[1], 1);
		filmscores.put(films[2], 9);
		filmscores.put(films[3], 7);
		filmscores.put(films[4], 1);
		ufscores.put(users[0], filmscores);
		//bb
		filmscores = new HashMap<String,Integer>();
		filmscores.put(films[0], 2);
		filmscores.put(films[1], 9);
		filmscores.put(films[2], 2);
		filmscores.put(films[3], 5);
		filmscores.put(films[4], 6);
		ufscores.put(users[1], filmscores);
		//cc
		filmscores = new HashMap<String,Integer>();
		filmscores.put(films[0], 9);
		filmscores.put(films[1], 9);
		filmscores.put(films[2], 9);
		filmscores.put(films[3], 3);
		filmscores.put(films[4], 3);
		ufscores.put(users[2], filmscores);
		//dd
		filmscores = new HashMap<String,Integer>();
		filmscores.put(films[0], 4);
		filmscores.put(films[1], 9);
		filmscores.put(films[2], 9);
		filmscores.put(films[3], 4);
		filmscores.put(films[4], 4);
		ufscores.put(users[3], filmscores);
		
		return ufscores;
	}
	
	/**
	 * 计算两个用户的相似度
	 * @param user1 与其他用户比较的
	 * @param user2 其他用户
	 * @return 相似的分数
	 */
	public double getLikelyScore(String user1,String user2){
		double total=0.0;
		int x1,x2,y1,y2;
		double a;
		//得到电影-评分的映射
		Map u1filmscores = (HashMap) scores.get(user1);
		Map u2filmscores = (HashMap) scores.get(user2);
		//u1作为参考，所以u1没有了就不用再计算了
		//得到hashmap里面全部key的集合，然后取出重复元素
		Iterator it = u1filmscores.keySet().iterator();
		while(it.hasNext()){
			String film = (String) it.next();
			x1 = (int)u1filmscores.get(film);			
			y1 = (int)u2filmscores.get(film);
			
			x2 = (int)u1filmscores.get(film);
			y2 = (int)u2filmscores.get(film);
//			System.out.println("x1:"+x1+" x2:"+x2+" y1:"+y1+" y2:"+y2);
			a = x1*x2-y1*y2;
			total += Math.sqrt(Math.abs(a));
		}
		return total;
	}
	
	/**
	 * 输出与每个用户的相似度
	 * @param user 与其他相比较的那个用户
	 */
	public void nearUser(String user){
	
		for(String tempUser:users){
			if(tempUser.equalsIgnoreCase(user)){//自己和自己就不比较了，跳出当前循环进入下一个循环
				continue;
			}
			//得到两个用户的相似度并输出
			double score = getLikelyScore(user,tempUser);//指定用户与其他用户比较
			System.out.println(user+"与"+tempUser+" 相似度："+score);
		}
	}
	
	public static void main(String[] args) {
		UserRecommend ur = new UserRecommend();
		ur.nearUser(users[0]);
	}

}
