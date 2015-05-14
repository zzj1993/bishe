![AIB Datathon 2014](data/img/header.png)
=================

Creating a movie recommendation model based on an actual history of user’s movie ratings.

## Program Files
- **Main: [PredictRatings.java](PredictRatings.java)**
- [Movies.java](Movies.java) - Contains all methods relating to movies data.
- [Users.java](Users.java) - Methods relating to user's data.
- [Utils.java](Utils.java) - Useful methods for use in the program.
- [FileIO.java](FileIO.java) - For reading input data from files.


## The Dataset
![Database Structure](data/img/database_structure.png)


## Algorithm

Method used to predict rating for a movie that a user could rate:

1. Get a movieID and userID to predict what that movie might be rated by that user.
2. Find all the users who have already rated the movie
3. If other users have rated the movie, then find the correlation of the current user with the user who has rated the movie already based on all the movies they both have commonly rated int he same genre.
	* If there are enough users in the same gender, compare only with users of the same gender.
	* If there are enough users with the same occupation, compare with only these users.
4. Use the correlation to predict what the user would rate the movie.
5. Find the average prediction based on all users who have already rated the movie.


## Running The Program

After compiling all the java files run PredictRatings. Normal use:

````
java PreductRatings 
````

To get predicion for just one user and movie:

````
java PredictRatings [(int) userID] [(int) movieID]
````
So this will get the predicted rating userID 7 will give for movieID 10:

````
java PreductRatings 7 10
````
Saving all the results to a file:

````
java PredictRatings -save fileName.txt
````
