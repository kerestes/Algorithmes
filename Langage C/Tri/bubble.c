#include<stdio.h>
#include<time.h>

void triBubble(int *tableau, int len);

int main(){
    int arrayTest[300000];
    int valeur = 300000;
    

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = valeur;
        valeur--;
    }

    clock_t debut = clock();

    int len = sizeof(arrayTest)/sizeof(arrayTest[0]);
    triBubble(arrayTest, len);

    clock_t fin = clock();

    printf("%f \n", (double)(fin - debut)/CLOCKS_PER_SEC);

    /*for(int i=0; i<sizeof(arrayTest)/sizeof(int); i++){
        printf("Elemento original %i  \n", arrayTest[i]);
    }*/

    return 0;
}

void triBubble(int *tableau, int len){
    for(int i=len; i>0; i--){
        for (int j=1; i>=j; j++){
            if (tableau[j] < tableau[j-1]){
                tableau[j] = tableau[j] + tableau[j-1];
                tableau[j-1] = tableau[j] - tableau[j-1];
                tableau[j] = tableau[j] - tableau[j-1];
            } 
        }
    }
}
