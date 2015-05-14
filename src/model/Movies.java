/*
* Class with all methods to read and do calculations based on the ratings data.
*
* @author Daniel Paul
* @version 1.0
* @date 14/12/14
*
*/
package model;
import java.util.*;

public class Movies
{

  // read in ratings data and store in a 2d array
  static FileIO reader = new FileIO();
  static int[][] ratings = Utils.data2Array(reader.load("data/Ratings.txt")); // userId, movieId, rating
  static String[][] movieData = Utils.stringData2Array(reader.load("data/Movies.txt"));

  /*
  * Method to find all the movies a user has rated.
  *
  * @param int userId - user ID of a user
  * @return an array list with int arrays of the users rating data
  */
  public static ArrayList<int[]> userRatings(int userId)
  {
    ArrayList<int[]> userRatings = new ArrayList<int[]>();

    // loop though ratings and find all of userId's ratings
    for(int i = 0; i < ratings.length; i++)
    {
      if(ratings[i][0] == userId) userRatings.add(ratings[i]);
    }

    return userRatings;
  }


  /*
  * Method to find the average rating a user has given all the movies they have rated
  *
  * @param int userId
  * @return average rating the user gives movies
  */
  public static double averageUserRating(int userId)
  {
    ArrayList<int[]> moviesRated = userRatings(userId); // all the movies the user has rated
    int noOfMoviesRated = moviesRated.size();

    if(noOfMoviesRated == 0) return 0; // user has not rated any movies

    // loop and add add the ratings they have given movies
    int totalRating = 0;
    for(int i = 0; i < noOfMoviesRated; i++) totalRating += moviesRated.get(i)[2];

    return totalRating / noOfMoviesRated; // average of ratings
  }


  /*
  * Method to find all ratings data for a particular movie
  *
  * @param int movieId
  * @return an array list of int arrays with ratings data for this movie
  */
  public static ArrayList<int[]> movieRatings(int movieId)
  {
    ArrayList<int[]> list = new ArrayList<int[]>();

     // go through the ratings data
    for(int i = 0; i < ratings.length; i++)
    {
      // if the rating belongs to the moive we are looking for add to our list
      if(ratings[i][1] == movieId) list.add(ratings[i]);
    }

    return list;
  }


  /*
  * Method to find the average rating of a movie.
  *
  * @param int movieId
  * @return the average movie rating of the movie.
  */
  public static double averageMovieRating(int movieId)
  {
    ArrayList<int[]> movieRatings = movieRatings(movieId); // all ratings for this movie

    double ratingsTotal = 0; // variable to store the sum of all the ratings belonging to the movie
    int numberOfRatings = movieRatings.size(); // how many people have rated this movie

    if(numberOfRatings == 0) return 0; // if no one has rated the movie before

    // loop and add ratings belonging to this movie
    for(int i = 0; i < movieRatings.size(); i++) ratingsTotal += movieRatings.get(i)[2]; // add the rating

    return ratingsTotal / numberOfRatings; // average
  }


  /* 
  * Method to find same movies from two arrayLists have the same movie [movieId, user1 rating, user2 rating] 
  *
  * @param an int[] array list of user1
  * @param an int[] array list of user2
  * @return array list of int arrays in the format [movieId, user1 rating, user2 rating]
  */
  public static ArrayList<int[]> findSameMovies(ArrayList<int[]> user1Ratings, ArrayList<int[]> user2Ratings)
  {
    // plot of x, y values. Where x is the rating user1 gave and y is the rating user2 gave for the same movie
    ArrayList<int[]> list = new ArrayList<int[]>();

    for(int i = 0; i < user1Ratings.size(); i++)
    {      
      int movieId = user1Ratings.get(i)[1]; // get current movieID from user1's ratings

      // check if movieId is in user2's ratings
      for(int j = 0; j < user2Ratings.size(); j++)
      {
        if(movieId == user2Ratings.get(j)[1])
        {
          // same movie found so add it to list
          int[] value = {movieId, user1Ratings.get(i)[2], user2Ratings.get(j)[2]}; // [movieId, user1 rating, user2 rating]
          list.add(value);
          break; // break inner loop and check for other movies
        }
      } // END inner for loop

    } // END outer for loop

    return list;
  }



