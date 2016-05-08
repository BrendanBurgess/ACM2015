#!/usr/bin/env bash

# unzip all of the test cases from ICPC and rename to be testCases
javac Evolution.java
for file in evolution/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Evolution < ${file} > evolution/${fname}.out
    diff evolution/${fname}.out evolution/${fname}.ans
done
echo "Done."