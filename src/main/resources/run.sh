#!/bin/bash

java -cp ../lib/question-1.0-SNAPSHOT-jar-with-dependencies.jar evariant.interview.Main  ../files/states.csv ../files/CountyMSA.txt ../files/wbanmasterlist.psv  ../files/201505precip.txt  > result.out
exit ${?}
