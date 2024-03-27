package Tri;

import java.util.Arrays;

public class triSelection {
    public static void main(String[] args) {
        int taille = 200000;
        int[] arrayTest = new int[taille];
        int nombre = taille;
        for(int i=0; i<taille; i++){
            arrayTest[i] = nombre;
            nombre--;
        }

        long start = System.currentTimeMillis();
        triSelectionAlgorithme(arrayTest);
        long end = System.currentTimeMillis();

        System.out.println("Temps : " + (end-start));

        //Arrays.stream(arrayTest).forEach(System.out::println);
    }

    static void triSelectionAlgorithme(int array[]){
        for(int i=0; i<array.length; i++){
            int index = i;
            for(int j=i; j<array.length; j++){
                if (array[j] < array[index]){
                    index = j;
                }
            }
            if (i!=index){
                array[i] = array[i] + array[index];
                array[index] = array[i] - array[index];
                array[i] = array[i] - array[index];
            }
        }
    }
}

