# Qabel documentation
For the documentation take a look at the [wiki](https://github.com/Qabel/qabel-doc/wiki/Table-of-contents) in our documentation [repository](https://github.com/Qabel/qabel-doc).

qabel-helloworld-module
=======================

Helloworld example module for Qabel

## building source

0. Make sure you have a working [git client](http://git-scm.com/) installed

0. clone the source

   ```
   git clone https://github.com/Qabel/qabel-helloworld-module.git
   ```
0. build the project

   ```
   cd qabel-helloworld-module
   git submodule init
   git submodule update
   ./gradlew jar
   ```
0. Continue with the qabel-desktop client

   https://github.com/Qabel/qabel-desktop
