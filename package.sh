#!/bin/bash
# Package required files to a tar.gz package
cp bin/bakup .
cp build/libs/bakup.jar .
cp -R systemd/. .
tar -cvzf bakup.tar.gz bakup bakup.jar bakup@.service bakup@.timer example
rm bakup
rm bakup.jar
rm bakup@.service
rm bakup@.timer
rm example