#include<stdio.h>
#include<time.h>

void triInsertion(int *tableau, int length);

int main(){
    int arrayTest[1000000];
    int valeur = 1000000;
    

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = valeur;
        valeur--;
    }

    clock_t debut = clock();

    int len = sizeof(arrayTest)/sizeof(arrayTest[0]);
    triInsertion(arrayTest, len);

    clock_t fin = clock();

    printf("%f \n", (double)(fin - debut)/CLOCKS_PER_SEC);

    /*for(int i=0; i<sizeof(arrayTest)/sizeof(int); i++){
        printf("Elemento original %i  \n", arrayTest[i]);
    }*/

    return 0;
}

void triInsertion(int *tableau, int length){
    
    for(int i=1; i<length; i++){
        int j=i;
            do{
                j--;
            }while(j>0 && tableau[i]<tableau[j]);
            if(tableau[i]<tableau[j]){
                int temp = tableau[i];
                for(int k=i; k>j; k--){
                    tableau[k] = tableau[k-1];
                }
                tableau[j] = temp;
            }
    }
}