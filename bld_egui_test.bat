@echo off
del test\*.class
javac -classpath ./classes test/Test1.java
rem jar cvf Test1.jar -C bin .