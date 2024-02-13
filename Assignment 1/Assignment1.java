import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.reflect.Array;

/**
 * Solutions to assignment 1
 *
 * @author 557824ad Alek Dimitrov
 */
public class Assignment1 {
    /**
     * Main method for assignment 1. Uses command line arguments to discern which assignment question
     * to execute and which relevant file to read as input for the specific assignment questions.
     *
     * @param args Command-line arguments. args[0] is understood as the question number (1-5)
     *             and args[1] is understood as the input file path for that particular question.
     * @throws FileNotFoundException When the specified input file is not found.
     * @throws NumberFormatException When the input for question 3 is not an integer
     */
    public static void main(String[] args) throws FileNotFoundException, NumberFormatException {
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
                String word = inputFile.nextLine();

                question1(word);
                inputFile.close();
                break;
            case 2:
                inputFile = new Scanner(new File(inputFileName));
                String sentenceq3 = inputFile.nextLine();

                question2(sentenceq3);
                inputFile.close();
                break;
            case 3:
                inputFile = new Scanner(new File(inputFileName));
                String numberString = inputFile.nextLine();
                int numberInteger = Integer.parseInt(numberString);

                question3(numberInteger);
                inputFile.close();
                break;
            case 4:
                inputFile = new Scanner(new File(inputFileName));
                String letterString = inputFile.nextLine();

                question4(letterString);
                inputFile.close();
                break;
            case 5:
                inputFile = new Scanner(new File(inputFileName));
                String sentenceq5 = inputFile.nextLine();

                question5(sentenceq5);
                inputFile.close();
                break;
            default:
                System.out.println("Invalid question number!");
        }
    }

    /**
     * Question 1, prints out all anagrams of the provided string.
     *
     * @param str given input string
     * @throws NumberFormatException if there is a non-integer value in the input
     * @see #anagrammingQ1(List, int)
     */
    public static void question1(String str) throws NumberFormatException {

        String[] values = str.split(",", -1);
        //Then, remove the first element of the string, which represents the size of the words, for ease of notation
        int visits = Integer.parseInt(values[1]);
        List<String> valuesList = Arrays.asList(values);
        List<String> cities = valuesList.subList(2, values.length);
        ArrayList<String> output = new ArrayList<>(anagrammingQ1(cities, visits));
        String answer = "";
        for (String s : output) {
            answer = answer + s + ",";
        }
        System.out.println(answer);
    }


    /**
     * Recursion for {@link #question1(String)} to compute all anagrams
     *
     * @param cities the list of all cities
     * @param visits the number of cities to be visited
     * @return answer the list of all possible combinations of visits
     */

    private static List<String> anagrammingQ1(List<String> cities, int visits) {
        List<String> answer = new ArrayList<>();
        if (visits == 1) {
            return cities;
        }
        List<String> previousAnagram = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            ArrayList<String> copy = new ArrayList<String>(cities);
            copy.remove(i);
            previousAnagram = anagrammingQ1(copy, visits - 1);
            for (int j = 0; j < previousAnagram.size(); j++) {
                answer.add(cities.get(i) + previousAnagram.get(j));
            }
        }
        return answer;
    }

    /**
     * Question 2, prints the number of matching elements
     *
     * @param str given input string
     * @see #countMatchesQ2(String[], String)
     */
    public static void question2(String str) {
        String[] allWords = str.split(", ");
        String keyword = allWords[0];
        String[] words = Arrays.copyOfRange(allWords, 1, allWords.length);
        System.out.println(countMatchesQ2(words, keyword));
    }

    /**
     * Recursion for {@link #question2(String)}
     *
     * @param words   the array of all the input words
     * @param keyword the word which will be searched from in the given array
     * @return the number of matching words with the keyword
     */
    private static int countMatchesQ2(String[] words, String keyword) {
        if (words.length == 0) {
            return 0;
        }
        if (words[0].equals(keyword)) {
            return 1 + countMatchesQ2(Arrays.copyOfRange(words, 1, words.length), keyword);
        }
        return countMatchesQ2(Arrays.copyOfRange(words, 1, words.length), keyword);

    }

    /**
     * Question 3, prints the cheops number corresponding to a given integer
     *
     * @param n the number of iterations that the cheops number recursion must do
     * @see #cheopsNumberQ3(int)
     */
    public static void question3(int n) {
        System.out.println(cheopsNumberQ3(n));
    }


    /**
     * Recursion for {@link #question3(int)}
     *
     * @param n the number of iterations that the cheops number recursion must do
     * @return the cheops number corresponding to n
     */
    private static int cheopsNumberQ3(int n) {
        if (n == 1) {
            return 1;
        } else
            return (n + cheopsNumberQ3(n - 1));
    }


    /**
     * Question 4, returns the list of all possibles words that can be formed by pressing the given numbers in a phone keypad
     *
     * @param str the list of given numbers
     * @see #wordsArrayQ4(String, int, Map)
     */
    public static void question4(String str) {
        //Remove commas (we can do this, since all given numbers are 1-digit)
        String noCommas = str.replace(",", "");
        //Then, remove the first element of the string, which represents the size of the words, for ease of notation
        StringBuilder strBuilder = new StringBuilder(noCommas);
        strBuilder = strBuilder.deleteCharAt(0);
        String cleanedString = strBuilder.toString();
        //We want to see where are the zeros and ones in the input and remove them, since they add no information
        cleanedString = cleanedString.replace("0", "");
        cleanedString = cleanedString.replace("1", "");
        //Next, we determine the number of words that can be formed
        int numberOfWords = 1;
        for (int i = 0; i < cleanedString.length(); i++) {
            if (cleanedString.charAt(i) == '7' || cleanedString.charAt(i) == '9') {
                numberOfWords = numberOfWords * 4;
            } else {
                numberOfWords = numberOfWords * 3;
            }
        }

        //We map each number to the letters it represents
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(2, Arrays.asList(new String[]{"a", "b", "c"}));
        map.put(3, Arrays.asList(new String[]{"d", "e", "f"}));
        map.put(4, Arrays.asList(new String[]{"g", "h", "i"}));
        map.put(5, Arrays.asList(new String[]{"j", "k", "l"}));
        map.put(6, Arrays.asList(new String[]{"m", "n", "o"}));
        map.put(7, Arrays.asList(new String[]{"p", "q", "r", "s"}));
        map.put(8, Arrays.asList(new String[]{"t", "u", "v"}));
        map.put(9, Arrays.asList(new String[]{"w", "x", "y", "z"}));
        String[] finalArray = wordsArrayQ4(cleanedString, numberOfWords, map);
        String answer = "";
        for (String s : finalArray) {
            answer = answer + s + ",";
        }
        StringBuilder sb = new StringBuilder(answer);
        sb.deleteCharAt(answer.length() - 1);
        String answerNoComma = sb.toString();
        System.out.println(answerNoComma);
    }

    /**
     * Recursion for {@link #question4(String)}
     *
     * @param cleanedString the string of numbers in the input after removing the commas and the first number
     * @param numberOfWords the total number of words that can be made
     * @param map           the map that links the different numbers of the keypad to their corresponding letters
     * @return currentArray the array of all the possible words that can be made
     */
    private static String[] wordsArrayQ4(String cleanedString, int numberOfWords, Map<Integer, List<String>> map) {
        String[] currentArray = new String[numberOfWords];
        int firstNumber = Character.getNumericValue(cleanedString.charAt(0));
        //Since depending on whether we have a 7 or 9, the total number of words changes, we must split the code in 2 parts, where first part is executed if
        //we are currently at a 7 or 9, and second part is executed otherwise (since arrays have fixed size, we can't initialize an array without specifying size)
        if (firstNumber == 7 || firstNumber == 9) {
            String[] previousArray = new String[numberOfWords / 4];
            if (cleanedString.length() == 1) {
                Array.set(currentArray, 0, map.get(firstNumber).get(0));
                Array.set(currentArray, 1, map.get(firstNumber).get(1));
                Array.set(currentArray, 2, map.get(firstNumber).get(2));
                Array.set(currentArray, 3, map.get(firstNumber).get(3));

            } else {

                previousArray = wordsArrayQ4(cleanedString.substring(0, cleanedString.length() - 1), numberOfWords / 4, map);
                for (int i = 0; i < numberOfWords / 4; i++) {
                    Array.set(currentArray, i, map.get(firstNumber).get(0) + previousArray[i]);
                    Array.set(currentArray, i + numberOfWords / 4, map.get(firstNumber).get(1) + previousArray[i]);
                    Array.set(currentArray, i + numberOfWords * 2 / 4, map.get(firstNumber).get(2) + previousArray[i]);
                    Array.set(currentArray, i + numberOfWords * 3 / 4, map.get(firstNumber).get(3) + previousArray[i]);
                }
            }
        } else {
            String[] previousArray = new String[numberOfWords / 3];
            if (cleanedString.length() == 1) {
                Array.set(currentArray, 0, map.get(firstNumber).get(0));
                Array.set(currentArray, 1, map.get(firstNumber).get(1));
                Array.set(currentArray, 2, map.get(firstNumber).get(2));

            } else {
                previousArray = wordsArrayQ4(cleanedString.substring(1, cleanedString.length()), numberOfWords / 3, map);
                for (int i = 0; i < numberOfWords / 3; i++) {
                    Array.set(currentArray, i, map.get(firstNumber).get(0) + previousArray[i]);
                    Array.set(currentArray, i + numberOfWords / 3, map.get(firstNumber).get(1) + previousArray[i]);
                    Array.set(currentArray, i + numberOfWords * 2 / 3, map.get(firstNumber).get(2) + previousArray[i]);

                }
            }
        }
        return currentArray;
    }

    /**
     * Question 5, returns the number of vowels in a sentence
     *
     * @param str the given string (sentence)
     * @see #numberOfVowelsQ5(String)
     */
    public static void question5(String str) {
        System.out.println(numberOfVowelsQ5(str));
    }

    /**
     * Recursion for {@link #question5(String)}
     *
     * @param str the given string (sentence)
     * @return the number of vowels in the sentence
     */
    private static int numberOfVowelsQ5(String str) {
        if (str.length() == 0) {
            return 0;
        }
        if (str.charAt(0) == 'a' || str.charAt(0) == 'e' || str.charAt(0) == 'i' || str.charAt(0) == 'o' || str.charAt(0) == 'u' || str.charAt(0) == 'A' || str.charAt(0) == 'E' || str.charAt(0) == 'I' || str.charAt(0) == 'O' || str.charAt(0) == 'U') {
            return 1 + numberOfVowelsQ5(str.substring(1, str.length()));
        }
        return numberOfVowelsQ5(str.substring(1, str.length()));
    }


}
