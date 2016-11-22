# PSetGrader
An open source grader for problem sets / contests that don't have an online grader. More features to come.## Problem Set Storage
Storing the actual problem set will consist of a directory structure with the following layout:

__ Problem Set X
_jjk_ config.txt [1]
__
__ Problem A
__ __ config.txt
__ __
__ __ Sub Task 1
__ __ __ input
__ __ __ __ in1.txt
__ __ __ __ in2.txt
__ __ __ __ [....]
__ __ __ 
__ __ __ output
__ __ __ __ out1.txt
__ __ __ __ out2.txt
__ __ __ __ [....]
__ __ 
__ __ Sub Task 2
__ __ __ [...]
__
__ Problem B
__ __ config.txt
__ __ [...]
