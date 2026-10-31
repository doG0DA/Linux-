@echo off
chcp 65001 >nul
rem Ошибки в параметрах командной строки
cd /d "%~dp0.."
echo.
echo ===== Запуск: --unknown x
call run.bat --unknown x < nul
echo.
echo ===== Запуск: --vfs
call run.bat --vfs < nul
echo.
echo ===== Запуск: --vfs a.xml --vfs b.xml
call run.bat --vfs a.xml --vfs b.xml < nul
