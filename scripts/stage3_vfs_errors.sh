#!/bin/sh
# Ошибки загрузки VFS: нет файла, неверный формат
cd "$(dirname "$0")/.."
echo
echo "===== Запуск: --vfs examples/vfs/no_such.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/no_such.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/bad/broken.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/bad/broken.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/bad/wrong_root.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/bad/wrong_root.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/bad/bad_base64.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/bad/bad_base64.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/bad/duplicate.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/bad/duplicate.xml --script examples/scripts/stage3.txt < /dev/null
echo
echo "===== Запуск: --vfs examples/vfs/bad/unknown_tag.xml --script examples/scripts/stage3.txt"
./run.sh --vfs examples/vfs/bad/unknown_tag.xml --script examples/scripts/stage3.txt < /dev/null
