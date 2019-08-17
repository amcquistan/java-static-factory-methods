
package staticfactory.linalg;

import java.util.Arrays;
import java.util.StringJoiner;

public class Matrix implements IMatrix {
    protected final double[][] data;

    public Matrix(double[][] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    public int getRows() {
        return data.length;
    }

    public int getCols() {
        return data[0].length;
    }

    public IVector getRow(int row) {
        return new Vector(data[row]);
    }

    public IVector getCol(int col) {
        double[] colData = new double[getRows()];
        for (int row = 0; row < getRows(); row++) {
            colData[row] = data[row][col];
        }
        return new Vector(colData);
    }

    public IMatrix matmul(IMatrix mat) {
        checkMatMulCompatibility(mat);

        // identity matmul does nothting so ... do nothing ...
        if (mat instanceof IdentityMatrix) {
            return this;
        }

        IMatrix trans = mat.transpose();
        double[][] resultArr = new double[getRows()][mat.getCols()];
        for (int row = 0; row < getRows(); row++) {
            IVector r = getRow(row);
            for (int col = 0; col < mat.getCols(); col++) {
                IVector v = trans.getRow(col);
                resultArr[row][col] = r.dot(v);
            }
        }
        return new Matrix(resultArr);
    }

    public IMatrix transpose() {
        double[][] transposed = new double[getCols()][getRows()];
        for (int col = 0; col < getCols(); col++) {
            for (int row = 0; row < getRows(); row++) {
                transposed[col][row] = data[row][col];
            }
        }
        return new Matrix(transposed);
    }

    @Override
    public double[][] toArray() {
        return Arrays.copyOf(data, data.length);
    }

    private void checkMatMulCompatibility(IMatrix m) throws ArithmeticException {
      if (getCols() != m.getRows()) {
          String s = "Incompatible matrix dimensions [" + getRows() + " x " + getCols() + "] vs [" + m.getRows() + " x " + m.getCols() + "]";
          throw new ArithmeticException(s);
      }
  }

  @Override
  public String toString() {
      String s = getClass().getSimpleName() + "[" + getRows() + " x " + getCols() + "]\n  [\n";
      StringJoiner sj = new StringJoiner(",\n");

      int row = 0;
      for (double[] vData : data) {
          IVector v = new Vector(vData);
          if (v != null && v.size() > 0) {
              if (getRows() > 6 && (row < 2 || row > (getRows() - 3))) {
                  sj.add(String.format("%6d", row) + ": " + v);
              } else if (getRows() > 6 && row == 2) {
                  sj.add("       ... ");
              } else if (getRows() <= 6) {
                  sj.add(String.format("%6d", row) + ": " + v);
              }
              row++;
          }
      }

      return s + sj + "\n  ]";
  }
}
