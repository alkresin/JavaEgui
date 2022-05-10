#!/bin/bash
rm ./test/*.class
#javac -classpath ./classes test/Test1.java
javac -classpath ./classes test/$1.java
