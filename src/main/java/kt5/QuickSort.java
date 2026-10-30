package kt5;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Часть 2. Быстрая сортировка со случайным опорным элементом.
 */
public final class QuickSort {

    private QuickSort() {
    }

    /**
     * Сортирует диапазон {@code [low, high]} включительно. После разбиения
     * рекурсия уходит только в меньшую часть, а большая обрабатывается
     * следующим проходом цикла. Так глубина рекурсии не превышает log n
     * даже при неудачных опорных элементах.
     */
    public static void quickSort(int[] arr, int low, int high) {
        while (low < high) {
            int split = partition(arr, low, high);
            if (split - low < high - split) {
                quickSort(arr, low, split);
                low = split + 1;
            } else {
                quickSort(arr, split + 1, high);
                high = split;
            }
        }
    }

    /**
     * Разбиение Хоара со случайным опорным элементом. Опорный выбирается
     * случайно из всего диапазона и ставится в начало. Два индекса идут
     * навстречу: левый останавливается на элементе не меньше опорного, правый
     * на элементе не больше опорного, и эти элементы меняются местами.
     *
     * Возвращает границу {@code split}: все элементы {@code [low, split]} не
     * больше опорного, все элементы {@code [split + 1, high]} не меньше его.
     * Обе части непустые, поэтому сортировка всегда продвигается.
     *
     * Равные опорному элементы останавливают оба индекса и расходятся по
     * обеим частям поровну, так что массив из одинаковых чисел тоже
     * делится пополам, а не в отношении 1 к n - 1.
     */
    public static int partition(int[] arr, int low, int high) {
        swap(arr, low, ThreadLocalRandom.current().nextInt(low, high + 1));
        int pivot = arr[low];
        int i = low - 1;
        int j = high + 1;

        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);
            do {
                j--;
            } while (arr[j] > pivot);
            if (i >= j)
                return j;
            swap(arr, i, j);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }
}
