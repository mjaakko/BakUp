#!/bin/bash
# Package required files to a tar.gz package
cp bin/bakup .
cp build/libs/bakup.jar .
tar -cvzf bakup.tar.gz bakup bakup.jar
rm bakup
rm bakup.jar