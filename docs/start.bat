@echo off
set "JAR_FILE=ecassist.jar"
set "JAR_PATH=%~dp0%JAR_FILE%"
start /min javaw -jar "%JAR_PATH%"
exit