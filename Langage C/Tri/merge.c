#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int * triFusion(int *tableau, int deb, int fin);

int main(){
    int arrayTest[1000000];
    int valeur = 1000000;

    for (int i = 0; i<sizeof(arrayTest)/sizeof(int); i++){
        arrayTest[i] = valeur;
        valeur--;
    }

    clock_t debut = clock();

    int len = (int) (sizeof(arrayTest)/sizeof(int));
    int teste[len];
    triFusion(arrayTest, 0, len-1);

    clock_t fin = clock();

    printf("%f \n", (double)(fin - debut)/CLOCKS_PER_SEC);

    /*for(int i=0; i<sizeof(arrayTest)/sizeof(int); i++){
        printf("Elemento ordenado %i  \n", arrayTest[i]);
    }*/

    return 0;
}

int * triFusion(int * tableau, int deb, int fin){
    if(deb<fin){
        int moyen = ((fin-deb)/2) + deb;
        triFusion(tableau, deb, moyen);
        triFusion(tableau, moyen+1, fin);
        int G[moyen-deb+1];
        int D[fin-moyen];
        for(int i=0; i<=moyen-deb; i++){
            D[i] = tableau[i+deb];
        }
        for(int i=0; i<fin-moyen; i++){
            G[i] = tableau[moyen+1+i];
        }
        int j=0, k=0;
        for(int i=deb; i<=fin; i++){
            if(j<=moyen-deb && G[j]<D[k]){
                tableau[i] = G[j];
                j++;
            } else {
                tableau[i] = D[k];
                k++;
            }
        }
    }
}