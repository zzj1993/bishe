package slopeone;
/* Renata Ghisloti Duarte de Souza */
//Execution time was 407541 ms.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;








import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;


/* Slope One version
 * ****Simple slope one*****
 * Here is presented only the calculation of the diffs matrix (the most expensive part)
 * the predction part is in another file
 */

public class SlopeOne {

    int maxItemsId = 0;    
    Table<Integer,Integer,Float> usersMatrix = TreeBasedTable.create();//user-movie-rating
    Table<Integer,Integer,Double> mdiff = TreeBasedTable.create();//movie-movie-diff
    Table<Integer,Integer,Integer> mfreq = TreeBasedTable.create();//movie-movie-freq

    public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException{
        long start = System.currentTimeMillis();
        SlopeOne so = new SlopeOne();
        so.readUsersMatrix();
        so.buildDiffMatrix();
        so.writeDiffFreq();
        long end = System.currentTimeMillis();
        System.out.println("\nExecution time was "+(end-start)+" ms.");
    }

    //1、
	public Table<Integer,Integer,Float> readUsersMatrix() throws ClassNotFoundException, SQLException, IOException{	
		Connection conn = DBUtil.getConn();
		//这里要按movieid排序，不然下面赋值到数组时会出错，因为if和else的map是承接的
		PreparedStatement pst = conn.prepareStatement("select * from base1 order by userid asc,movieid asc");
		ResultSet rs = pst.executeQuery();
		int userid,movieid;
		float score;
		
		while(rs.next()){
			userid = rs.getInt(2);
			movieid = rs.getInt(3);
			score = rs.getInt(4);
			
			usersMatrix.put(userid, movieid, score);
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
		return usersMatrix;
	}

    /*2、
     * Function buildDiffMatrix()
     * Calculates the DiffMatrix for all items
     *
     */
    public Object[] buildDiffMatrix() {
        /* Iterate through all users, and then, through all items do calculate the diffs */
    	Object[] a = new Object[2];
        for(int cUser : usersMatrix.rowKeySet()){
            for(int i: usersMatrix.columnKeySet()){
                for(int j : usersMatrix.columnKeySet() ){
                	int oldcount = 0;
                	double olddiff = 0.0;
                	double observeddiff = 0.0;
                	if(usersMatrix.get(cUser, i)!=null&&usersMatrix.get(cUser, j)!=null){
                		observeddiff = usersMatrix.get(cUser, i).doubleValue() - usersMatrix.get(cUser,j).doubleValue();
                	}
                    if(mdiff.get(i, j)!=null){
                    	olddiff = mdiff.get(i, j);
                    }
                	mdiff.put(i, j, olddiff+observeddiff);
                    mfreq.put(i, j, oldcount+1);                    
                }
            }
        }
        
        /*  Calculate the averages (diff/freqs) */
        for(int i = 1; i<= maxItemsId; i++){
            for(int j = i; j <= maxItemsId; j++){
                if(mfreq.get(i, j) > 0){
                    mdiff.put(i, j, mdiff.get(i, j) / mfreq.get(i, j));
                }
            }
        }
        a[0] = mdiff;
        a[1] = mfreq;
        return a;
    }
    
    //3、将item-item-Diff和item-item-Freq写入文件
    public void writeDiffFreq() throws ClassNotFoundException, SQLException{
    	Connection conn = DBUtil.getConn();
    	PreparedStatement pst = conn.prepareStatement("insert into difffreq(movieid1,movieid2,diff,freq) values(?,?,?,?)");

        for(int i = 1; i <= maxItemsId; i++){
        	for(int j = i; j <= maxItemsId; j++){
        		if(!Double.isNaN (mdiff.get(i, j))){
                    pst.setInt(1, i);
                    pst.setInt(2, j);
                    pst.setDouble(3, mdiff.get(i, j));
                    pst.setInt(4, mfreq.get(i, j));
                    pst.executeUpdate();
                    
                    System.out.println("insert");
                }else{
                	System.out.println("not insert");
                }
            }
        }//end for
		if(pst!=null){
			pst.close();
		}
		if(conn!=null){
			conn.close();
		}
    }
    
    public int getMaxItemId() throws ClassNotFoundException, SQLException{
    	Connection conn = DBUtil.getConn();
    	PreparedStatement pst = conn.prepareStatement("select max(movieid) from base1");
    	ResultSet rs = pst.executeQuery();
    	while(rs.next()){
    		maxItemsId = rs.getInt(1);
    	}
    	return maxItemsId;
    }

}


