#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int * triRapide(int *tableau, int deb, int fin);

int main(){
    int arrayTest[1000000];

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = rand() % 1000000;
    }

    clock_t debut = clock();

    int len = (int) (sizeof(arrayTest)/sizeof(int));
    int teste[len];
    triRapide(arrayTest, 0, len-1);

    clock_t fin = clock();

    printf("%f \n", (double)(fin - debut)/CLOCKS_PER_SEC);

    /*for(int i=0; i<sizeof(arrayTest)/sizeof(int); i++){
        printf("Elemento ordenado %i  \n", arrayTest[i]);
    }*/

    return 0;
}

int * triRapide(int * tableau, int deb, int fin){
    if(deb<fin){
        int pivot, i, j;
        pivot = deb;
        i=deb;
        j = fin;
        while(i<j){
            while(tableau[i] <= tableau[pivot] && i<fin)
                i++;
            while(tableau[j]>tableau[pivot])
                j--;
            if (i<j){
                tableau[j] = tableau[j] + tableau[i];
                tableau[i] = tableau[j] - tableau[i];
                tableau[j] = tableau[j] - tableau[i];
            }
        }
        if(pivot!=j){
            tableau[j] = tableau[j] + tableau[pivot];
            tableau[pivot] = tableau[j] - tableau[pivot];
            tableau[j] = tableau[j] - tableau[pivot];
        }

        triRapide(tableau, deb, j-1);
        triRapide(tableau, j+1, fin);
    }
}