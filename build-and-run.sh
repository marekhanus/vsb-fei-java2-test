#!/bin/bash

cd java2-lab02-common-v1
mvn clean install
cd ../java2-lab02-db-v1
mvn clean install
cd ../java2-lab02-file-v1
mvn clean install
cd ../java2-lab02-v1
mvn clean package
java --module-path target/java2-lab02-v1-0.0.1-SNAPHOST.jar:target/libs -m cz.vsb.fei.java2.lab02_module/lab.gui.App
cd ..
