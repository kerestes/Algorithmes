package Tri;

import java.util.Arrays;

public class triInsertion {
    public static void main(String[] args) {
        int taille = 200000;
        int[] arrayTest = new int[taille];
        int nombre = taille;
        for(int i=0; i<taille; i++){
            arrayTest[i] = nombre;
            nombre--;
        }

        long start = System.currentTimeMillis();
        triInsertionAlgorithme(arrayTest);
        long end = System.currentTimeMillis();

        System.out.println("Temps : " + (end-start));

        //Arrays.stream(arrayTest).forEach(System.out::println);
    }

    static void triInsertionAlgorithme(int array[]){
        for(int i=1; i<array.length; i++){
            int j=i;
            do{
                j--;
            }while(j>0 && array[i]<array[j]);
            if(array[i]<array[j]){
                int temp = array[i];
                for(int k=i; k>j; k--){
                    array[k] = array[k-1];
                }
                array[j] = temp;
            }
        }
    }
}
