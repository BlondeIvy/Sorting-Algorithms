import java.util.Arrays;
import java.util.Random;

public class AlgorithmAnalysis {
    private static final int[] ARRAY_SIZES = {100, 500, 1000, 10000, 100000};
    private static final int RUNS = 10;

    public static void main(String[] args) {
        for (int size : ARRAY_SIZES) {
            System.out.println("Array size: " + size);
            runSortAnalysis(size);
            runSearchingAnalysis(size);
            System.out.println();
        }
    }

    private static void runSortAnalysis(int size) {
        long[][] times = new long[4][RUNS];
        for (int i = 0; i < RUNS; i++) {
            int[] array = ArrayGenerator.generateArray(size);
            int[] array1 = Arrays.copyOf(array, array.length);
            int[] array2 = Arrays.copyOf(array, array.length);
            int[] array3 = Arrays.copyOf(array, array.length);

            times[0][i] = ArrayGenerator.timeAlgorithm(() -> SortingAlgorithms.bubbleSort(array));
            times[1][i] = ArrayGenerator.timeAlgorithm(() -> SortingAlgorithms.insertionSort(array1));
            times[2][i] = ArrayGenerator.timeAlgorithm(() -> SortingAlgorithms.selectionSort(array2));
            times[3][i] = ArrayGenerator.timeAlgorithm(() -> SortingAlgorithms.mergeSort(array3));
        }
        printResults("Bubble Sort", times[0]);
        printResults("Insertion Sort", times[1]);
        printResults("Selection Sort", times[2]);
        printResults("Merge Sort", times[3]);
    }

    private static void runSearchingAnalysis(int size) {
        long[][] times = new long[2][RUNS];
        int[] array = ArrayGenerator.generateArray(size);
        Arrays.sort(array);  // For binary search, array needs to be sorted
        int searchValue = array[array.length / 2];  // Search for middle value
        for (int i = 0; i < RUNS; i++) {
            times[0][i] = ArrayGenerator.timeAlgorithm(() -> SearchingAlgorithms.linearSearch(array, searchValue));
            times[1][i] = ArrayGenerator.timeAlgorithm(() -> SearchingAlgorithms.binarySearch(array, searchValue));
        }
        printResults("Linear Search", times[0]);
        printResults("Binary Search", times[1]);
    }

    private static void printResults(String algorithmName, long[] times) {
        Arrays.sort(times);
        long fastest = times[0];
        long slowest = times[RUNS - 1];
        long average = Arrays.stream(times).sum() / RUNS;
        System.out.printf("%s: Fastest: %d ns, Slowest: %d ns, Average: %d ns%n",
                algorithmName, fastest, slowest, average);
    }
}

class ArrayGenerator {
    public static int[] generateArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000000); // Random numbers between 0 and 999999
        }
        return array;
    }

    public static long timeAlgorithm(Runnable algorithm) {
        long startTime = System.nanoTime();
        algorithm.run();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}

class SortingAlgorithms {
    public static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public static void insertionSort(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    public static void selectionSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = array[minIdx];
            array[minIdx] = array[i];
            array[i] = temp;
        }
    }

    public static void mergeSort(int[] array) {
        if (array.length < 2) {
            return;
        }
        int mid = array.length / 2;
        int[] lo = new int[mid];
        int[] hi = new int[array.length - mid];

        System.arraycopy(array, 0, lo, 0, mid);
        System.arraycopy(array, mid, hi, 0, array.length - mid);

        mergeSort(lo);
        mergeSort(hi);

        merge(array, lo, hi);
    }

    private static void merge(int[] array, int[] lo, int[] hi) {
        int i = 0, j = 0, k = 0;
        while (i < lo.length && j < hi.length) {
            if (lo[i] <= hi[j]) {
                array[k++] = lo[i++];
            } else {
                array[k++] = hi[j++];
            }
        }
        while (i < lo.length) {
            array[k++] = lo[i++];
        }
        while (j < hi.length) {
            array[k++] = hi[j++];
        }
    }
}

class SearchingAlgorithms {
    public static int linearSearch(int[] array, int x) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == x) {
                return i;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] array, int x) {
        int lo = 0, hi = array.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (array[mid] == x) {
                return mid;
            }
            if (array[mid] < x) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -1;
    }
}
