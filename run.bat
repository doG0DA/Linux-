@echo off
rem Сборка и запуск эмулятора. Параметры передаются эмулятору.
chcp 65001 >nul
pushd "%~dp0"
javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 (popd & exit /b 1)
popd
java -Dstdout.encoding=UTF-8 -cp "%~dp0out" Main %*
