#!/bin/bash

cd java2-lab02-v1
java --module-path target/java2-lab02-v1-0.0.1-SNAPHOST.jar:target/libs -m cz.vsb.fei.java2.lab02_module/lab.gui.App
cd ..
