import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.lang.*;

/**
 * Solutions to assignment 2
 *
 * @author 557824ad Alek Dimitrov
 */
public class Assignment2 {
    /**
     * Main method for assignment 2. Uses command line arguments to discern which assignment question
     * to execute and which relevant file to read as input for the specific assignment questions.
     *
     * @param args Command-line arguments. args[0] is understood as the question number (1-5)
     *             and args[1] is understood as the input file path for that particular question.
     * @throws FileNotFoundException When the specified input file is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Get the question number
        String questionNumberArg = args[0];
        int questionNumber = Integer.parseInt(questionNumberArg);

        // Get the input file name
        String inputFileName = args[1];

        // Do a large switch statement over the question number.
        Scanner inputFile;
        switch (questionNumber) {
            case 1:
                inputFile = new Scanner(new File(inputFileName));
                String[] input = inputFile.nextLine().split(",");
                int[] inputs = new int[input.length];
                for (int i = 0; i < input.length; i++) {
                    inputs[i] = Integer.parseInt(input[i]);
                }
                question1(inputs);
                inputFile.close();
                break;
            case 2:
                inputFile = new Scanner(new File(inputFileName));
                String[] input_q2 = inputFile.nextLine().split(",");
                int[] inputs_q2 = new int[input_q2.length];
                for (int i = 0; i < input_q2.length; i++) {
                    inputs_q2[i] = Integer.parseInt(input_q2[i]);
                }
                question2(inputs_q2);
                break;
            case 3:
                inputFile = new Scanner(new File(inputFileName));
                String[] input_q3 = inputFile.nextLine().split(",");
                int[] inputs_q3 = new int[input_q3.length];
                for (int i = 0; i < input_q3.length; i++) {
                    inputs_q3[i] = Integer.parseInt(input_q3[i]);
                }
                question3(inputs_q3);
                break;
            case 4:
                inputFile = new Scanner(new File(inputFileName));
                String[] input_q4 = inputFile.nextLine().split(",");
                int[] inputs_q4 = new int[input_q4.length];
                for (int i = 0; i < input_q4.length; i++) {
                    inputs_q4[i] = Integer.parseInt(input_q4[i]);
                }
                question4(inputs_q4);
                break;
            case 5:
                inputFile = new Scanner(new File(inputFileName));
                String[] input_q5 = inputFile.nextLine().split(",");
                int[] inputs_q5 = new int[input_q5.length];
                for (int i = 0; i < input_q5.length; i++) {
                    inputs_q5[i] = Integer.parseInt(input_q5[i]);
                }
                question5(inputs_q5);
                break;
            default:
                System.out.println("Invalid question number!");
        }
    }

    /**
     * Question 1, prints the smallest and second-smallest integers in an integer array with solution complexity
     * time of O(n), where n is the size of the array
     *
     * @param inputs input array of integers
     */
    public static void question1(int[] inputs) {
        int smallest;
        int secondSmallest;
        if (inputs[1] == inputs[2]) {
            smallest = 1;
            secondSmallest = Integer.MAX_VALUE;
        } else {
            if (inputs[1] < inputs[2]) {
                smallest = inputs[1];
                secondSmallest = inputs[2];
            } else {
                smallest = inputs[2];
                secondSmallest = inputs[1];
            }
        }
        for (int i = 3; i < inputs.length; i++) {
            if (inputs[i] < secondSmallest && inputs[i] != smallest) {
                if (inputs[i] < smallest) {
                    secondSmallest = smallest;
                    smallest = inputs[i];
                } else {
                    secondSmallest = inputs[i];
                }
            }
        }
        System.out.println(smallest + "," + secondSmallest);
    }


    /**
     * Question 2, finds the time needed to produce m items at the bakery using binary search. The solution complexity
     * time is O(log m)+O(N), where m are the number of items to be produced and N are the number of robots.
     *
     * @param inputs input array of integers, where the first number is m, the number of items to be produced and the
     *               latter numbers represent the production speeds of the robots
     * @see #itemsProduced(int[], int)
     */

    public static void question2(int[] inputs) {
        int B = inputs[0];
        int servers = inputs.length - 1;
        // First, find the highest preparation time for a baker (Solution complexity time of O(N))
        int maxTime = inputs[1];
        for (int i = 2; i < inputs.length; i++) {
            if (maxTime < inputs[i]) {
                maxTime = inputs[i];
            }
        }

        // Now, the lower bound for the binary search is 1 and the upper bound is maxTime*(int)Math.ceil((double)B/servers)
        int lb = 1;
        int ub = maxTime * (int) Math.ceil((double) B / servers);

        //Then, perform binary search using this upper bound, the solution complexity time for this is O(log m)
        int step = 1;
        while (lb < ub) {
            step = (lb + ub) / 2;
            int currentItems = itemsProduced(inputs, step);

            if (currentItems < B) {
                lb = step + 1;
            } else {
                ub = step;
            }
        }
        System.out.println(ub);
    }

