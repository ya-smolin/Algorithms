import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private Board board;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initialBoard) {
        board = initialBoard;
    }

    /**
     *  is the initial board solvable?
     *  Detecting unsolvable puzzles. Not all initial boards can lead to the goal board by a sequence of legal moves
     *  To detect such situations, use the fact that boards are divided into two equivalence classes with respect to reachability:
     *  (i) those that lead to the goal board and (ii) those that lead to the goal board if we modify the initial board by
     *  swapping any pair of blocks (the blank square is not a block).
     *  To apply the fact, run the A* algorithm on two puzzle instances—one with the initial board and one with
     *  the initial board modified by swapping a pair of blocks—in lockstep
     *  (alternating back and forth between exploring search nodes in each of the two game trees).
     *  Exactly one of the two will lead to the goal board.
     */
       public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {

        return 0;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
