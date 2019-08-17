
package staticfactory.linalgjane;

import java.util.Arrays;
import java.util.StringJoiner;

class VectorJane implements IVectorJane {

    public final double[] data;

    public VectorJane(int size) {
        data = new double[size];
    }

    public VectorJane(double[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    @Override
    public double get(int i) {
        return data[i];
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public double dot(IVectorJane v) {
        checkSizeCompatibility(v);

        double product = 0.0d;
        for (int i = 0; i < size(); i++) {
            product += (data[i] * v.get(i));
        }
        return product;
    }

    private void checkSizeCompatibility(IVectorJane v) throws ArithmeticException {
        if (this.size() != v.size())
            throw new ArithmeticException("Incompatible vector sizes " + v.size() + " and " + this.size());
    }

    @Override
    public String toString() {
        String s = getClass().getSimpleName() + "["  + this.size() + "] => ";
        StringJoiner sj = new StringJoiner(",");
        if (this.size() > 8) {
            for (int i = 0; i < 3; i++) {
                sj.add(String.format("%10.4f", this.data[i]));
            }
            sj.add("...");
            for (int i = this.size() - 3; i < this.size(); i++) {
                sj.add(String.format("%10.4f", this.data[i]));
            }
        } else {
            for (int i = 0; i < this.size(); i++) {
                sj.add(String.format("%10.4f", this.data[i]));
            }
        }
        return s + "[" + sj + "]";
    }
}
