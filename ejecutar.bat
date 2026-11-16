@echo off
rem Ejecuta ContaGest Spring en Windows (requiere JDK 17+ y Maven).
rem Luego abra http://localhost:8080
cd /d "%~dp0"
mvn spring-boot:run
