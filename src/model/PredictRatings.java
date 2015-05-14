/*
* Main program to find prediction for 1000 movies given by 100 users for 10 films.
*
* @author Daniel Paul 
* @version 1.0
* @date 10/12/12


* Usage from terminal:
  - Normal use:
    * java PredictRatings
  - To get prediction for just one user and movie:
    * java PredictRatings [(int) userID] [(int) movieID]
      java PredictRatings 1 2
  - This will write the results to a txt file:
    * java PredictRatings -save [fileName.txt]



* Test.txt file contains userID, movieId.
* Need to predict what rating this user would give the movie.

* Algorithm:
  - Get a movieID and userID to predict what that movie might be rated by that user
  - Find all the users who have already rated the movie
  - if(other users have rated the movie) 
    {
      if(there are some users with same gender who have rated the movie) get all users of same gender
      if(there are some users with same occupation) filter list by same occupation
      for each user who has reated the movie already
      {
        - find the correlation of the current user with this user based on all the movies they have rated in common in the same genre
        - use the correlation to predict what our user would rate the movie
      }
      - find the averate prediction based on all the users
    }
    - if(no one has rated the movie) 
      {
        - Find the averare rating this user has given other movies in the same genre and use that as prediction.
        - if(user hasnt rated any other movies in the same genre) { don't consider the genre }
        - if(the user has rated no movies at all) { generate a random rating }
      }

*/
package model;
import java.util.*;

public class PredictRatings 
{

  static FileIO reader = new FileIO();
  static Movies movies = new Movies();

  // Read test.txt file and save in a reusable format
  static String[] inputs = reader.load("data/Test.txt"); // store each line in an array space
  // populate each array postion with userid and MovieId
  static int[][] test = Utils.data2Array(inputs); // 2d array to store userId, MovieId


  /*
  * Main method to run print out the results
  */
  public static void main(String args[]) 
  {


    // save results to file if requested
    boolean saveToFile = false;
    String fileName = "result.txt";

    if(args.length > 0 && args[0].equals("-save"))
    {
      if(args.length == 2) fileName = args[1];
      saveToFile = true;
    }
    // For manually predicting for one user and a movie
    else if(args.length == 2) // check if arguments passed via command line
    {
      int userID = Integer.parseInt(args[0]);
      int movieID = Integer.parseInt(args[1]);

      // find prediction for what userID would rate movieID
      double prediction = movies.predictRating(userID, movieID);
      System.out.println("Predicted rating by userID " + userID + " for movieID " + movieID + ": " + Utils.round(prediction, 4));

      System.exit(1); // stop the program
    }


    // Find predictions for input data
    double[] predictions = findPredictions(); // array with list of all predictions of test data

    // save to file or print out results to screen
    if(saveToFile)
    {
      try {
        Utils.saveToFile(fileName, predictions);
      } catch (Exception e) {
        System.out.println(e.getClass());
      }

      System.out.println("\n\nResults have been writen to " + fileName);
    }
    else
    {
      printAsList(predictions);
    }

  } // END main()


  /*
  * Method to find predictions for all data in input file
  *
  * @return a double array of all the predictions
  */
  public static double[] findPredictions()
  {

    // variables for percentage bar
    int total = test.length;
    double progressPercentage = 0;
    double increaseBy = 100 / (double)(total * 100);

    double[] results = new double[total];
    for(int i = 0; i < total; i++)
    {
      double prediction = movies.predictRating(test[i][0], test[i][1]);
      results[i] = Utils.round(prediction, 4);

      // update the progress bar
      progressPercentage += increaseBy;
      updateProgress(progressPercentage, i, total);
    }

    return results;
  }


  /*
  * Method to print out the predictions as a comma seperated list
  */
  public static void printAsList(double[] array)
  {
    System.out.println("\r");
    for(int i = 0; i < array.length; i++)
    {
      System.out.print(array[i]);
      if(i != array.length - 1) System.out.print(", ");
    }
  }


  /*
  * Method to print out a progress bar of the program's progress
  *
  * @param double progressPercentage - how much is done
  * @param int current - what is currently done
  * @param int total - total number of things to compute
  */
  public static void updateProgress(double progressPercentage, int current, int total) {
    final int width = 50; // progress bar width in chars

    System.out.print("\r[");
    int i = 0;
    for (; i <= (int)(progressPercentage * width); i++) {
      System.out.print("#");
    }
    for (; i < width; i++) {
      System.out.print(" ");
    }
    System.out.print("] ");
    
    System.out.print(Utils.round(progressPercentage * 100, 2) + "%");
    System.out.print(" | " + current + "/" + total);
  }




} // END public class
