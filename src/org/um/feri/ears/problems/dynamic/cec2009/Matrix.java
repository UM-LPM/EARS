package org.um.feri.ears.problems.dynamic.cec2009;

// orthogonal rotation matrix
public class Matrix {

    public int col, row;    // matrix size
    private double[][] data;    // value of each element

    public Matrix(int numberOfDimension) {
        row = numberOfDimension;
        col = numberOfDimension;
        if (row == 0) {
            throw new IllegalArgumentException("if (row == 0) return;");
        }
        data = new double[row][col];
    }

    public Matrix(final int col, final int row) {
        this.col = col;
        this.row = row;
        this.data = new double[row][col];
    }

    public Matrix(Matrix other) {
        this.col = other.col;
        this.row = other.row;
        this.data = other.data;
    }

    public Matrix copy(final Matrix m) {
        if (row != m.row || col != m.col) {
            return this;
        }
        if (this == m) return this;
        row = m.row;
        col = m.col;
        this.data = new double[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(m.data[i], 0, this.data[i], 0, col);
        }
        return this;
    }

    public Matrix multiply(Matrix m) {
        if (col != m.row) return this;
        Matrix r = new Matrix(m.col, row);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < m.col; j++) {
                r.data[i][j] = 0.0;
                for (int k = 0; k < col; k++) {
                    r.data[i][j] += data[i][k] * m.data[k][j];
                }
            }
        }
        col = r.col;
        row = r.row;
        data = r.data;
        return this;
    }

    public boolean identity() {
        if (row != col) {
            return false;
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (j != i) {
                    data[i][j] = 0.0;
                } else {
                    data[i][j] = 1.0;
                }
            }
        }
        return true;
    }

    public void setRotation(final int r, final int c, final double angle) {
        identity();
        data[r][r] = Math.cos(angle);
        data[r][c] = -Math.sin(angle);
        data[c][r] = Math.sin(angle);
        data[c][c] = Math.cos(angle);
    }

    public void setData(final double[] d, final int c) {
        setData(d, c, 1);
    }

    public void setData(final double[] d, final int c, final int r) {
        if (r != row)
            return;
        for (int i = 0; i < row; i++) {
            if (col >= 0) System.arraycopy(d, 0, data[i], 0, col);
        }
    }

    public double[][] getData() {
        return data;
    }

    // used for debug
    public void print() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}