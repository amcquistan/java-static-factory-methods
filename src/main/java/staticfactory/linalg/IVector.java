package staticfactory.linalg;

public interface IVector {

    public double get(int i);

    public int size();

    public double dot(IVector v);
}