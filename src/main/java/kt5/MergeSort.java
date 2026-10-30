package kt5;

/**
 * Часть 1. Сортировка слиянием.
 */
public final class MergeSort {

    private MergeSort() {
    }

    /**
     * Сортирует диапазон {@code [left, right]} включительно. Диапазон делится
     * пополам, пока не останется один элемент, затем отсортированные половины
     * сливаются обратно. Глубина рекурсии log n, время O(n log n) на любых
     * данных, дополнительная память O(n) на слияние.
     */
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right)
            return;
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    /**
     * Сливает уже отсортированные половины {@code [left, mid]} и
     * {@code [mid + 1, right]}. Два индекса идут по половинам, в результат
     * каждый раз уходит меньший элемент. При равенстве берётся элемент левой
     * половины, поэтому равные элементы сохраняют исходный порядок
     * (сортировка устойчивая).
     */
    public static void merge(int[] arr, int left, int mid, int right) {
        int[] merged = new int[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j])
                merged[k++] = arr[i++];
            else
                merged[k++] = arr[j++];
        }
        // Одна половина закончилась, хвост другой дописывается без сравнений.
        while (i <= mid)
            merged[k++] = arr[i++];
        while (j <= right)
            merged[k++] = arr[j++];

        for (k = 0; k < merged.length; k++)
            arr[left + k] = merged[k];
    }
}
