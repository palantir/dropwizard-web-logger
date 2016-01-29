#!/usr/bin/env bash

if ! which pandoc > /dev/null; then
  echo "You must install pandoc to use this!"
  echo "brew install pandoc"
  exit 1
fi

### Pipeline setup. ###
## Cause bash to exit with an error on any simple command.
set -e
## Cause bash to exit with an error on any command in a pipeline.
set -o pipefail

if [[ -z "$1" ]]; then
  echo "Usage: $(basename $0) version"
  exit 1
fi

cd $(dirname $0)/..

baseline-changelog "$1" docs/config/changelog.yml
