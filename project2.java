import java.util.*;

public class project2 {

     // Main method test for n from 10 to 10000
    public static void main(String[] args) 
    { 
        for(int i=10; i<100000; i = i*10) // to check accuracy mark for loop as a comment and do example
        {
            long startT = System.nanoTime();
           // int[][] points = {{1,2},{0,0},{0,2},{5,5},{7,3},{9,2},{4,5},{6,9},{2,4},{4,2}}; //example to check code work
            int[][] points = new int[i][i];
            int[][] ret = divideandconquer(points); 

            /*System.out.println("Convex Hull: ");  //Code to check it works
            for(int i=0; i<ret.length; i++)
            {
                System.out.println("x = " + ret[i][0] + " y = " + ret[i][1]);
            }*/
            long endT = System.nanoTime();
            System.out.println("Alorithm at " + points.length + " takes " + (endT - startT));
        }
    } 

    public static int[][] divideandconquer(int[][] points)
    {   
        // sort the array by the x axis and divive into two new arrays
        Arrays.sort(points,Comparator.comparingInt(a -> a[0])); 
        int[][] left = Arrays.copyOfRange(points,0,points.length/2);
        int[][] right = Arrays.copyOfRange(points, points.length/2, points.length);

        //call convex on each half
        int[][] retRight = convexHull(right);
        int[][] retLeft = convexHull(left);
        
        //keep getting convex hull if the length is too big
        while(retLeft.length < 3)
        {
            retRight = convexHull(retRight); 
        }
        while(retRight.length < 3)
        {
            retLeft = convexHull(retLeft);
        }

        //return the merged result
        return mergeBoth(retLeft, retRight);
    }

    // convex hull using the half sent to it
    public static int[][] convexHull(int[][] points) 
    { 
        //if there is less than 3 points they are all the convex hull
        if(points.length < 3)
        {
            System.out.println("All points are the convex hull");
            return points; 
        }

        int[][] result = new int[points.length][2];
        int retSize = 0;

        // go throught the points and copy the farthest ones
        for (int i = 0; i < points.length; i++) 
        {
            while (retSize >= 2 && smallestLeft(result[retSize - 2], result[retSize - 1], points[i]) == -1) {
                retSize--;
            }
            result[retSize++] = points[i].clone(); // copy into nesult array
        }


        //combine both sides results and sent back
        HashSet<int[]> hull = new HashSet<>(Arrays.asList(result).subList(0, retSize));
  
        int[][] ret = new int[hull.size()][];
        hull.toArray(ret);
        return ret; 
    } 

    // to check correct direction for closest point turning to the left
    public static int smallestLeft(int[] a, int[] b, int[] c)
    {
        int result = (b[1] - a[1]) * (c[0] - b[0]) - (b[0] - a[0]) * (c[1] - b[1]);
        if(result == 0)
        {
            return 0; 
        }
        if(result > 0)
        {   
            return 1;
        }

        return -1;
    }
   
    //using hash combine both list then sent list back
    public static int[][] mergeBoth(int[][] left, int[][] right)
    {
        HashSet<int[]> hull = new HashSet<>(Arrays.asList(right).subList(0, right.length));
        hull.addAll(Arrays.asList(left).subList(0, left.length));

        int[][] ret = new int[hull.size()][];
        hull.toArray(ret);

        //clean up any duplicate points
        Arrays.sort(ret,Comparator.comparingInt(a -> a[0])); 
        int count = 0;
        for(int i=0; i<ret.length-1; i++)
        {
            if((ret[i][0] == ret[i+1][0] && (ret[i][1] == ret[i+1][1])))
            {
                ret[i] = null;
            }
            count++;
        }

        int[][] result = new int[count][2];
        for(int i=0; i<count; i++)
        {
            if(ret[i] != null)
            {
                result[i] = ret[i].clone();
            }
        }


        return result;
    }

} 
  