  /*
  * Filter an ArrayList<int[]> by a data column
  *
  * @param ArrayList<int[]> to filter
  * @param int col - array position number to compare
  * @param int find - what to keep in new list
  * @return ArrayList<int[]> with only the filtered values
  */
  public static ArrayList<int[]> filterList(ArrayList<int[]> list, int col, int find)
  {
    ArrayList<int[]> newList = new ArrayList<int[]>();

    // loop through each values in the list
    for(int i = 0; i < list.size(); i++)
    {
      // check if meets our condition and add to new list
      if(list.get(i)[col] == find) newList.add(list.get(i));
    }

    return newList;
  }



  /*
  * Find genre of a movie
  *
  * @param int movieId
  * @return int genreId
  */
  public static String[] getGenres(int movieId)
  {
    String[] movie = movieData[movieId - 1]; // get the movie's row
    return Arrays.copyOfRange(movie, 4, movie.length); // return just the genres section
  }


  /*
  * Check if two movies have atlest one genre in common
  *
  * @param movie1's ID
  * @param movie2's ID
  * @return true or false
  */
  public static boolean hasSameGenre(int movie1, int movie2)
  {
    String[] movie1Genres = getGenres(movie1), movie2Genres = getGenres(movie2);
    for(int i = 0; i < movie1Genres.length; i++)
    {
      if(movie1Genres[i] != null && movie2Genres[i] != null && movie1Genres[i].equals("1") && movie2Genres[i].equals("1")) return true;
    }
    return false;
  }



  /*
  * Method to get only movies in same genre.
  *
  * @param ArrayList<int[]> of movies to filter
  * @param int movieId of movie to filter other movies with
  * @return ArrayList<int[]> of movies that only have atlease one same genre
  */
  public static ArrayList<int[]> filterMoviesInSameGenre(ArrayList<int[]> moviesList, int movieId)
  {
    ArrayList<int[]> newList = new ArrayList<int[]>();

    for(int i = 0; i < moviesList.size(); i++)
    {
      // check if movieId from list and movie comparing to have same genre
      if(hasSameGenre(moviesList.get(i)[0], movieId)) newList.add(moviesList.get(i));
    }

    return newList;
  }




