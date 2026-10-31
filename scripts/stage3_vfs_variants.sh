#!/bin/sh
# Работа с разными вариантами VFS: минимальная, несколько файлов, глубокая
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --vfs examples/vfs/minimal.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/minimal.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/multi.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/multi.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/stage3.txt < /dev/null
