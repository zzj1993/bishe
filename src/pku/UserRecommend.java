package pku;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserRecommend {

	/**
	 * ��ʼ��
	 */
	String[] films={"������","һ��֮ң","�����칬","������","��̽���ʽ�"};
	static String[] users={"aa","bb","cc","dd"};
	Map scores = (HashMap)initScore();
	
	/**
	 * ��ʼ���û�-��Ӱ-����
	 * @return �����û�-��Ӱ-������Map
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
	 * ���������û������ƶ�
	 * @param user1 �������û��Ƚϵ�
	 * @param user2 �����û�
	 * @return ���Ƶķ���
	 */
	public double getLikelyScore(String user1,String user2){
		double total=0.0;
		int x1,x2,y1,y2;
		double a;
		//�õ���Ӱ-���ֵ�ӳ��
		Map u1filmscores = (HashMap) scores.get(user1);
		Map u2filmscores = (HashMap) scores.get(user2);
		//u1��Ϊ�ο�������u1û���˾Ͳ����ټ�����
		//�õ�hashmap����ȫ��key�ļ��ϣ�Ȼ��ȡ���ظ�Ԫ��
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
	 * �����ÿ���û������ƶ�
	 * @param user ��������Ƚϵ��Ǹ��û�
	 */
	public void nearUser(String user){
	
		for(String tempUser:users){
			if(tempUser.equalsIgnoreCase(user)){//�Լ����Լ��Ͳ��Ƚ��ˣ�������ǰѭ��������һ��ѭ��
				continue;
			}
			//�õ������û������ƶȲ����
			double score = getLikelyScore(user,tempUser);//ָ���û��������û��Ƚ�
			System.out.println(user+"��"+tempUser+" ���ƶȣ�"+score);
		}
	}
	
	public static void main(String[] args) {
		UserRecommend ur = new UserRecommend();
		ur.nearUser(users[0]);
	}

}
