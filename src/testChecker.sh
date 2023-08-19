#!/bin/bash

JAVA_PATH=/home/student/jdk-20.0.2/bin/java
JAVAC_PATH=/home/student/jdk-20.0.2/bin/javac

MAIN_CLASS=Checker
MAIN_JAVA_FILE=Checker.java

$JAVAC_PATH $MAIN_JAVA_FILE
$JAVA_PATH $MAIN_CLASS ../examples/seller/seller ../examples/seller/seller.log

rm *.class