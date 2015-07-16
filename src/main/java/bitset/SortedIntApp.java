package bitset;


import java.util.Random;

public class SortedIntApp {

    public static void main(String... args) {

        SortedIntSet intSet = new SortedIntSet();
        int maxValue = Integer.parseInt(args[0]);
        Random random = new Random();
        System.out.println("Input Value->");
        for (int counter = 0; counter < maxValue; counter++) {
            int nextValue = random.nextInt(maxValue);
            intSet.add(nextValue);
            System.out.print(nextValue + " ");
        }

        System.out.println();
        System.out.println("Sorted Value ->");

        for (int value : intSet) {
            System.out.print(value + " ");
        }


    }
}
