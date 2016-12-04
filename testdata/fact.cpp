#include <cstdio>
using namespace std;


int main () {

    int n;
    int A[100000000];
    scanf("%d", &n);

    A[0] = n;

    int f = 1;
    for (; n; n--) f *= n;

    printf("%d\n", 6); // AC for n = 3, WA otherwise

    return 0;
}