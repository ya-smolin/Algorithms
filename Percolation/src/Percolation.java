import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private boolean[][] isOpenGrid;
    private int size;

    /**
     * create n-by-n grid, with all sites blocked
     */
    public Percolation(int n) {
        size = n;
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        isOpenGrid = new boolean[n][n];
        // 2 extra cells: imaginary source and outlet
        grid = new WeightedQuickUnionUF(n * n + 2);
    }

    //TODO: Backwash bug fix
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);
        if (!isOpen(row, col)) {
            openCell(row, col);
            tryToFlowToSource(row, col);
            tryToFlowToOutlet(row, col);
            tryToFlow(row, col, row + 1, col);
            tryToFlow(row, col, row - 1, col);
            tryToFlow(row, col, row, col + 1);
            tryToFlow(row, col, row, col - 1);
        }
    }

    private void tryToFlowToSource(int row, int col) {
        if (row == 1) {
            grid.union(toPlaneIndex(row, col), getSourcePlaneIndex());
        }
    }

    private void tryToFlowToOutlet(int row, int col) {
        if (row == size) {
            grid.union(toPlaneIndex(row, col), getOutletPlaneIndex());
        }
    }

    private void tryToFlow(final int srcRow, final int srcCol, final int outRow, final int outCol) {
        int outletPlaneIndex = toPlaneIndex(outRow, outCol);
        int sourcePlaneIndex = toPlaneIndex(srcRow, srcCol);
        if (sourcePlaneIndex != -1 && outletPlaneIndex != -1) {
            grid.union(sourcePlaneIndex, outletPlaneIndex);
        }
    }

    private int getSourcePlaneIndex() {
        return size * size;
    }

    private int getOutletPlaneIndex() {
        return size * size + 1;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return isOpenGrid[row - 1][col - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if(!isOpen(row, col)) {
            return false;
        }
        return grid.connected(toPlaneIndex(row, col), getSourcePlaneIndex());
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(getOutletPlaneIndex(), getSourcePlaneIndex());
    }

    private void checkBounds(int row, int col) {
        if (!checkSizeCorrectness(row, col)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void openCell(final int row, final int col) {
        isOpenGrid[row - 1][col - 1] = true;
    }

    /**
     * if the vertice is out of boundary or is closed
     */
    private int toPlaneIndex(int row, int col) {
        if (!checkSizeCorrectness(row, col) || !isOpen(row, col)) {
            return -1;
        } else {
            return (row - 1) * size + col - 1;
        }
    }

    private boolean checkSizeCorrectness(final int row, final int col) {
        return row >= 1 && row <= size && col >= 1 && col <= size;
    }
}
