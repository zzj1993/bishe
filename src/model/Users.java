/*
* Class with all methods to get information from user's data
*
* @author Daniel Paul
* @version 1.0
* @date 15/12/14
*
*/
package model;
import java.util.*;

public class Users
{

  static FileIO reader = new FileIO();
  static String[][] users = Utils.stringData2Array(reader.load("data/Users.txt"));

  /*
  * Get a user's gender
  *
  * @param int userId
  * @return "M" or "F"
  */
  public static String getGender(int userID)
  {
    String[] user = users[userID - 1];
    return user[2];
  }


  /*
  * Check if two users are of same gender
  *
  * @param user1ID
  * @param user2ID
  * @return true or false
  */
  public static boolean isSameGender(int user1, int user2)
  {
    return (getGender(user1) == getGender(user2));
  }


  /*
  * Get a user's occupation
  *
  * @param int userId
  * @return String of occupation
  */
  public static String getOccupation(int userID)
  {
    return users[userID - 1][3];
  }


  /*
  * Check if two users have the same occupation
  *
  * @param user1ID
  * @param user2ID
  * @return true or false
  */
  public static boolean isSameOccupation(int user1, int user2)
  {
    return (getOccupation(user1) == getOccupation(user2));
  }


}