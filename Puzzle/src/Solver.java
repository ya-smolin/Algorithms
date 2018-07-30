import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Objects;

public class Solver {
    private Stack<Board> solution = null;
    private BoardNode initBoardNode = null;

    private MinPQ<BoardNode> pq;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        initBoardNode = new BoardNode(initial, null);
        pq = new MinPQ<>();
        pq.insert(initBoardNode);
    }

    /**
     * is the initial board solvable?
     * Detecting unsolvable puzzles. Not all initial boards can lead to the goal board by a sequence of legal moves
     * To detect such situations, use the fact that boards are divided into two equivalence classes with respect to reachability:
     * (i) those that lead to the goal board and (ii) those that lead to the goal board if we modify the initial board by
     * swapping any pair of blocks (the blank square is not a block).
     * To apply the fact, run the A* algorithm on two puzzle instances—one with the initial board and one with
     * the initial board modified by swapping a pair of blocks—in lockstep
     * (alternating back and forth between exploring search nodes in each of the two game trees).
     * Exactly one of the two will lead to the goal board.
     */
    public boolean isSolvable() {
        //Solver separateEqSolver = new Solver(initBoardNode.getBoard().twin());
        return solution() != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        Iterable<Board> solutionMoves = solution();
        if (solutionMoves == null) return -1;
        else {
            Stack<Board> solutionMovesB = (Stack<Board>) solutionMoves;
            return solutionMovesB.size() - 1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        BoardNode lastBoardNode = null;
        if (solution == null) {
            ArrayList<Board> expendedBoards = new ArrayList<>();
            while (!pq.isEmpty()) {
                BoardNode curBoardNode = pq.delMin();
                expendedBoards.add(curBoardNode.getBoard());
                if (curBoardNode.isGoal()) {
                    lastBoardNode = curBoardNode;
                    break;
                } else {
                    Iterable<Board> neighbors = curBoardNode.getBoard().neighbors();
                    for (Board neighbor : neighbors) {
                        if (!expendedBoards.contains(neighbor)) {
                            BoardNode neighborNode = new BoardNode(neighbor, curBoardNode);
                            pq.insert(neighborNode);
                        }
                    }
                }
            }

            if (lastBoardNode == null){
                solution = null;
            } else {
                solution = new Stack<>();
                for (BoardNode curBoardNode = lastBoardNode; curBoardNode != null; curBoardNode = curBoardNode.getPrevBoard()) {
                    solution.push(curBoardNode.getBoard());
                }
            }
        }
        return solution;
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
        for (Board board : solver.solution())
            StdOut.println(board);
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

class BoardNode implements Comparable<BoardNode> {
    private Board board;
    private BoardNode prevBoard;
    private int movesNumber;
    public BoardNode(Board board, BoardNode prevBoard) {
        this.movesNumber = 0;
        this.board = board;
        this.prevBoard = prevBoard;
        if(prevBoard == null){
            movesNumber = 0;
        } else {
            movesNumber = prevBoard.getMovesNumber() + 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardNode boardNode = (BoardNode) o;
        return Objects.equals(board, boardNode.board);
    }

    private int getMovesNumber() {
        return movesNumber;
    }


    private int getPriority() {
        return board.hamming() + movesNumber;
    }

    public Board getBoard() {
        return board;
    }

    public BoardNode getPrevBoard() {
        return prevBoard;
    }

    public boolean isGoal() {
        return board.isGoal();
    }

    @Override
    public int compareTo(BoardNode o) {
        if (this.getPriority() == o.getPriority()) return 0;
        else if (this.getPriority() > o.getPriority()) return 1;
        else return -1;
    }
}