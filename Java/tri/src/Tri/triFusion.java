package Tri;

import java.util.Arrays;

public class triFusion {
    public static void main(String[] args) {
        int taille = 1000000;
        int[] arrayTest = new int[taille];
        int nombre = taille;
        for(int i=0; i<taille; i++){
            arrayTest[i] = nombre;
            nombre--;
        }

        long start = System.currentTimeMillis();
        triFusionAlgorithme(arrayTest, 0, arrayTest.length-1);
        long end = System.currentTimeMillis();

        System.out.println("Temps : " + (end-start));

        //Arrays.stream(arrayTest).forEach(System.out::println);
    }

    static void triFusionAlgorithme(int array[], int deb, int fin){
        if(deb<fin){
            int moyen = ((fin-deb)/2) + deb;
            triFusionAlgorithme(array, deb, moyen);
            triFusionAlgorithme(array, moyen+1, fin);
            int taille1 = moyen-deb+1;
            int taille2 = fin-moyen;
            int[] arrayTemp1= new int[taille1];
            int[] arrayTemp2 = new int[taille2];
            for(int i=0; i<taille1; i++){
                arrayTemp1[i] = array[deb + i];
            }
            for(int i=0; i<taille2; i++){
                arrayTemp2[i] = array[moyen + i +1];
            }
            int j=0, k=0;
            for(int i=deb; i<=fin; i++){
                if(k >= taille2 || (j<taille1 && arrayTemp1[j] < arrayTemp2[k])){
                    array[i] = arrayTemp1[j++];
                } else  {
                    array[i] = arrayTemp2[k++];
                }
            }
        }
    }
}

