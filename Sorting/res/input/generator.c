#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "twister.h"

#define VALUES_PER_LINE 8

/*************************************************************
 * Prints N random integers between 0 and W-1.
 * Use the Mersenne twister to generate pseduo-random integers.
 *
 * Compilation: gcc generator.c twister.c
 * Calling syntax:  a.out N W [seed]
 *
 * Optional third parameter seed sets initial value of
 * pseudo-random number generator. If no such parameter, set
 * it arbitrarily using the system clock.
 *************************************************************/


/***************************************************
 * return a random integer between 0 and n-1
 ***************************************************/
int randomInteger(int n) {
    return genrand() % n;
}

int main(int argc, char *argv[]) {
   int i;
   int N = 0;           // number of item to print
   int W = 0;           // generate random integers between 0 and W-1
   unsigned int seed;   // seed for pseudo-random number generator
   int width;           // printing width per integer

   if (argc != 3 && argc != 4) {
      printf("Calling syntax: %s N W [seed]\n", argv[0]);
      exit(EXIT_FAILURE);
   }

   // read in number of item and maximum value from command line
   N = atoi(argv[1]);
   assert(N >= 0);
   W = atoi(argv[2]);
   assert(W >= 1);
   if (W == 1) width = 1;          
   else width = 1 + log10(W-1);      // fancy trick
   
   // set seed
   if (argc == 4) seed = atoi(argv[3]);
   else seed = time(NULL);
   sgenrand(seed);
    
   // print results, 8 per line
   for (i = 0; i < N; i++) {
       if (i % VALUES_PER_LINE == 0) printf("\n");

       // use that cool * feature of printf
       printf("%*d ", width, randomInteger(W));
   }
   printf("\n");

   return 0;
}
