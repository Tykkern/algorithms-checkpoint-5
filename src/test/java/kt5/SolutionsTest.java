package kt5;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class SolutionsTest {

    private static final Random RANDOM = new Random(1);
    private static final int BIG = 1_000_000;

    private static int[] randomArray(int length, int bound) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++)
            arr[i] = RANDOM.nextInt(2 * bound + 1) - bound;
        return arr;
    }

    // Наборы, на которых обычно ломаются сортировки: пустой, один элемент,
    // повторы, уже отсортированный, обратный, крайние значения int.
    private static int[][] specialArrays() {
        int[] ascending = new int[1000];
        int[] descending = new int[1000];
        int[] organPipe = new int[1000];
        for (int i = 0; i < 1000; i++) {
            ascending[i] = i;
            descending[i] = 1000 - i;
            organPipe[i] = Math.min(i, 999 - i);
        }
        return new int[][] {
                {}, {7}, {2, 1}, {1, 2}, {3, 3, 3, 3, 3},
                {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, Integer.MAX_VALUE, Integer.MIN_VALUE},
                ascending, descending, organPipe, new int[1000],
        };
    }

    // Сравнивает сортировку с Arrays.sort на копии того же массива.
    private static void assertSortsLikeArraysSort(Consumer<int[]> sort, int[] original) {
        int[] actual = Arrays.copyOf(original, original.length);
        int[] expected = Arrays.copyOf(original, original.length);
        sort.accept(actual);
        Arrays.sort(expected);
        assertArrayEquals(expected, actual);
    }

    private static void mergeSort(int[] arr) {
        MergeSort.mergeSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr) {
        QuickSort.quickSort(arr, 0, arr.length - 1);
    }

    @Nested
    class MergeSortTest {

        @Test
        void specialCases() {
            for (int[] arr : specialArrays())
                assertSortsLikeArraysSort(SolutionsTest::mergeSort, arr);
        }

        @Test
        void randomArraysMatchArraysSort() {
            for (int round = 0; round < 3000; round++)
                assertSortsLikeArraysSort(SolutionsTest::mergeSort,
                        randomArray(RANDOM.nextInt(300), 1 + RANDOM.nextInt(100)));
        }

        @Test
        void sortsOnlyGivenRange() {
            int[] arr = {9, 8, 5, 1, 4, 2, 0, -1};
            MergeSort.mergeSort(arr, 2, 5);
            assertArrayEquals(new int[] {9, 8, 1, 2, 4, 5, 0, -1}, arr);
        }

        @Test
        void mergeKeepsBothHalves() {
            int[] arr = {1, 4, 4, 9, 2, 4, 10, 11};
            MergeSort.merge(arr, 0, 3, 7);
            assertArrayEquals(new int[] {1, 2, 4, 4, 4, 9, 10, 11}, arr);
        }

        @Test
        @Timeout(5)
        void millionElements() {
            assertSortsLikeArraysSort(SolutionsTest::mergeSort, randomArray(BIG, Integer.MAX_VALUE / 2));
        }
    }

    @Nested
    class QuickSortTest {

        @Test
        void specialCases() {
            for (int[] arr : specialArrays())
                assertSortsLikeArraysSort(SolutionsTest::quickSort, arr);
        }

        @Test
        void randomArraysMatchArraysSort() {
            for (int round = 0; round < 3000; round++)
                assertSortsLikeArraysSort(SolutionsTest::quickSort,
                        randomArray(RANDOM.nextInt(300), 1 + RANDOM.nextInt(100)));
        }

        @Test
        void sortsOnlyGivenRange() {
            int[] arr = {9, 8, 5, 1, 4, 2, 0, -1};
            QuickSort.quickSort(arr, 2, 5);
            assertArrayEquals(new int[] {9, 8, 1, 2, 4, 5, 0, -1}, arr);
        }

        @Test
        void partitionSplitsAroundPivot() {
            for (int round = 0; round < 2000; round++) {
                int[] arr = randomArray(2 + RANDOM.nextInt(50), 1 + RANDOM.nextInt(20));
                int[] before = Arrays.copyOf(arr, arr.length);
                int split = QuickSort.partition(arr, 0, arr.length - 1);

                assertTrue(split >= 0 && split < arr.length - 1, "обе части должны быть непустыми");
                int leftMax = Integer.MIN_VALUE;
                for (int i = 0; i <= split; i++)
                    leftMax = Math.max(leftMax, arr[i]);
                for (int i = split + 1; i < arr.length; i++)
                    assertTrue(arr[i] >= leftMax);

                Arrays.sort(before);
                int[] after = Arrays.copyOf(arr, arr.length);
                Arrays.sort(after);
                assertArrayEquals(before, after, "разбиение не должно терять элементы");
            }
        }

        // Худшие случаи для опорного «всегда последний»: здесь каждый был бы O(n²),
        // то есть около 5 * 10^11 сравнений на миллионе элементов.
        @Test
        @Timeout(5)
        void sortedMillion() {
            int[] arr = new int[BIG];
            for (int i = 0; i < BIG; i++)
                arr[i] = i;
            assertSortsLikeArraysSort(SolutionsTest::quickSort, arr);
        }

        @Test
        @Timeout(5)
        void reversedMillion() {
            int[] arr = new int[BIG];
            for (int i = 0; i < BIG; i++)
                arr[i] = BIG - i;
            assertSortsLikeArraysSort(SolutionsTest::quickSort, arr);
        }

        @Test
        @Timeout(5)
        void millionEqualElements() {
            int[] arr = new int[BIG];
            Arrays.fill(arr, 42);
            assertSortsLikeArraysSort(SolutionsTest::quickSort, arr);
        }

        @Test
        @Timeout(5)
        void millionFewDistinctValues() {
            assertSortsLikeArraysSort(SolutionsTest::quickSort, randomArray(BIG, 2));
        }

        @Test
        @Timeout(5)
        void millionRandom() {
            assertSortsLikeArraysSort(SolutionsTest::quickSort, randomArray(BIG, Integer.MAX_VALUE / 2));
        }
    }

    @Nested
    class BinarySearchTest {

        @Test
        void smallArrayByEye() {
            int[] arr = {-3, 0, 1, 3, 3, 3, 5, 8, 9};
            assertEquals(0, Search.binarySearch(arr, -3));
            assertEquals(7, Search.binarySearch(arr, 8));
            assertEquals(8, Search.binarySearch(arr, 9));
            assertEquals(3, arr[Search.binarySearch(arr, 3)]);
            assertEquals(-1, Search.binarySearch(arr, 4));
            assertEquals(-1, Search.binarySearch(arr, -10));
            assertEquals(-1, Search.binarySearch(arr, 100));
            assertEquals(-1, Search.binarySearch(new int[0], 1));
            assertEquals(0, Search.binarySearch(new int[] {Integer.MIN_VALUE}, Integer.MIN_VALUE));
        }

        @Test
        void matchesArraysBinarySearch() {
            for (int round = 0; round < 2000; round++) {
                int bound = 1 + RANDOM.nextInt(30);
                int[] arr = randomArray(RANDOM.nextInt(100), bound);
                mergeSort(arr);
                for (int target = -bound - 2; target <= bound + 2; target++) {
                    int found = Search.binarySearch(arr, target);
                    if (Arrays.binarySearch(arr, target) >= 0)
                        assertEquals(target, arr[found]);
                    else
                        assertEquals(-1, found);
                }
            }
        }

        // В массиве из 2^31 - 1 элемента (left + right) переполнился бы, но такой
        // массив не поместится в тест. Поэтому проверяется сама формула середины
        // на границах, которые встречаются в таком массиве.
        @Test
        void middleFormulaDoesNotOverflow() {
            int left = Integer.MAX_VALUE - 10;
            int right = Integer.MAX_VALUE - 1;
            assertTrue(left + right < 0, "наивная формула здесь переполняется");
            assertEquals(Integer.MAX_VALUE - 6, left + (right - left) / 2);
        }
    }

    @Nested
    class BoundsTest {

        @Test
        void smallArrayByEye() {
            int[] arr = {1, 2, 2, 2, 5, 7, 7};
            assertEquals(1, Search.lowerBound(arr, 2));
            assertEquals(4, Search.upperBound(arr, 2));
            assertEquals(4, Search.lowerBound(arr, 3));
            assertEquals(4, Search.upperBound(arr, 3));
            assertEquals(0, Search.lowerBound(arr, 0));
            assertEquals(0, Search.lowerBound(arr, 1));
            assertEquals(1, Search.upperBound(arr, 1));
            assertEquals(5, Search.lowerBound(arr, 7));
            assertEquals(7, Search.upperBound(arr, 7));
            assertEquals(7, Search.lowerBound(arr, 8));
            assertEquals(7, Search.upperBound(arr, 8));
            assertEquals(0, Search.lowerBound(new int[0], 5));
            assertEquals(0, Search.upperBound(new int[0], 5));
        }

        @Test
        void allEqual() {
            int[] arr = {4, 4, 4, 4};
            assertEquals(0, Search.lowerBound(arr, 4));
            assertEquals(4, Search.upperBound(arr, 4));
            assertEquals(4, Search.lowerBound(arr, 5));
            assertEquals(0, Search.upperBound(arr, 3));
        }

        @Test
        void extremeValues() {
            int[] arr = {Integer.MIN_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
            assertEquals(0, Search.lowerBound(arr, Integer.MIN_VALUE));
            assertEquals(2, Search.upperBound(arr, Integer.MIN_VALUE));
            assertEquals(3, Search.lowerBound(arr, Integer.MAX_VALUE));
            assertEquals(4, Search.upperBound(arr, Integer.MAX_VALUE));
        }

        // Разница upperBound - lowerBound сверяется с подсчётом вручную, а сами
        // границы с определением: всё левее меньше (не больше), всё правее нет.
        @Test
        void countMatchesManualCount() {
            for (int round = 0; round < 2000; round++) {
                int bound = 1 + RANDOM.nextInt(20);
                int[] arr = randomArray(RANDOM.nextInt(100), bound);
                quickSort(arr);
                for (int target = -bound - 2; target <= bound + 2; target++) {
                    int lower = Search.lowerBound(arr, target);
                    int upper = Search.upperBound(arr, target);
                    int count = 0;
                    for (int value : arr)
                        if (value == target)
                            count++;
                    assertEquals(count, upper - lower);
                    for (int i = 0; i < arr.length; i++) {
                        assertEquals(i < lower, arr[i] < target);
                        assertEquals(i < upper, arr[i] <= target);
                    }
                }
            }
        }
    }

    @Test
    void mainSelfCheckFindsNoMismatches() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int mismatches = Main.run(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        assertEquals(0, mismatches);
        assertTrue(buffer.toString(StandardCharsets.UTF_8).contains("расхождений 0"));
    }
}
