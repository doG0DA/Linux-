#!/bin/sh
# Все поддерживаемые параметры по отдельности и вместе
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --vfs examples/vfs/minimal.xml"
./run.sh --vfs examples/vfs/minimal.xml < /dev/null
echo
echo "===== Запуск: --script examples/scripts/stage2.txt"
./run.sh --script examples/scripts/stage2.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/minimal.xml --script examples/scripts/stage2.txt"
./run.sh --vfs examples/vfs/minimal.xml --script examples/scripts/stage2.txt < /dev/null
echo
echo "===== Запуск: --script examples/scripts/stage2.txt --vfs examples/vfs/minimal.xml"
./run.sh --script examples/scripts/stage2.txt --vfs examples/vfs/minimal.xml < /dev/null
