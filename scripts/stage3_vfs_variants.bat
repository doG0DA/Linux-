@echo off
chcp 65001 >nul
rem Работа с разными вариантами VFS: минимальная, несколько файлов, глубокая
cd /d "%~dp0.."
echo.
echo ===== Запуск: --vfs examples/vfs/minimal.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/minimal.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/multi.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/multi.xml --script examples/scripts/stage3.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage3.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/stage3.txt < nul
