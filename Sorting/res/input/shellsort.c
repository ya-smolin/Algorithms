#include <stdio.h>
#include <assert.h>

typedef enum { FALSE, TRUE } Boolean;

#define MAXPRINT 20
#define MAXN 1000000

// By declaring the array here, it is allocated on the heap instead
// of the stack, making it possible to use a huge array. Since the
// elements only initialized if they are actually used, there is
// effectively no overhead for declaring such a huge array.

int a[MAXN];           // the data
int N = 0;             // number of elements

/********************************************
 * Print out array.
 ********************************************/
void show(void) {
    int i;
    if (N <= MAXPRINT)
        for (i = 0; i < N; i++)
            printf("%d ", a[i]);
    printf("\n");
}


/********************************************
 * Array implementation of shellsort using
 * Knuth increment sequence.
 ********************************************/
int main(void) {
    int i, temp;
    int val;                     // input data
    int h;                       // shellsort increment
    int exch, totalexch;         // count exchanges
    int cmp, totalcmp;           // count key comparisons
    int pass, totalpass;         // count bubble h-sort passes
    Boolean done;                // flag to stop pass early if no exchanges

    // read in data
    while(scanf("%d", &val) == 1) {
        assert(N < MAXN);        // prevent buffer overflow
        a[N++] = val;
    }

    // header
    printf("Array implementation of Shell sort\n\n");
    printf("%8s %5s %9s %9s    ", "h", "pass", "cmp", "exch");
    show();
    printf("----------------------------------\n");


   /***************************************************
    * shellsort with Knuth increment sequence
    ***************************************************/
    totalcmp = totalexch = totalpass = 0;

    // compute initial increment sequence
    for (h = 1; h <= N / 3; h = 3*h + 1)
        ;

    // shellsort it
    while (h > 0) {
        exch = cmp = pass = 0;
        done = FALSE;
        while (!done) {
            done = TRUE;
            for (i = N - 1; i >= h; i--) {
                cmp++;
                if (a[i] < a[i-h]) {
                    temp = a[i]; a[i] = a[i-h]; a[i-h] = temp;
                    done = FALSE;
                    exch++;
                }
            }
            pass++;

            if (N <= MAXPRINT && !done) {
                printf("%8d %5d %23s", h, pass, "");
                show();
            }
            
        }
        totalexch += exch;
        totalcmp  += cmp;
        totalpass += pass;
        printf("%8d %5d %9d %9d    ", h, pass, cmp, exch);
        show();
        h /= 3;
    }

    // footer
    printf("----------------------------------\n");
    printf("%8s %5d %9d %9d\n", "Total", totalpass, totalcmp, totalexch);

    return 0;
}
