#include<stdio.h>
#include<time.h>

void triSelection(int * tableau, int len);

int main(){
    int arrayTest[1000000];
    int valeur = 1000000;

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = valeur;
        valeur--;
    }

    int len = sizeof(arrayTest)/sizeof(arrayTest[0]);

    clock_t debut = clock();

    triSelection(arrayTest, len);

    clock_t fin = clock();

    printf("%f \n", (double)(fin - debut)/CLOCKS_PER_SEC);

    /*for(int i=0; i<sizeof(arrayTest)/sizeof(int); i++){
        printf("Elemento ordenado %i  \n", arrayTest[i]);
    }*/

    return 0;
}

void triSelection(int * tableau, int len){
    int index = -1;
    for (int i=0; i<len; i++){
        index = i;
        for(int j=i+1; j<len; j++){
            if (tableau[j]<tableau[index]){
                index = j;
            }
        }
        if (index!=i){
            tableau[i] = tableau[i] + tableau[index];
            tableau[index] = tableau[i] - tableau[index];
            tableau[i] = tableau[i] - tableau[index];
        }
    }
}