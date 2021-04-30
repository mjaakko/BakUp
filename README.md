# BakUp [![Build status](https://github.com/mjaakko/BakUp/actions/workflows/test-and-build.yml/badge.svg)](https://github.com/mjaakko/BakUp/actions/workflows/test-and-build.yml)
BakUp is a very simple tool for creating backups. No complex configuration, no unnecessary features. Packages for Arch Linux can be downloaded from [GitHub releases](https://github.com/mjaakko/BakUp/releases).

## Usage
BakUp can be used either by running the `bakup` script or by running the JAR file with `java -jar bakup.jar`.

Example for taking a backup of `/home/example/Documents` and saving the result to `/home/example/backups`: 
```bash
bakup -d /home/example/Documents -o /home/example/backups -n 5
```

The `-n` flag controls how many backups can be saved. The oldest backup archives will be deleted once that number is exceeded.