#!/usr/bin/env bash

# unzip all of the test cases from ICPC and rename to be testCases
javac Cheese.java
for file in cheese/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Cheese < ${file} > cheese/${fname}.out
    diff cheese/${fname}.out cheese/${fname}.ans
done
echo "Done."