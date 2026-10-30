package kt5;

/**
 * Части 3 и 4. Бинарный поиск по отсортированному массиву.
 *
 * Середина везде считается как {@code left + (right - left) / 2}: разность
 * границ не превышает длину массива, поэтому переполнения int нет.
 * Вариант {@code (left + right) / 2} переполняется, когда обе границы
 * больше {@code Integer.MAX_VALUE / 2}.
 */
public final class Search {

    private Search() {
    }

    /**
     * Индекс элемента {@code target} или -1, если его нет. Поиск на отрезке
     * {@code [left, right]}: середина сравнивается с искомым, и отрезок
     * сужается до половины, где искомое ещё может быть. При повторах
     * возвращается индекс одного из равных элементов. O(log n).
     */
    public static int binarySearch(int[] sortedArr, int target) {
        int left = 0;
        int right = sortedArr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (sortedArr[mid] == target)
                return mid;
            if (sortedArr[mid] < target)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return -1;
    }

    /**
     * Индекс первого элемента, не меньшего {@code target}, или длина массива,
     * если таких нет. Ответ ищется на полуинтервале {@code [left, right)}:
     * всё левее {@code left} меньше target, всё начиная с {@code right} не
     * меньше. Когда границы сходятся, это и есть ответ. O(log n).
     */
    public static int lowerBound(int[] sortedArr, int target) {
        int left = 0;
        int right = sortedArr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sortedArr[mid] < target)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }

    /**
     * Индекс первого элемента, строго большего {@code target}, или длина
     * массива. Отличается от {@link #lowerBound} только условием: элементы,
     * равные target, тоже остаются слева. Поэтому
     * {@code upperBound - lowerBound} равно числу вхождений target. O(log n).
     */
    public static int upperBound(int[] sortedArr, int target) {
        int left = 0;
        int right = sortedArr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sortedArr[mid] <= target)
                left = mid + 1;
            else
                right = mid;
        }
        return left;
    }
}
