
package staticfactory;

import staticfactory.linalg.Matrix;
import staticfactory.linalg.SparseIdentityMatrix;
import staticfactory.linalgjane.IMatrixJane;

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
        // var ident2x2 = new IdentityMatrix(2);
        var ident2x2 = new SparseIdentityMatrix(2);

        var mat2x3 = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });


        // var mat2x2 = IMatrixJane.from(new double[][]{
        //     {1, 2},
        //     {3, 4}
        // });

        // var ident2x2 = IMatrixJane.identity(2);

        // var mat2x3 = IMatrixJane.from(new double[][]{
        //     {1, 2, 3},
        //     {4, 5, 6},
        // });

        var result1 = mat2x2.matmul(ident2x2).matmul(mat2x3);
        System.out.println(result1);













        /**
         * MAJOR PRESS RELEASE FROM LINALG LIBRARY MAINTAINER
         */

        // library maintainer does a release that says "hey, just came out with more
        // efficient way to do matmul with identity matrixes (because thats really popular
        // popular these days) but, you need to use the IdentityMatrix class

        // var ident2x2 = new IdentityMatrix(2);

























        /**
         * SECOND MAJOR PRESS RELEASE FORM LINALG LIBRARY MAINAINER
         */

        // new press release from library maintainer, "Hey, with all this matmul happening with
        // identity matrices lately I came up with another great idea ...., the SparseIdentityMatrix
        // which can dramatically cut down on memory footprint"

        // var ident2x2 = new SparseIdentityMatrix(2);

























        /**
         * LOOK AT ALL THE HASTLE I COULD HAVE SAVED YOU POOR PEOPLE IF I JUST USED STATIC FACTORY 
         * METHOD FOR CREATING IDENTITY MATRICES!!!!
         */

        // var ident2x2_better = IMatrix.identity(2);

        // System.out.println(mat2x2.matmul(ident2x2_better).matmul(mat2x3));
    }
}
