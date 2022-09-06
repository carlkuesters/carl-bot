#!/bin/bash

VERSION=$1
TARGET=$2

# Checkout
git clone https://github.com/carlkuesters/carl-bot
if [ -n "$VERSION" ]; then
  git checkout "$VERSION"
fi

# Build
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64;mvn clean install

# Deploy
rm -rf "${TARGET}data"
mv data "${TARGET}"
rm -rf "${TARGET}tmp"
mkdir "${TARGET}tmp"
mv target/carl-bot-1.0-SNAPSHOT-jar-with-dependencies.jar "${TARGET}carl-bot.jar"
sh "${TARGET}control.sh" restart