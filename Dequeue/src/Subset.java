import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    private final static boolean DEBUG = false;

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = DEBUG ? 3 : Integer.parseInt(args[0]);
        String[] sequence = DEBUG ? new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"} : StdIn.readAll().split("\\s");

        for (String s : sequence) {
            rq.enqueue(s);
        }

        for (int i = 1; i <= k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
