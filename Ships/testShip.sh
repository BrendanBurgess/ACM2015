#!/usr/bin/env bash

# unzip all of the test cases from ICPC and rename to be testCases
for file in Ships/*.in; do
#    echo ${file}
    fname="$(basename ${file%.*})"
    echo ${fname}
    # javac Ships.java
    # java Ships < ${file} > /Ships/${fname}.out
    # diff /Ships/${fname}.out /Ships/${fname}.ans
done
echo "Done."