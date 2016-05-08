#!/usr/bin/env bash

# unzip all of the test cases from ICPC and rename to be testCases
javac KeyTwo.java
for file in keyboard/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    java KeyTwo < ${file} > keyboard/${fname}.out
    diff keyboard/${fname}.out keyboard/${fname}.ans
done
echo "Done."