    /**
     * Finding the number of items produced at a specific time for {@link #question2(int[])}
     *
     * @param inputs the input array from {@link #question2(int[])}
     * @param time   the time for which to compute how many items are produced
     * @return currentItems the number of items that can be produced for the given time
     */
    public static int itemsProduced(int[] inputs, int time) {
        int currentItems = 0;
        // We cycle over all N robots to update the currentItems, hence the solution complexity time of the following
        // loop is O(N):
        for (int i = 1; i < inputs.length; i++) {
            currentItems = currentItems + time / inputs[i];
        }
        return currentItems;
    }

    /**
     * Question 3, finds the third-largest number in an array using the divide and conquer algorithm and has a solution
     * complexity time of O(n), where n is the size of the array
     *
     * @param inputs input array of integers
     * @see #firstThree(int[], int, int)
     */
    public static void question3(int[] inputs) {
        System.out.println(firstThree(inputs, 0, inputs.length - 1)[0]);
    }

    /**
     * Finding the three largest integers for the given array from {@link #question3(int[])}} but confining the search to
     * be only between index p and r, where p<r-1 using the divide and conquer algorithm
     *
     * @param inputs the input array from {@link #question3(int[])}
     * @param p      the lower bound for the index (inclusive)
     * @param r      the upper bound for the index (inclusive)
     * @return result the array of size 3 which contains the third-largest, second-largest and largest elements
     * (in this order)
     */
    public static int[] firstThree(int[] inputs, int p, int r) {
        int[] result = new int[3];
        // Base case: array length is 3, 4 or 5:
        if (r - p == 2 || r - p == 3 || r - p == 4) {
            int[] intermediary = new int[r - p + 1];
            for (int k = p; k <= r; k++) {
                intermediary[k - p] = inputs[k];
            }
            // We use a sorting function for an array of size 3, 4 or 5 in this case, hence this part has an O(1) solution time
            // complexity and is not relevant to our overall solution time complexity:
            Arrays.sort(intermediary);
            result[0] = intermediary[intermediary.length - 3];
            result[1] = intermediary[intermediary.length - 2];
            result[2] = intermediary[intermediary.length - 1];
        } else {
            // Split the array of interest into two halves and find the three largest elements in each of them
            // separately using recursion:
            int[] leftArray = firstThree(inputs, p, (p + r) / 2);
            int[] rightArray = firstThree(inputs, (p + r) / 2 + 1, r);

            // Finally, find the three largest elements among the six elements we have from the previous step:
            int[] combined = new int[6];
            System.arraycopy(leftArray, 0, combined, 0, 3);
            System.arraycopy(rightArray, 0, combined, 3, 3);
            Arrays.sort(combined);
            result[0] = combined[3];
            result[1] = combined[4];
            result[2] = combined[5];
        }
        return result;
    }

    /**
     * Question 4, performs binary search find k, the index of the first zero, using n-1 as upper bound, where n is the size of the
     * input array. Hence, the solution complexity time of this method is O(log n)
     *
     * @param inputs input array of zeros and ones, where the first part of the array consists only of zeros and the
     *               second part consists only of ones
     */
    public static void question4(int[] inputs) {
        int lb = 0;
        int ub = inputs.length - 1;
        int step = 0;
        while (lb < ub) {
            step = (lb + ub) / 2;
            if (inputs[step] == 0) {
                lb = step + 1;
            } else {
                ub = step;
            }
        }
        System.out.println(ub - 1);
    }

    /**
     * Question 5, performs binary search to find k, the index of the first zero, using an upper bound that is no
     * greater than 2*k. The proposed solution has time complexity of O(log k)
     *
     * @param inputs input array of zeros and ones, where the first part of the array consists only of zeros and the
     *               second part consists only of ones
     */
    public static void question5(int[] inputs) {
        // We find an upper bound for the index of the last 0 using the following algorithm which is of time complexity
        // O(log k). The time complexity of this algorithm is O(log k), because ub (upper bound) can never become greater
        // than 2*k, hence we will reach to the correct ub value in no more than Math.ceil(log (2*k))
        // iterations of the while loop (where the log is of base 2, but the base of the log is not relevant when we
        // translate this to solution time complexity):
        int ub = 1;
        while (inputs[ub] != 1) {
            ub = ub * 2;
        }
        int lb = 0;

        //We then apply the binary search algorithm using this upper bound, which is, again, of time complexity O(log k):
        int step = 0;
        while (lb < ub) {
            step = (lb + ub) / 2;
            if (inputs[step] == 0) {
                lb = step + 1;
            } else {
                ub = step;
            }
        }
        System.out.println(ub - 1);
    }
}




