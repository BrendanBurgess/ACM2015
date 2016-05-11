#!/usr/bin/env bash

# unzip all of the test cases from ICPC and rename to be testCases
javac Catering.java
for file in catering/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java Catering < ${file} > catering/${fname}.out
    diff catering/${fname}.out catering/${fname}.ans
done
echo "Done."