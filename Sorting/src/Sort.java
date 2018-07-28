import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Smolin on 04.12.2016.
 * Implement Sort - aproximately O(N* logN)
 */
public class Sort {

    public static void quicksort(Comparable[] array) {
        //shuffle(array);
        int li = 0;
        int hi = array.length - 1;
        partition(array, li, hi);
    }

    private static void shuffle(Comparable[] array) {
        for (int i = 0; i < array.length; i++) {
            int ind = StdRandom.uniform(0, array.length);
            swap(array, i, ind);
        }


    }

    private static void partition(Comparable[] a, int li, int hi) {

        int pi = li;
        int hiTmp = hi;
        int liTmp = li;
        if (hi - li == 2) {
            if (greater(a[li], a[hi])) {
                swap(a, li, hi);
            }
            return;
        }

        while (hi - li <= 1) {

            while (less(a[li + 1], a[pi]) && li < hi) {
                li++;
            }

            while (greater(a[hi], a[pi]) && li < hi) {
                hi--;
            }
            li++;
            swap(a, li, hi);


            if (hi - li >= 2) {
                if (greater(a[li], a[pi])) li++;
                if (less(a[hi], a[pi])) hi--;

                assert greater(a[pi], a[li]);
                assert less(a[pi], a[hi]);
                swap(a, li, hi);
            }
        }
        swap(a, pi, li);
        assert isPartitioned(a, li, liTmp, hiTmp);
        partition(a, li + 1, hiTmp);
        partition(a, pi, li);

    }

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }

    public static void kLargest(Comparable[] array, int k) {

    }

    public static void shellsort(Comparable[] array) {
        int x_next = array.length;
        for (int i = 0; x_next != 1; i++) {
            int x = (x_next - 1) / 3;
            if (x < 1) x = 1;
            x_next = x;
            hSort(array, x);
            assert isHsorted(array, x);
        }
        assert isSorted(array);
    }

    public static void hSort(Comparable[] array, int h) {
        for (int i = h; i < array.length; i++) {
            int curNext = i;
            int curBack = i - h;
            while (greater(array[curBack], array[curNext])) {
                swap(array, curBack, curNext);
                curNext = curBack;
                curBack -= h;
                if (curBack < 0) break;
            }
        }
    }

    private static void swap(Comparable[] array, int i, int j) {
        Comparable tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private static boolean greater(Comparable v, Comparable w) {
        return v.compareTo(w) > 0;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    private static boolean isSorted(Comparable[] a) {
        for (int i = 0; i < a.length - 1; i++)
            if (greater(a[i], a[i + 1])) return false;
        return true;
    }

    // is the array h-sorted?
    private static boolean isHsorted(Comparable[] a, int h) {
        for (int i = 0; i < a.length - h; i++)
            if (greater(a[i], a[i + h])) return false;
        return true;
    }

    private static boolean isPartitioned(Comparable[] a, int pi, int li, int hi) {
        for (int i = li; i < hi; i++) {
            if (i < pi && greater(a[i], a[pi])) return false;
            if (i > pi && less(a[i], a[pi])) return false;
            if (i == pi && !equal(a[i], a[pi])) return false;
        }
        return true;
    }

    private static boolean equal(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) == 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = 10;
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = in.readInt();
        }
        show(array);
        quicksort(array);
        show(array);

    }
}
