# Making Java Static Factories Work For You

### Introduction

In this article I present an argument for using static factory methods to create objects (class instances) in Java, as opposed to the traditional new keyword and constructor duo, for the purpose of providing clarity, flexibility and, maintainability in designing Object Oriented programs.

My argument for using static factory methods will rely on the following key points:

* they abstract away what Class is actually being returned by the method
* they allow for hiding the actual implementation classes when used with an interface
* changes to the implementation classes being returned usually don't require changes in client code calling the static factory methods

### Making the Point with a Contrived Example

To build my argument for using static factory methods for creating objects, as opposed to the traditional use of new along with a constructor, I use a contrived dummy library for performing matrix algebra operations. To limit the complexity of my examples I limit the operations to matrix multiplication and generation of regular as well as identity matrices.

Below is the general class diagram for my fake linalg package library that use the traditional approach of creating objects in client code using the new keyword of publically accessible classes.

*** static-factories.png ***

### First Release of the Fake linalg Library

Lets say the creator of the fake linalg library, John Doe, has implemented the above class diagram and released the first version of this library to the masses. I, a Java coder, grab this library and enthusiastically start developing with it in a few production system's code bases making heavy use of matrix multiplication, particularly with identity matrices because I hear thats really popular these days (bad mathy joke ... I just can't help myself). 

```
package staticfactory;

import staticfactory.linalg.Matrix;
import staticfactory.linalg.SparseIdentityMatrix;

public class App {
    public static void main(String[] args) {
        var mat2x2 = new Matrix(new double[][]{
            {1, 2},
            {3, 4}
        });

        var ident2x2 = new Matrix(new double[][]{
            {1, 0},
            {0, 1}
        });

        var mat2x3 = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });

        var result1 = mat2x2.matmul(ident2x2).matmul(mat2x3);
        System.out.println(result1);
    }
}
```

Output from running example.

```
> Task :run
Matrix[2 x 3]
  [
     0: Vector[3] => [    9.0000,   12.0000,   15.0000],
     1: Vector[3] => [   19.0000,   26.0000,   33.0000]
  ]
```

Above is just one class where I've used this new linalg library as well as probably a dozen other files just like it using the same technique where I sandwhich all matrix multiplication ops with a identity matrix because I read somewhere it was really important to do this (DON'T EVER DO THIS ITS JUST A JOKE PEOPLE).

I now go home satisfied knowing that the systems are hard at work cranking out matrix ops albet a bit slow for some unknown reason.

## Maintainer of Fake linalg Library Quickly Releases a New Version

Well it turns out that many other devs have picked up this library also and are similarly doing copious amounts of matrix mutiplication ops similarly sandwiching in identity matrices between them. The maintainer of fake linalg library is pleased with himself but, like any good opensource library developer starts to look for ways to improve the performance of the code and immediately spots an area of improvement. After doing many such matrix multiplication ops with identity matrices he realizes that no matter what the result is always the same so, he says, "what if I introduce a IdentityMatrix class that when used for multiplication just skips the operation and returns the matrix?"

Then he said, "Let it be so". 

Below is his newly implemented IdentityMatrix class

```
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
    
    // ... omitting everything else but matmul

    @Override
    public IMatrix matmul(IMatrix m) {
        // identity matmul does not so ... do nothing
        return m;
    }
}
```

and, here is the updated matmul method of the Matrix class.

```

package staticfactory.linalg;

import java.util.Arrays;
import java.util.StringJoiner;

public class Matrix implements IMatrix {
    protected final double[][] data;

    public Matrix(double[][] data) {
        this.data = Arrays.copyOf(data, data.length);
    }

    // ... omitting everything else but matmul

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
}
```


*** identity-matrix.png ***

I hear about the new release of the fake linalg library and go to town upgrading all the places in my code where I was explicitly constructing an identity matrix from an array using the Matrix class. 

Whew ... all done. 

And what do you know, CPU utilization has dropped dramatically on the servers running this new version of the code because, afterall I did update things to include about 30 instances of new IdentityMatrix(int) in all the various places it was needed (eek, what a day!).

Here is my updated example from before.

```
package staticfactory;

import staticfactory.linalg.IdentityMatrix;
import staticfactory.linalg.Matrix;

public class App {
    public static void main(String[] args) {
        var mat2x2 = new Matrix(new double[][]{
            {1, 2},
            {3, 4}
        });

        // var ident2x2 = new Matrix(new double[][]{
        //     {1, 0},
        //     {0, 1}
        // });
        
        /**
         * MAJOR PRESS RELEASE FROM LINALG LIBRARY MAINTAINER
         *
         * library maintainer does a release that says "hey, just came out with more
         * efficient way to do matmul with identity matrixes (because thats really popular
         * popular these days) but, you need to use the IdentityMatrix class
        */

        var ident2x2 = new IdentityMatrix(2);

        var mat2x3 = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });

        var result1 = mat2x2.matmul(ident2x2).matmul(mat2x3);
        System.out.println(result1);
    }
}
```

Output from running example.

```
> Task :run
Matrix[2 x 3]
  [
     0: Vector[3] => [    9.0000,   12.0000,   15.0000],
     1: Vector[3] => [   19.0000,   26.0000,   33.0000]
  ]
```

Again, I go home satisfied with the increased performance I just made possible with a hard days work of swapping in the IdentityMatrix class.

