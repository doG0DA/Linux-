@echo off
chcp 65001 >nul
rem Ошибки загрузки VFS: нет файла, неверный формат
cd /d "%~dp0.."
echo.
echo ===== Запуск: --vfs examples/vfs/no_such.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/no_such.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/bad/broken.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/bad/broken.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/bad/wrong_root.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/bad/wrong_root.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/bad/bad_base64.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/bad/bad_base64.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/bad/duplicate.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/bad/duplicate.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/bad/unknown_tag.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/bad/unknown_tag.xml --script examples/scripts/stage3.txt < nul
