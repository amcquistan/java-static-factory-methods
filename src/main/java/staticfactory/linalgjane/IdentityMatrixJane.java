
package staticfactory.linalgjane;

import java.util.Arrays;

class IdentityMatrixJane implements IMatrixJane {

    private final double[][] data;

    public IdentityMatrixJane(int dimension) {
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
    public IVectorJane getRow(int row) {
        double[] vData = new double[row];
        vData[row] = 1;
        return new VectorJane(vData);
    }

    @Override
    public IVectorJane getCol(int col) {
        return getRow(col);
    }

    @Override
    public IMatrixJane matmul(IMatrixJane m) {
        // identity matmul does not so ... do nothing
        return m;
    }

    @Override
    public double[][] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public IMatrixJane transpose() {
        return new MatrixJane(data).transpose();
    }
}
