#!/bin/bash

set -e

cd $(dirname $0)/..

JAVA_HOME=$JAVA_1_8_HOME
./gradlew clean publish --refresh-dependencies
