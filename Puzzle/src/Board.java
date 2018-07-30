import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by Smolin on 21.12.2016.
 */
public class Board {
    private int dim;
    private int[][] blocks;
    private int hashCode = -1;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        dim = (blocks != null) ? blocks[0].length : 0;
        this.blocks = blocks;
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (!isEmptyCell(i, j) && !isInPlace(i, j)) hamming++;
            }
        }
        return hamming;
    }
    private boolean isEmptyCell(int i, int j){
       return blocks[i][j]==0;
    }

    //sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(!isInPlace(i,j) && !isEmptyCell(i, j)){
                    manhattan += calculateLocalManhattan(i, j);
                    System.out.println("{"+i +","+j+": "+manhattan+"}");
                }
            }
        }
        return manhattan;
    }

    private int calculateLocalManhattan(int is, int js) {
        ArrayInd canIndex = canonicalBoardIndex(blocks[is][js]);
        return Math.abs(is - canIndex.getI()) + Math.abs(js - canIndex.getJ());
    }

    private ArrayInd canonicalBoardIndex(int value) {
        int i = (value - 1) / dim;
        int j = (value - 1) % dim;
        return new ArrayInd(i, j);
    }

    private boolean isInPlace(int i, int j) {
        return blocks[i][j] == i * dim + j + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] twinBlocks = blocksClone();
        ArrayList<ArrayInd> swapInd = getTwoNonEmptyElements();
        swapElements(twinBlocks, swapInd.get(0), swapInd.get(1));
        return new Board(twinBlocks);
    }

    private void swapElements(int[][] twinBlocks, ArrayInd firstEl, ArrayInd secondEl) {
        int temp = twinBlocks[secondEl.getI()][secondEl.getJ()];
        twinBlocks[secondEl.getI()][secondEl.getJ()] = twinBlocks[firstEl.getI()][firstEl.getJ()];
        twinBlocks[firstEl.getI()][firstEl.getJ()] = temp;
    }

    private ArrayList<ArrayInd> getTwoNonEmptyElements() {
        final int NUMBER_OF_EMPTY_ELEMENTS = 2;
        ArrayList<ArrayInd> elemArr = new ArrayList<>();
        for (int i = 0; i < dim && elemArr.size() < NUMBER_OF_EMPTY_ELEMENTS; i++) {
            for (int j = 0; j < dim && elemArr.size() < NUMBER_OF_EMPTY_ELEMENTS; j++) {
                if (!isEmptyCell(i,j)) {
                    elemArr.add(new ArrayInd(i, j));
                }
            }
        }
        return elemArr;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null || getClass() != y.getClass()) return false;

        Board yBoard = (Board) y;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (yBoard.blocks[i][j] != blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        ArrayInd zeroInd = findValue(0);
        ArrayList<ArrayInd> vertNeib = getVerticalNeighbors(zeroInd);
        for (ArrayInd curNeibor : vertNeib) {
            int[][] neiborBlocks = blocksClone();
            swapElements(neiborBlocks, curNeibor, zeroInd);
            neighbors.add(new Board(neiborBlocks));
        }
        return neighbors;
    }

    private ArrayList<ArrayInd> getVerticalNeighbors(ArrayInd emptyCell) {
        ArrayList<ArrayInd> vertNeib = new ArrayList<>();
        if (emptyCell.getJ() > 0) {
            vertNeib.add(new ArrayInd(emptyCell.getI(), emptyCell.getJ() - 1));
        }
        if (emptyCell.getJ() < dim - 1) {
            vertNeib.add(new ArrayInd(emptyCell.getI(), emptyCell.getJ() + 1));
        }
        if (emptyCell.getI() > 0) {
            vertNeib.add(new ArrayInd(emptyCell.getI() - 1, emptyCell.getJ()));
        }
        if (emptyCell.getI() < dim - 1) {
            vertNeib.add(new ArrayInd(emptyCell.getI() + 1, emptyCell.getJ()));
        }
        return vertNeib;
    }

    @Override
    public int hashCode() {
        if(hashCode == -1){
            hashCode = Objects.hash(dim);
            for (int i = 0; i < dim; i++) {
                hashCode = 31*hashCode + Arrays.hashCode(blocks[i]);
            }
        }
        return hashCode;
    }

    private ArrayInd findValue(int value) {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (blocks[i][j] == value) return new ArrayInd(i, j);
            }
        }
        return null;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int[][] blocksClone() {
        int[][] clonedBlocks = new int[dim][];
        for (int i = 0; i < dim; i++) {
            clonedBlocks[i] = blocks[i].clone();
        }
        return clonedBlocks;
    }
    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] boardBlocks = {{1, 0, 2},
                {5, 4, 6},
                {7, 8, 3}};
        int[][] boardBlocks1 = {{1, 0, 2},
                {5, 4, 6},
                {7, 8, 3}};
        Board board = new Board(boardBlocks);
        Board board1 = new Board(boardBlocks1);
        System.out.println("equals:" + board1.equals(board) + " " + Objects.equals(board, board1));
        System.out.println("Arrays equals:" + boardBlocks1.equals(boardBlocks));
        System.out.println("hashcode:" + board.hashCode() + " " + board1.hashCode());
        HashSet<Board> boards = new HashSet<>();
        boards.add(board);
        System.out.println("contains:" + boards.contains(board1));

        System.out.println(board);
        //System.out.println(board.twin());
        System.out.println("hamming:" + board.hamming());
        System.out.println("manhattan:" + board.manhattan());

        Iterable<Board> neighbors = board.neighbors();
        for (Board neighbor : neighbors) {
            System.out.println(neighbor);
        }
    }
}

class ArrayInd {
    private int i;
    private int j;

    public ArrayInd(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayInd arrayInd = (ArrayInd) o;
        return i == arrayInd.i &&
                j == arrayInd.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }

    @Override
    public String toString() {
        return "{" + i + ", " + j + '}';
    }
}