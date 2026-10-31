@echo off
chcp 65001 >nul
rem Команды touch, chown и их ошибки
cd /d "%~dp0.."
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/stage5.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/stage5.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_args.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_args.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_parent.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_no_parent.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_parent_is_file.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/touch_parent_is_file.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_flag.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_flag.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_owner.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_bad_owner.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_no_path.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_no_path.txt < nul
echo.
echo ===== Запуск: --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_not_found.txt
call run.bat --vfs examples/vfs/deep.xml --script examples/scripts/errors/chown_not_found.txt < nul
