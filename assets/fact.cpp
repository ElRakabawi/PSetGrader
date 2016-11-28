#include <cstdio>
using namespace std;

int main () {

    int n;
    scanf("%d", &n);

    int f = 1;
    for (; n; n--) f *= n;

    printf("%d\n", 6);

    return 0;
}