  /*
  * Method to predict the rating a user would give a movie.
  *
  * @param int userId
  * @param int movieId
  * @return predicted rating userId would give movieId
  */
  public static double predictRating(int userId, int movieId)
  {

    double prediction = 0, averageMovieRating = averageMovieRating(movieId);

    // All the users who have rated this movie & All the movies current user (userId) has rated
    ArrayList<int[]> ratedUsers = movieRatings(movieId), usersRatings = userRatings(userId);

    
    // get users of the same gender only
    ArrayList<int[]> sameGenderUsers = new ArrayList<int[]>();
    for(int i = 0; i < ratedUsers.size(); i++)
    {
      if(Users.isSameGender(userId, ratedUsers.get(i)[0])) sameGenderUsers.add(ratedUsers.get(i)); // if same gender add to new list
    }
    if(sameGenderUsers.size() > 4) ratedUsers = sameGenderUsers; // if there are atleast 5 users, then take gender into consideration



    // filter by occupation if there are enough numbers
    ArrayList<int[]> sameOccupationUsers = new ArrayList<int[]>();
    for(int i = 0; i < ratedUsers.size(); i++)
    {
      if(Users.isSameOccupation(userId, ratedUsers.get(i)[0])) sameOccupationUsers.add(ratedUsers.get(i));
    }
    // if there are atleast 5 users with same occupation, then take occupation into consideration
    if(sameOccupationUsers.size() > 4) ratedUsers = sameOccupationUsers;




    // check if users have rated this movie
    if(ratedUsers.size() > 0)
    {

      int totalUsersCompared = 0; // total number of users we have compared to find average prediction
      double totalP = 0; // total prediction

      // loop through each users who have rated the movie and compare with user to find prediction based on this user
      for(int i = 0; i < ratedUsers.size(); i++)
      {
        
        // find all movies rated by user2
        ArrayList<int[]> user2Ratings = userRatings(ratedUsers.get(i)[0]);

        // check if they both users have rated a same film and get list of each film and their ratings [movieId, x, y]
        ArrayList<int[]> sameMovieIDs = findSameMovies(usersRatings, user2Ratings);

        if(sameMovieIDs.size() == 0) continue; // no same movie found, so skip comparing with user


        // filter by genre - compare only movies of same genre with other user if there are same movies
        ArrayList<int[]> sameGenreMovies = filterMoviesInSameGenre(sameMovieIDs, movieId);
        if(sameGenreMovies.size() > 4) sameMovieIDs = sameGenreMovies; // only filter by genre if there are atlest 5 movies to plot graph with


        double user2ratingOfMovie = 0; // what rating user2 has given this movie

        // formula values
        int n = sameMovieIDs.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        double sumY2 = 0;

        // loop and add up totals
        for(int j = 0; j < n; j++)
        {
          int[] current = sameMovieIDs.get(j);
          int x = current[1];
          int y = current[2];

          if(current[0] == movieId) user2ratingOfMovie = current[2]; // user2's rating

          sumXY += x * y;
          sumX += x;
          sumY += y;
          sumX2 += Math.pow(x, 2);
          sumY2 += Math.pow(y, 2);
        }

        double aTop = (sumY * sumX2) - (sumX * sumXY);
        double aBottom = (n * sumX2) - Math.pow(sumX, 2);
        double a = aTop / aBottom;

        double bTop = (n * sumXY) - (sumX * sumY);
        double bBottom = (n * sumX2) - Math.pow(sumX, 2);
        double b = bTop / bBottom;

        double y = a + (b * user2ratingOfMovie); // prediction based on user2

        // check if predicted value is a number
        if(!Utils.isNaN(y)) {
          totalUsersCompared++;
          totalP += y;
        }
         
      }

      totalP = totalP / totalUsersCompared; // get average of all predictions
      prediction = totalP;

    }
    else // no one has rated this movie
    {
      // find the average movie rating of user and use that as prediction
      prediction = averageUserRating(userId);
    }

    // if prediction is close to 0, return 1 as you can't have a zero rating
    if(Math.round(prediction) == 0) return 1;

    return prediction;
  } // END predictRating()




  /*
  * Method to find correlation (r) between movies of user1 and user2
  *
  * @param int user1's ID
  * @param int user2's ID
  * @return r
  */
  public static double findCorrelation(int user1, int user2)
  {
    // find all movies rated by user1 and user2
    ArrayList<int[]> user1Ratings = userRatings(user1), user2Ratings = userRatings(user2);

    // check if they both users have rated a same film and get list of each film and their ratings [movieId, x, y]
    ArrayList<int[]> sameMovieIDs = findSameMovies(user1Ratings, user2Ratings);

    if(sameMovieIDs.size() == 0) return -100; // no same movie found, so return this value and handle where it is got back

    // --- apply formula ---

    int n = sameMovieIDs.size();

    double sumXY = 0;
    double sumX = 0;
    double sumY = 0;
    double sumX2 = 0;
    double sumY2 = 0;

    // loop and add up totals
    for(int i = 0; i < n; i++)
    {
      int[] current = sameMovieIDs.get(i);
      int x = current[1];
      int y = current[2];

      sumXY += x * y;
      sumX += x;
      sumY += y;
      sumX2 += Math.pow(x, 2);
      sumY2 += Math.pow(y, 2);
    }

    // test vales. ans = 0.529809
    /* sumXY = 20485; sumX = 247; sumY = 486; sumX2 = 11409; sumY2 = 40022; n = 6; */
    // http://www.statisticshowto.com/what-is-the-pearson-correlation-coefficient/
    // http://www.mathsisfun.com/data/correlation.html


    // apply equation
    double topEq = (n * sumXY) - (sumX * sumY);
    double bottomEq =  Math.sqrt( ( (n * sumX2) - Math.pow(sumX, 2)) * ( (n * sumY2) - Math.pow(sumY, 2)) );
    double r = topEq / bottomEq;

    return r; 
  } // END findCorrelation()


}
