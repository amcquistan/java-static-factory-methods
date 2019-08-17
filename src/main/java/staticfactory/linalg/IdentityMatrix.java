
package staticfactory.linalg;

import java.util.Arrays;

public class IdentityMatrix implements IMatrix {

    private final double[][] data;

    public IdentityMatrix(int dimension) {
        data = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = 1;
        }
    }

    @Override
    public int getRows() {
        return data.length;
    }

    @Override
    public int getCols() {
        return data[0].length;
    }

    @Override
    public IVector getRow(int row) {
        double[] vData = new double[row];
        vData[row] = 1;
        return new Vector(vData);
    }

    @Override
    public IVector getCol(int col) {
        return getRow(col);
    }

    @Override
    public IMatrix matmul(IMatrix m) {
        // identity matmul does not so ... do nothing
        return m;
    }

    @Override
    public double[][] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public IMatrix transpose() {
        return new Matrix(data).transpose();
    }
}
