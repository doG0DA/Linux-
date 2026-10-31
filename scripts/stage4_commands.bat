@echo off
chcp 65001 >nul
rem Команды ls, cd, rev, echo, clear и их ошибки
cd /d "%~dp0.."
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage4.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/stage4.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_not_dir.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_not_dir.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_too_many.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/cd_too_many.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/clear_args.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/clear_args.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_bad_flag.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_bad_flag.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_not_found.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/ls_not_found.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_dir.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_dir.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_no_args.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/rev_no_args.txt < nul
