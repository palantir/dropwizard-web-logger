#!/bin/bash

set -e

cd $(dirname $0)/..

JAVA_HOME=$JAVA_1_8_HOME
./gradlew clean publish --refresh-dependencies

# trigger RTFM build
curl -XPOST --insecure https://rtfm.yojoe.local/build/dropwizard-bundles
