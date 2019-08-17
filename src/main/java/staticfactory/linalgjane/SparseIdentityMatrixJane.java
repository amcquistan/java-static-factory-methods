
package staticfactory.linalgjane;

class SparseIdentityMatrixJane implements IMatrixJane {

    private final int dimension;

    public SparseIdentityMatrixJane(int dimension) {
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
    public IVectorJane getRow(int row) {
      return new IdentityMatrixJane(dimension).getRow(row);
    }

    @Override
    public IVectorJane getCol(int col) {
        return getRow(col);
    }

    @Override
    public IMatrixJane matmul(IMatrixJane m) {
        return new IdentityMatrixJane(dimension).matmul(m);
    }

    @Override
    public IMatrixJane transpose() {
        return new IdentityMatrixJane(dimension).transpose();
    }

    @Override
    public double[][] toArray() {
        return new IdentityMatrixJane(dimension).toArray();
    }
}
