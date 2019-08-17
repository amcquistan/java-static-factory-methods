
package staticfactory.linalgjane;

public interface IMatrixJane {

    public int getRows();

    public int getCols();

    public IVectorJane getRow(int row);
    
    public IVectorJane getCol(int col);

    public IMatrixJane matmul(IMatrixJane m);

    public IMatrixJane transpose();

    public double[][] toArray();

    public static IMatrixJane identity(int dimension) {

        // var data = new double[dimension][dimension];

        // for (int i = 0; i < dimension; i++) {
        //     data[i][i] = 1;
        // }

        // return new MatrixJane(data);

        // return new IdentityMatrixJane(dimension);

        return new SparseIdentityMatrixJane(dimension);
    }

    public static IMatrixJane from(double[][] data) {
        return new MatrixJane(data);
    }
}
