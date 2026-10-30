package kt5;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

/**
 * Самопроверка из задания: свои сортировки сравниваются с Arrays.sort,
 * свой поиск с Arrays.binarySearch и с подсчётом вхождений вручную.
 * Встроенные методы здесь только для сверки, в самих алгоритмах их нет.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.exit(run(System.out) == 0 ? 0 : 1);
    }

    /** Печатает пример и прогоняет случайные проверки, возвращает число расхождений. */
    static int run(PrintStream out) {
        showExample(out);
        int mismatches = randomCheck(new Random(2026), 2_000);
        out.println();
        out.println("Случайные массивы: 2000 проверок, расхождений " + mismatches);
        return mismatches;
    }

    private static void showExample(PrintStream out) {
        int[] original = {5, -3, 8, 3, 1, 9, 3, 0, 3};
        int[] merged = Arrays.copyOf(original, original.length);
        int[] quick = Arrays.copyOf(original, original.length);
        int[] expected = Arrays.copyOf(original, original.length);
        MergeSort.mergeSort(merged, 0, merged.length - 1);
        QuickSort.quickSort(quick, 0, quick.length - 1);
        Arrays.sort(expected);

        out.println("Исходный массив: " + Arrays.toString(original));
        out.println("mergeSort:       " + Arrays.toString(merged));
        out.println("quickSort:       " + Arrays.toString(quick));
        out.println("Arrays.sort:     " + Arrays.toString(expected));
        out.println();

        for (int target : new int[] {8, 3, 4, -5, 10}) {
            out.printf("x = %d: binarySearch = %d (Arrays.binarySearch = %d), "
                            + "lowerBound = %d, upperBound = %d, вхождений %d (вручную %d)%n",
                    target, Search.binarySearch(merged, target), Arrays.binarySearch(expected, target),
                    Search.lowerBound(merged, target), Search.upperBound(merged, target),
                    Search.upperBound(merged, target) - Search.lowerBound(merged, target),
                    countByHand(original, target));
        }
    }

    private static int randomCheck(Random random, int rounds) {
        int mismatches = 0;
        for (int round = 0; round < rounds; round++) {
            int[] original = new int[random.nextInt(200)];
            int bound = 1 + random.nextInt(50);
            for (int i = 0; i < original.length; i++)
                original[i] = random.nextInt(2 * bound + 1) - bound;

            int[] merged = Arrays.copyOf(original, original.length);
            int[] quick = Arrays.copyOf(original, original.length);
            int[] expected = Arrays.copyOf(original, original.length);
            MergeSort.mergeSort(merged, 0, merged.length - 1);
            QuickSort.quickSort(quick, 0, quick.length - 1);
            Arrays.sort(expected);
            if (!Arrays.equals(merged, expected) || !Arrays.equals(quick, expected))
                mismatches++;

            for (int target = -bound - 1; target <= bound + 1; target++) {
                int found = Search.binarySearch(expected, target);
                boolean present = Arrays.binarySearch(expected, target) >= 0;
                boolean foundOk = present ? found >= 0 && expected[found] == target : found == -1;
                int count = Search.upperBound(expected, target) - Search.lowerBound(expected, target);
                if (!foundOk || count != countByHand(original, target))
                    mismatches++;
            }
        }
        return mismatches;
    }

    private static int countByHand(int[] arr, int target) {
        int count = 0;
        for (int value : arr)
            if (value == target)
                count++;
        return count;
    }
}
