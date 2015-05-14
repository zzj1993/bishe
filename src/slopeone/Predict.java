/* Renata Ghisloti Duarte de Souza */
package slopeone;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


public class Predict{

    Map<Integer,Float> itemRating = new HashMap<Integer, Float>();
    HashMap<Integer,Float> predictions = new HashMap<Integer,Float>();
    /* User to whom the prediction will be made */
//    int targetUser = 2;//userID
//    float mDiff[][];
//    float mFreq[][];
    Table<Integer,Integer,Integer> usersMatrix = TreeBasedTable.create();//user-movie-rating
    Table<Integer,Integer,Double> mdiff = TreeBasedTable.create();//movie-movie-diff
    Table<Integer,Integer,Integer> mfreq = TreeBasedTable.create();//movie-movie-freq
    /* Contains the maximun number of items */
    int maxItem;

    public static void main(String args []) throws IOException, ClassNotFoundException, SQLException{
        long start = System.currentTimeMillis();

//        for(int i=1;i<=5;i++){
        	Predict newPrediction = new Predict(1);
//        }
        /* Estimates time */
        long end = System.currentTimeMillis();
        System.out.println("\nExecution time was "+(end-start)+" ms.");
    }

    public Predict(int targetUser) throws IOException, ClassNotFoundException, SQLException{
    	FileOutputStream output = new FileOutputStream("dataset/predict.txt");
        
//        getItemRating(targetUser);//1、
//        readDiffs();//2、得到mDiff[][]和mFreq[][]   	
        SlopeOne slope = new SlopeOne();
        itemRating = slope.readUsersMatrix().row(targetUser);
        
        Object[] a = slope.buildDiffMatrix();
        mdiff = (Table<Integer, Integer, Double>) a[0];
        mfreq = (Table<Integer, Integer, Integer>) a[1];
        
        maxItem = slope.getMaxItemId();;
        float totalFreq[] =  new float [maxItem+1];//已评分的物品也放进去

        /* 3、Start prediction */
        for (int j=1; j <= maxItem; j++) {
            predictions.put(j,0.0f);
            totalFreq[j] = 0;
        }

        for (int j : itemRating.keySet()) {//用户2评过的项目
            for (int k = 1; k <= maxItem; k++) {
                if( j != k) {
                    /* Only for items the user has not seen */
                    if(!itemRating.containsKey(k)){
                        float newVal = 0;
                        if(k < j) {
//                            newVal = mFreq[j][k] * (mDiff[j][k] + itemRating.get(j).floatValue());
                        	newVal = (float) (mfreq.get(j, k) * (mdiff.get(j, k) + itemRating.get(j).floatValue()));
                        }
                        else {//因为只保存了对角线下半部分的，上半部分只要乘以-1即可
//                            newVal = mFreq[j][k] * (-1 * mDiff[j][k] + itemRating.get(j).floatValue());
                        	newVal = (float) (mfreq.get(j, k) * (-1 * mdiff.get(j, k) + itemRating.get(j).floatValue()));
                        }
                        totalFreq[k] = totalFreq[k] + mfreq.get(j, k);
                        predictions.put(k, predictions.get(k).floatValue() + newVal);
                    }
                } 
            }
        }

        /* Calculate the average */
        for (int j : predictions.keySet()) {
            predictions.put(j, predictions.get(j).floatValue()/(totalFreq[j] ));
        }

        /* Fill the predictions vector with the already known rating values */
        /*for (int j : itemRating.keySet()) {
            predictions.put(j, itemRating.get(j));
        }*/

        /* Print predictions */
        System.out.println("\n" + "#### Predictions #### ");
        Connection conn = DBUtil.getConn();
        PreparedStatement pst = conn.prepareStatement("insert into slopeone(userid,movieid,score) values(?,?,?)");
        
        for (int j : predictions.keySet()) {
            System.out.println( j + " " + predictions.get(j).floatValue());
            output.write((j + " " + predictions.get(j).floatValue()+"\n").getBytes());
        	if(!Double.isNaN (predictions.get(j).doubleValue())){
        		pst.setInt(1, targetUser);
    			pst.setInt(2, j);//itemid=i
    			pst.setDouble(3, predictions.get(j).doubleValue());
    			pst.executeUpdate();
        	}
            
        }
        if(pst!=null){
			pst.close();
		}
        if(conn!=null){
			conn.close();
		}
    }

    /* 2、
     * Function readDiff()
     * Read the precalculated Diffs between items
     *
     */
/*    public void readDiffs(){

        File foutput = new File("dataset/slope-intermidiary-output.txt");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;

        try {
            fis = new FileInputStream(foutput);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            String line = dis.readLine();
            StringTokenizer t = new StringTokenizer(line, "\t");
            maxItem = Integer.parseInt(t.nextToken());
            mDiff = new float[maxItem + 1][maxItem + 1];
            mFreq = new float[maxItem + 1][maxItem + 1];

            for(int i = 1; i <= maxItem; i++)
              for(int j = 1; j <= maxItem; j++){
                mDiff[i][j] = 0;
                mFreq[i][j] = 0;
              }

            System.out.println("\n" + "#### Diffs #### ");

            while(dis.available() != 0){

                line = dis.readLine();
                t = new StringTokenizer(line, "\t");
                int itemID1 = Integer.parseInt(t.nextToken());
                int itemID2 = Integer.parseInt(t.nextToken());

                mDiff[itemID1][itemID2] = Float.parseFloat(t.nextToken());//得到两个项目的平均评分差

                line = dis.readLine();
                t = new StringTokenizer(line, "\t");
                itemID1 = Integer.parseInt(t.nextToken());
                itemID2 = Integer.parseInt(t.nextToken());

                mFreq[itemID1][itemID2] = Float.parseFloat(t.nextToken());//得到两个项目的共同用户(评分)数
            }

            fis.close();
            bis.close();
            dis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*1、
     * Function getUser()
     * Get already known user preferences
     * 得到userID的已有评分
     */
/*    public  void getItemRating( int userID ) throws ClassNotFoundException, SQLException{
    	Connection conn = DBUtil.getConn();
    	conn.prepareStatement("");
    	
    }*/

}
