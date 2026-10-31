#!/bin/sh
# Команды ls, cd, rev, echo, clear и их ошибки
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage4.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/stage4.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_not_dir.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_not_dir.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_too_many.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_too_many.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/clear_args.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/clear_args.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_bad_flag.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_bad_flag.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_not_found.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_not_found.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_dir.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_dir.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_no_args.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_no_args.txt < /dev/null
