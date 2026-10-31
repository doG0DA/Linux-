@echo off
rem Сборка и запуск тестов JUnit 5. При первом запуске JUnit скачивается в папку lib.
chcp 65001 >nul
cd /d "%~dp0"
set JUNIT=lib\junit-platform-console-standalone-1.10.2.jar
if not exist "%JUNIT%" (
    if not exist lib mkdir lib
    curl -L -o "%JUNIT%" https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar || exit /b 1
)
javac -encoding UTF-8 -cp "%JUNIT%" -d out-tests src\*.java tests\*.java || exit /b 1
java -Dstdout.encoding=UTF-8 -jar "%JUNIT%" execute --class-path out-tests --scan-class-path
