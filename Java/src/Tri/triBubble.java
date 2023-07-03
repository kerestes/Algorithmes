package Tri;

import java.util.Arrays;

public class triBubble {
    public static void main(String[] args) {
        int taille = 200000;
        int[] arrayTest = new int[taille];
        int nombre = taille;
        for(int i=0; i<taille; i++){
            arrayTest[i] = nombre;
            nombre--;
        }

        long start = System.currentTimeMillis();
        triBubbleAlgorithme(arrayTest);
        long end = System.currentTimeMillis();

        System.out.println("Temps : " + (end-start));

        //Arrays.stream(arrayTest).forEach(System.out::println);
    }

    static void triBubbleAlgorithme(int array[]){
        for(int i=array.length-1; i>0; i--){
            for(int j=0; j<i; j++){
                if(array[j]>array[j+1]){
                    array[j] = array[j] + array[j+1];
                    array[j+1] = array[j] - array[j+1];
                    array[j] = array[j] - array[j+1];
                }
            }
        }
    }
}

