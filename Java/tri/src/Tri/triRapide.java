package Tri;

import java.util.Arrays;

public class triRapide {
    public static void main(String[] args) {
        int taille = 100000;
        int[] arrayTest = new int[taille];
        int nombre = taille;
        for(int i=0; i<taille; i++){
            arrayTest[i] = nombre;
            nombre--;
        }

        long start = System.currentTimeMillis();
        triRapideAlgorithme(arrayTest, 0, arrayTest.length-1);
        long end = System.currentTimeMillis();

        System.out.println("Temps : " + (end-start));

        //Arrays.stream(arrayTest).forEach(System.out::println);
    }

    static void triRapideAlgorithme(int array[], int deb, int fin){
        if(deb<fin){
            int pivot = deb;
            int i = deb;
            int j = fin;
            while(i<j){
                while(i<fin && array[i]<array[pivot])
                    i++;
                while(j>deb && array[j]>array[pivot])
                    j--;
                if (i<j){
                    array[i] = array[i] + array[j];
                    array[j] = array[i] - array[j];
                    array[i] = array[i] - array[j];
                }
            }
            if (pivot!=j){
                array[pivot] = array[pivot] + array[j];
                array[j] = array[pivot] - array[j];
                array[pivot] = array[pivot] - array[j];
            }
            triRapideAlgorithme(array, deb, pivot-1);
            triRapideAlgorithme(array, pivot+1, fin);
        }
    }
}

