package carlos.Utils;

public class ArrayUtils {

    public static int[][] getSubmatrix(int[][] matrix, int minX, int minY, int maxX, int maxY){
        int[][] submatrix = new int[maxX-minX][maxY-minY];
        for(int i = minX; i < maxX; i++){
            for(int j = minY; j < maxY; j++){
                submatrix[i-minX][j-minY] = matrix[i][j];
            }
        }
        return submatrix;
    }
}
