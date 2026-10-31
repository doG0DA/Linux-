@echo off
chcp 65001 >nul
rem Запуск без параметров
cd /d "%~dp0.."
echo.
echo ===== Запуск: 
call run.bat  < nul
