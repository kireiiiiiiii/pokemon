@echo off
setlocal enabledelayedexpansion

REM Exit immediately if a command fails
set "errorlevel="

REM Check if javac is available
where javac >nul 2>nul
if errorlevel 1 (
    echo Error: Java compiler (javac) is not available. Please install JDK and ensure it's in your PATH.
    exit /b 1
)

REM Check if jar is available
where jar >nul 2>nul
if errorlevel 1 (
    echo Error: JAR tool is not available. Please ensure the JDK is correctly installed and in your PATH.
    exit /b 1
)

REM Create the build directory if it doesn't exist
if not exist build (
    mkdir build
)

REM Compile Java files
for /R src\main\java %%f in (*.java) do (
    set "java_files=!java_files! %%f"
)
javac -d build %java_files%
if errorlevel 1 (
    echo Error: Compilation failed.
    exit /b 1
)

REM Create the MANIFEST.MF file
echo Main-Class: pokemon.App > build\MANIFEST.MF

REM Create the JAR file
jar cfm build\pokemon.jar build\MANIFEST.MF -C build .
if errorlevel 1 (
    echo Error: Failed to create JAR file.
    exit /b 1
)

REM Output success message
echo Build successful. JAR file is located at build\pokemon.jar
