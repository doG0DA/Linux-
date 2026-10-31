#!/bin/sh
# Команды touch, chown и их ошибки
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage5.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/stage5.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_args.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_args.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_parent.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_parent.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_parent_is_file.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_parent_is_file.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_flag.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_flag.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_owner.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_owner.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_no_path.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_no_path.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_not_found.txt"
./run.sh --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_not_found.txt < /dev/null
