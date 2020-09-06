#!/bin/bash

VERSION=$1
TARGET=$2

# Checkout
git clone https://github.com/carlkuesters/carl-bot
if [ -n "$VERSION" ]; then
  git checkout "$VERSION"
fi

# Build
mvn clean install

# Deploy
rm -rf "${TARGET}data"
mv data "${TARGET}"
mv target/carl-bot-1.0-SNAPSHOT-jar-with-dependencies.jar "${TARGET}carl-bot.jar"
sh "${TARGET}control.sh" restart