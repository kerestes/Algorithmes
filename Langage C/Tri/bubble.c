#include<stdio.h>
#include<time.h>

void triBubble(int *tableau, int len);

int main(){
    int arrayTest[1000000];
    int valeur = 1000000;
    

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = valeur;
        valeur--;
    }

    clock_t start = clock();

    int len = sizeof(arrayTest)/sizeof(arrayTest[0]);
    triBubble(arrayTest, len);

    clock_t end = clock();

    printf("%f \n", (double)(end - start)/CLOCKS_PER_SEC);

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
