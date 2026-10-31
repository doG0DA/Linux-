#!/bin/sh
# Сборка и запуск эмулятора. Параметры передаются эмулятору.
DIR=$(cd "$(dirname "$0")" && pwd)
javac -encoding UTF-8 -d "$DIR/out" "$DIR"/src/*.java || exit 1
java -Dstdout.encoding=UTF-8 -cp "$DIR/out" Main "$@"
