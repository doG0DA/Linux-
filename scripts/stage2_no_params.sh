#!/bin/sh
# Запуск без параметров
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: "
./run.sh  < /dev/null