## Maintainer of Fake linalg Library Releases another New Version

After celebrating for a few weeks due to the enourmous success of his linalg library the Maintainer, John Doe, thinks to himself, "it sure seems like those identity matrices that people are using are waisting a lot of memory storing all those zero filled arrays, I bet I can improve that by using a empty class that only creates the data backing double array when its actually needed". And again, he introduces another new class he names SparseIdentityMatrix (shown below) and releases another version of the library.

```
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
```


*** updated class diagram ***

Still high on my laurels from all the CPU improvements I can brag about after implementing the IdentityMatrix class for all the systems I'm developing on I am eager to upgrade to the latest linalg release and swap out the IdentityMatrix usage for the SparseIdentityMatrix class. I go to work making the changes, build, and deploy to the servers. Again, it was a bit of work making the changes but, the improved memory usage is really paying off and my boss is very happy with me (turns out she doesn't believe in code reviews).

Here is my yet again updated example from before.

```

package staticfactory;

import staticfactory.linalg.IdentityMatrix;
import staticfactory.linalg.Matrix;
import staticfactory.linalg.SparseIdentityMatrix;

public class App {
    public static void main(String[] args) {
        var mat2x2 = new Matrix(new double[][]{
            {1, 2},
            {3, 4}
        });

        // var ident2x2 = new Matrix(new double[][]{
        //     {1, 0},
        //     {0, 1}
        // });
        
        /**
         * MAJOR PRESS RELEASE FROM LINALG LIBRARY MAINTAINER
         *
         * library maintainer does a release that says "hey, just came out with more
         * efficient way to do matmul with identity matrixes (because thats really popular
         * popular these days) but, you need to use the IdentityMatrix class
        */
        
        // var ident2x2 = new IdentityMatrix(2);
        
        
        /**
         * SECOND MAJOR PRESS RELEASE FORM LINALG LIBRARY MAINAINER
         * 
         * new press release from library maintainer, "Hey, with all this matmul happening with
         * identity matrices lately I came up with another great idea ...., the SparseIdentityMatrix
         * which can dramatically cut down on memory footprint"
         */

        var ident2x2 = new SparseIdentityMatrix(2);

        var mat2x3 = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });

        var result1 = mat2x2.matmul(ident2x2).matmul(mat2x3);
        System.out.println(result1);
    }
}
```

Output from running example 

```
> Task :run
Matrix[2 x 3]
  [
     0: Vector[3] => [    9.0000,   12.0000,   15.0000],
     1: Vector[3] => [   19.0000,   26.0000,   33.0000]
  ]
```

## Enter the Static Factory Method

Ok, lets change the story a bit. Lets say that at the same time Jane Doe was creating a similar fake linalg library (Jane is John's brighter sister). She takes a different approach by implementing a static factory methods right in the IMatrix interface that creates a Matrix instance from an array of doubles plus an identity method that returns a Matrix as well. Also, she hides her Matrix and Vector implementation classes from being used directly by declaring them package private (ie, no explicit access modifiers in the class signature). This has the effect of not allowing the users of her library to instantiate the implementation classes with the new keyword and their constructors.

For Jane's first release the class diagram would look like this. Note that Jane likes to suffix her type names with Jane to make sure people know it's her code.




The IMatrixJane interface's code would then look like this.

```
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

        var data = new double[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            data[i][i] = 1;
        }

        return new MatrixJane(data);

        // return new IdentityMatrixJane(dimension);

        // return new SparseIdentityMatrixJane(dimension);
    }

    public static IMatrixJane from(double[][] data) {
        return new MatrixJane(data);
    }
}
```

And, if I had choosen to use her version of the library my example would look like so.

 
```
package staticfactory;

import staticfactory.linalgjane.IMatrixJane;

public class App {
    public static void main(String[] args) {

        var mat2x2 = IMatrixJane.from(new double[][]{
            {1, 2},
            {3, 4}
        });

        var ident2x2 = IMatrixJane.identity(2);

        var mat2x3 = IMatrixJane.from(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });

        var result1 = mat2x2.matmul(ident2x2).matmul(mat2x3);
        System.out.println(result1);

    }
}
```

Now in this implementation using static factory methods to create new instances of matrices Jane is able to make the same changes that John did when I walked through my fake timeline example but, each time she releases a new update I actually don't need to do anything more than swap out my jars in my Maven POM or Gradle build.gradle dependencies.

For example, in the first library change Jane's update would be implemented in the identity static factory method like so.

```
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
        //    data[i][i] = 1;
        // }

        // return new MatrixJane(data);

        return new IdentityMatrixJane(dimension);
    }

    public static IMatrixJane from(double[][] data) {
        return new MatrixJane(data);
    }
}
```

Since the change is abstracted away in the use of the IdentityMatrixJane within the identity method I would not have to change a single line of code to gain these new "efficiencies".

Similarly, in the second change Jane's updates are again isolated to the IMatrixJane#identity method so, I would only need to update my jars not any actual code.

```
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
        //    data[i][i] = 1;
        // }

        // return new MatrixJane(data);

        // return new IdentityMatrixJane(dimension);

        return new SparseIdentityMatrixJane(dimension);
    }

    public static IMatrixJane from(double[][] data) {
        return new MatrixJane(data);
    }
}
```

### Conclusion




 
 

