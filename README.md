# PSetGrader
An open source grader for problem sets / contests that don't have an online grader. More features to come.## Problem Set 

## Storage
Storing the actual problem set will consist of a directory structure with the following layout:

```
.
└── Problem Set X
    ├── config.txt
    |
    ├── Problem A
    │   ├── config.txt
    |   |
    │   ├── Sub Task 1
    │   │   ├── input
    │   │   │   ├── in1.txt
    |   |   |      ....
    │   │   │   └── in[n].txt
    │   │   └── output
    │   │       ├── out1.txt
    |   |          ....
    │   │       └── out[n].txt
    |   |
    |     ....
    |   |
    |   |
    │   ├── Sub Task [n]
    |   |   ....
    |   |
    |
       .... 
    |
    ├── Problem [n]
    |   ....
    |
    
```
