@echo off
chcp 65001 >nul
rem Ошибки стартового скрипта
cd /d "%~dp0.."
echo.
echo ===== Запуск: --script examples/scripts/no_such_script.txt
call run.bat --script examples/scripts/no_such_script.txt < nul
echo.
echo ===== Запуск: --script examples/scripts/stage2_error.txt
call run.bat --script examples/scripts/stage2_error.txt < nul
