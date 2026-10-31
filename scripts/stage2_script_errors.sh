#!/bin/sh
# Ошибки стартового скрипта
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --script examples/scripts/no_such_script.txt"
./run.sh --script examples/scripts/no_such_script.txt < /dev/null
echo
echo "===== Запуск: --script examples/scripts/stage2_error.txt"
./run.sh --script examples/scripts/stage2_error.txt < /dev/null
