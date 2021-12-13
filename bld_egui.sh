#!/bin/bash
rm ./classes/alkresin/egui/*.class
javac -d ./classes ./src/alkresin/egui/*.java
#jar cvf egui.jar -C classes .