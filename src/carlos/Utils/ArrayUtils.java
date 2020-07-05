package carlos.Utils;

import carlos.helper.Example;

import java.util.ArrayList;

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

    public static ArrayList<ArrayList<Double>> matrixToArrayList(int[][] array){
        ArrayList<ArrayList<Double>> arrayLists = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            ArrayList<Double> aux = new ArrayList<>();
            for(int j = 0; j < array[0].length; j++){
                aux.add(j, (double) array[i][j]);
            }
            arrayLists.add(i, aux);
        }
        return arrayLists;
    }

    public static ArrayList<Double> arrayToArrayList(int[] array){
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            arrayList.add(i, (double) array[i]);
        }
        return arrayList;
    }
    public static ArrayList<Double> arrayToArrayList(float[] array){
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            arrayList.add(i, (double) array[i]);
        }
        return arrayList;
    }

    public static <T> T[] addAll(T[] first, T[] second) {
        T[] all = (T[]) new Example[first.length + second.length];
        int i = 0;
        for(int j = 0; j < first.length; j++){
            all[i] = first[j];
            i++;
        }
        for(int k = 0; k < second.length; k++){
            all[i] = second[k];
            i++;
        }
        return all;
    }
}
