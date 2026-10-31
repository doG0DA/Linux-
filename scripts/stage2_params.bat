@echo off
chcp 65001 >nul
rem Все поддерживаемые параметры по отдельности и вместе
cd /d "%~dp0.."
echo.
echo ===== Запуск: --vfs examples/vfs/minimal.xml
call run.bat --vfs examples/vfs/minimal.xml < nul
echo.
echo ===== Запуск: --script examples/scripts/stage2.txt
call run.bat --script examples/scripts/stage2.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/minimal.xml --script examples/scripts/stage2.txt
call run.bat --vfs examples/vfs/minimal.xml --script examples/scripts/stage2.txt < nul
echo.
echo ===== Запуск: --script examples/scripts/stage2.txt --vfs examples/vfs/minimal.xml
call run.bat --script examples/scripts/stage2.txt --vfs examples/vfs/minimal.xml < nul
