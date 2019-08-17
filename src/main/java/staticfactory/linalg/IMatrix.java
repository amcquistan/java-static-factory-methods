
package staticfactory.linalg;

public interface IMatrix {

    public int getRows();

    public int getCols();

    public IVector getRow(int row);
    
    public IVector getCol(int col);

    public IMatrix matmul(IMatrix m);

    public IMatrix transpose();

    public double[][] toArray();
}
