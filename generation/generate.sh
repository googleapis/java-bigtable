#!/bin/bash

set -e

basedir=$(dirname "$(readlink -f "$0")")

generator_branch=$(cat "${basedir}/generator-branch")

mkdir -p target
rm -rf target/sdk-platform-java
git clone --depth=1 --branch "${generator_branch}" https://github.com/googleapis/sdk-platform-java target/sdk-platform-java

repo_path=$(realpath "${basedir}/..")
REPO="${repo_path}" sh -x target/sdk-platform-java/generation/generate.sh //google/bigtable/v2

