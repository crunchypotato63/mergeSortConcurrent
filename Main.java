import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.random.*;

public class Main {

    public static final long seed = 92;
    public static void main(String[] args) {
        // TODO: Seed your randomizer

        Random random = new Random(seed);

        // TODO: Get array size and thread count from user

        Scanner kb = new Scanner(System.in);
        System.out.print("Array size: ");
        int arraySize = kb.nextInt();
        System.out.print("Thread count: ");
        int numThreads = kb.nextInt();

        kb.close();

        // TODO: Generate a random array of given size

        int numbers[] = new int[arraySize];

        for(int i = 0; i < arraySize; i++){
            numbers[i] = i+1;
        }

        for (int i = arraySize - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }

        // TODO: Call the generate_intervals method to generate the merge 
        // sequence
        
        List<Interval> mergeSequence = generate_intervals(0, arraySize - 1);

        // TODO: Call merge on each interval in sequence

        long SinglestartTime = System.currentTimeMillis();
        for (Interval interval : mergeSequence) {
            merge(numbers, interval.getStart(), interval.getEnd());
        }
        long SingleendTime = System.currentTimeMillis();
        System.out.println("Single-Threaded Runtime: %d", SingleendTime-SinglestartTime);
        

        // Once you get the single-threaded version to work, it's time to 
        // implement the concurrent version. Good luck :)
        
        long ConcurrentstartTime = System.currentTimeMillis();
        //concurrent code here

        long ConcurrentendTime = System.currentTimeMillis();
        System.out.printf("Concurrent Runtime: %d", ConcurrentendTime-ConcurrentstartTime);

    }

    /*
    This function generates all the intervals for merge sort iteratively, given 
    the range of indices to sort. Algorithm runs in O(n).

    Parameters:
    start : int - start of range
    end : int - end of range (inclusive)

    Returns a list of Interval objects indicating the ranges for merge sort.
    */
    public static List<Interval> generate_intervals(int start, int end) {
        List<Interval> frontier = new ArrayList<>();
        frontier.add(new Interval(start,end));

        int i = 0;
        while(i < frontier.size()){
            int s = frontier.get(i).getStart();
            int e = frontier.get(i).getEnd();

            i++;

            // if base case
            if(s == e){
                continue;
            }

            // compute midpoint
            int m = s + (e - s) / 2;

            // add prerequisite intervals
            frontier.add(new Interval(m + 1,e));
            frontier.add(new Interval(s,m));
        }

        List<Interval> retval = new ArrayList<>();
        for(i = frontier.size() - 1; i >= 0; i--) {
            retval.add(frontier.get(i));
        }

        return retval;
    }

    /*
    This function performs the merge operation of merge sort.

    Parameters:
    array : vector<int> - array to sort
    s     : int         - start index of merge
    e     : int         - end index (inclusive) of merge
    */
    public static void merge(int[] array, int s, int e) {
        int m = s + (e - s) / 2;
        int[] left = new int[m - s + 1];
        int[] right = new int[e - m];
        int l_ptr = 0, r_ptr = 0;
        for(int i = s; i <= e; i++) {
            if(i <= m) {
                left[l_ptr++] = array[i];
            } else {
                right[r_ptr++] = array[i];
            }
        }
        l_ptr = r_ptr = 0;

        for(int i = s; i <= e; i++) {
            // no more elements on left half
            if(l_ptr == m - s + 1) {
                array[i] = right[r_ptr];
                r_ptr++;

            // no more elements on right half or left element comes first
            } else if(r_ptr == e - m || left[l_ptr] <= right[r_ptr]) {
                array[i] = left[l_ptr];
                l_ptr++;
            } else {
                array[i] = right[r_ptr];
                r_ptr++;
            }
        }
    }
}

class Interval {
    private int start;
    private int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
