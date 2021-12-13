@echo off
del test\*.class
rem javac -sourcepath ./src -d bin src/alkresin/egui_test/Test1.java
javac -classpath ./classes test/Test1.java
rem jar cvf Test1.jar -C bin .