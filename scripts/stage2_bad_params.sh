#!/bin/sh
# Ошибки в параметрах командной строки
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --unknown x"
./run.sh --unknown x < /dev/null
echo
echo "===== Запуск: --vfs"
./run.sh --vfs < /dev/null
echo
echo "===== Запуск: --vfs a.xml --vfs b.xml"
./run.sh --vfs a.xml --vfs b.xml < /dev/null
