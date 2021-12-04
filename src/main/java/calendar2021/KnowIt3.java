package calendar2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class KnowIt3 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("data/day3.txt"));

        int[] counts = in.nextLine().chars().map(i -> i == 'J' ? 1 : 0).toArray();
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }
        int max = 0;
        int first = -1;
        for (int i = 0; i < counts.length; i++) {
            for (int j = i + 1; j < counts.length; j++) {
                int current = 2 * (counts[j] - counts[i] + 1);
                if (current == j - i + 1 && current > max) {
                    max = current;
                    first = i;
                }
            }
        }
        System.out.println(max + ", " + first);
    }
}
