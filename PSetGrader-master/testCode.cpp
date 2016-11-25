//#include <bits/stdc++.h>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
//#include <random>
#include <time.h>
using namespace std;

int main () {

    // uncomment for time limit
    //int lol = 0; while (true) {lol++;}

    // this does not affect the output, it outputs to a different folder
    // the output for a different folder tells if the code is working as it outputs a random each time

    cout << "this is a test code!" << endl;

    //freopen("shopping.in", "r", stdin);
    freopen("testCodeOutput.txt", "w", stdout);

    cout << "this is the output" << endl;

    srand( time(NULL) );
    long long ri = rand()%1000;
    cout << "random " << ri << endl;
}
