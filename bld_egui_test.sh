#!/bin/bash
rm ./test/*.class
#javac -sourcepath ./src -d ./bin ./src/alkresin/egui_test/Test1.java
javac -classpath ./classes test/Test1.java
