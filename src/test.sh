#!/bin/bash

JAVA_PATH=/home/student/jdk-20.0.2/bin/java
JAVAC_PATH=/home/student/jdk-20.0.2/bin/javac

MAIN_CLASS=StockExchangeProgram
MAIN_JAVA_FILE=StockExchangeProgram.java

$JAVAC_PATH $MAIN_JAVA_FILE
$JAVA_PATH $MAIN_CLASS ../examples/seller/seller 2


rm *.class