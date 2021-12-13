@echo off
del classes\alkresin\egui\*.class
javac -d classes src/alkresin/egui/*.java
rem jar cvf egui.jar -C classes .