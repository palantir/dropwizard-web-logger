#!/bin/bash

set -e
set -o pipefail

cd "$(dirname "$0")"/..

: ${JAVA_1_8_HOME:=/usr/java/jdk1.8.0_latest}
export JAVA_HOME=$JAVA_1_8_HOME
export PATH=$JAVA_HOME/bin:$PATH

./gradlew clean build --continue --refresh-dependencies --info
