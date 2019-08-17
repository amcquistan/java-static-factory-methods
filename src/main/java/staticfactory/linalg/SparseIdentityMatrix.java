
package staticfactory.linalg;

public class SparseIdentityMatrix implements IMatrix {

    private final int dimension;

    public SparseIdentityMatrix(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public int getRows() {
        return dimension;
    }

    @Override
    public int getCols() {
        return dimension;
    }

    @Override
    public IVector getRow(int row) {
      return new IdentityMatrix(dimension).getRow(row);
    }

    @Override
    public IVector getCol(int col) {
        return getRow(col);
    }

    @Override
    public IMatrix matmul(IMatrix m) {
        return new IdentityMatrix(dimension).matmul(m);
    }

    @Override
    public IMatrix transpose() {
        return new IdentityMatrix(dimension).transpose();
    }

    @Override
    public double[][] toArray() {
        return new IdentityMatrix(dimension).toArray();
    }
}
