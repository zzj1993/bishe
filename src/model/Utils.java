/*
* Class with methods which will be used in the main program. General methods for use.
* 
* @author Daniel Paul
* @version 1.0
* @date 12/12/14
*/
package model;
import java.io.*;

public class Utils
{

  /*
  * Method to convert an array of strings with values seperated by spaces into a 2d array.
  *
  * @param Sting[] array - an array of strings with each data seperated by space
  * @param int width - size of array to create
  * @return data of 2d int array
  */
  public static int[][] data2Array(String[] array, int width)
  {
    int[][] data = new int[array.length][width];

    // loop through each array position
    for(int i = 0; i < array.length; i++)
    {
      String[] spaceSplit = array[i].split("\\s+"); // split space

      // save all values to 2d data array
      for(int j = 0; j < spaceSplit.length; j++)
      {
        data[i][j] = Integer.parseInt(spaceSplit[j]);
      }

    } // END for each values in inputs loop

    return data;
  }

  /* If no width is specified, use the size of the first data values */
  public static int[][] data2Array(String[] array)
  {
    int width = array[0].split("\\s+").length;
    return data2Array(array, width);
  }

  public static String[][] stringData2Array(String[] array)
  {
    int width = array[0].split("\\t+").length;
    String[][] data = new String[array.length][width];
    for(int i = 0; i < array.length; i++) // loop through each array position
    {
      String[] spaceSplit = array[i].split("\\t+"); // split space
      for(int j = 0; j < spaceSplit.length; j++) data[i][j] = spaceSplit[j]; // save all values to 2d data array
    } // END for each values in inputs loop
    return data;
  }
  

  /*
  * Method to check if a double number is NaN (not a number)
  *
  * @param a double number to check
  * @return boolean. true if is NaN
  */
  public static boolean isNaN (double x)
  {
    return x != x;
  }

  /*
  * Method to round up a double number to x decimal places
  * http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
  *
  * @param double value - the number to round
  * @param int places - number of place to round up to
  * @return double number of the rounded value
  */
  public static double round(double value, int places)
  {
    // check if places to round up to is less than 0. Can't round to a negative number.
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);

    return (double) tmp / factor;
  }




  /*
  * Method saves a double array data to a file
  */
  public static void saveToFile(String file, double[] array) throws FileNotFoundException, IOException {

    File aFile = new File(file); 
    Writer output = null;
    try {
      output = new BufferedWriter( new FileWriter(aFile) );
      for(int i=0;i<array.length;i++){
        output.write( Double.toString(array[i]) );
        // output.write(System.getProperty("line.separator"));
        if(i != array.length - 1) output.write(", "); // comma seperated list
      }
    }
    finally {
      if (output != null) output.close();
    }
  }
  
}
