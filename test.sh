#!/bin/sh
# Сборка и запуск тестов JUnit 5. При первом запуске JUnit скачивается в папку lib.
cd "$(dirname "$0")" || exit 1
JUNIT=lib/junit-platform-console-standalone-1.10.2.jar
if [ ! -f "$JUNIT" ]; then
    mkdir -p lib
    curl -L -o "$JUNIT" https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar || exit 1
fi
javac -encoding UTF-8 -cp "$JUNIT" -d out-tests src/*.java tests/*.java || exit 1
java -Dstdout.encoding=UTF-8 -jar "$JUNIT" execute --class-path out-tests --scan-class-path
